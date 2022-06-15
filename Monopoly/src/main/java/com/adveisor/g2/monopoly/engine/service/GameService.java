/*
 * Copyright (c) ReichTUM 2022.
 */

package com.adveisor.g2.monopoly.engine.service;

import com.adveisor.g2.monopoly.config.MqttClientSingleton;
import com.adveisor.g2.monopoly.engine.service.model.Dice;
import com.adveisor.g2.monopoly.engine.service.model.PlayerBid;
import com.adveisor.g2.monopoly.engine.service.model.board.Field;
import com.adveisor.g2.monopoly.engine.service.model.Game;
import com.adveisor.g2.monopoly.engine.service.model.deck.Card;
import com.adveisor.g2.monopoly.engine.service.model.mqtt.MqttPublishModel;
import com.adveisor.g2.monopoly.engine.service.model.player.Player;
import com.adveisor.g2.monopoly.engine.service.status.*;
import com.adveisor.g2.monopoly.util.Logger;
import lombok.Getter;
import lombok.Setter;
import org.eclipse.paho.client.mqttv3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.server.ResponseStatusException;


import java.util.*;

@Getter
@Setter
@Service
public class GameService {
    private final Game game;

    // all the possible statuses initialized here
    private AbstractStatus auctionStatus = new AuctionStatus(this);
    private DiceStatus diceStatus = new DiceStatus(this);
    private AbstractStatus turnStatus = new TurnStatus(this);
    private EndStatus endStatus = new EndStatus(this);
    private WaitingStatus waitingStatus = new WaitingStatus(this);
    //

    private AbstractStatus currentStatus;

    @Autowired
    public GameService(Game game) {
        this.game = game;
        // game start at this status
        this.currentStatus = this.waitingStatus;
    }

    public void resetGame() {
        game.resetGame();
    }
    public void setNextPlayer() {
        game.setNextPlayer();
    }

    public void incrementGameVersionId() {
        this.game.incrementVersionId();
    }

    public Long getGameVersionId() {
        return this.game.getVersionId();
    }

    public int getPlayerCount() {
        return this.game.getPlayers().size();
    }

    public List<Player> getPlayers() {
        return this.game.getPlayers();
    }

    public Player getCurrentPlayer() {
        return game.findCurrentPlayer();
    }

    public String getCurrentPlayerId() {

        return game.getCurrentPlayerId();
    }
    // util above to get game info

    /**
     * The method is used to validate if the player trying to make action is the current player
     * if not, the access will be blocked with HTTP 401 Unauthorized
     * @param player a player object of which only the playId field must be specified for validation
     */
    public Player validateActivePlayer(Player player) {
        if (!Objects.equals(player.getPlayerId(), getCurrentPlayerId())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Player validation failed, non active player cannot take action");
        }
        return getCurrentPlayer();
    }

    // player action below

    /**
     * When the game is in waiting status, new player can attempt to join
     * returned is a player that has been initialized
     * @param player a player object of which only the playerName and piece field is required
     * @return  an initialized player that has a generated playerId and all the initial data determined by the game
     */
    public Player join(Player player) {
        incrementGameVersionId();
        return currentStatus.join(player);
    }

    /**
     * Put the game from waiting status to start.
     * Will only take effect in waiting status, otherwise exception is thrown
     */
    public void start(){
        incrementGameVersionId();
        currentStatus.start();
    }

    /**
     * @return a Field object describing the field that the current player is standing on
     */
    public Field currentPlayerStandingField() {
        incrementGameVersionId();
        return getCurrentPlayer().standingOnField();
    }

    /**
     *
     * @param player The player who is intending to take a card
     * @return a Card object representing the card taken
     */
    public Card takeCard(Player player) {
        incrementGameVersionId();
        return currentStatus.takeCard(validateActivePlayer(player));
    }

    /**
     * Put the game in end status
     */
    public List<Player> end(){
        incrementGameVersionId();
        currentStatus = endStatus;

        List<Player> playerRank = endStatus.getPlayersList().subList(0, endStatus.getPlayersList().size());
        Collections.reverse(playerRank);

        return playerRank;
    }

    public List<Player> getGameResult() {
        if (currentStatus != endStatus) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Game not ended yet");
        }
        List<Player> playerRank = endStatus.getPlayersList().subList(0, endStatus.getPlayersList().size());
        Collections.reverse(playerRank);
        return playerRank;
    }


    /**
     * A dice-throw and move according to the throw result.
     * only possible when game is in dice status
     * @param dice a `Dice` object representing the result of the two dice.
     *             if it is not present(null passed), a simulated dice will be used
     * @return a complete `Dice` object containing boolean field `pasch` to indicate whether the throw is a double
     */
    public Dice diceThrow(Dice dice) {
        incrementGameVersionId();
        var toBeReturned = currentStatus.diceThrow(dice);
        Logger.log(this);
        return toBeReturned;
    }

    /**
     * End the current player's round
     * @param player playerId required for validation
     */
    public void continueGame(Player player) {
        incrementGameVersionId();
        validateActivePlayer(player);
        currentStatus.continueGame();
    }

    /**
     * use jail card to get out of jail immediately.
     * throw exception if player is not in jail or do not have jail cards.
     * @param player playerId required for validation.
     */
    public void useJailCard(Player player){
        incrementGameVersionId();
        validateActivePlayer(player);
        currentStatus.useJailCard();
    }

    /**
     * player hands in 50 currency units to get out of jail immediately.
     * throw exception if player is not in jail.
     * @param player playerId required for validation.
     */
    public void buyOutOfJail(Player player) {
        incrementGameVersionId();
        validateActivePlayer(player);
        currentStatus.buyOutOfJail();
    }

    /**
     * current player buy the field he/she is currently standing on
     * @param player playerId required for validation.
     */
    public Field buyProperty(Player player) {
        incrementGameVersionId();
        validateActivePlayer(player);
        return currentStatus.buyProperty(currentPlayerStandingField());
    }

    public void sellPropertyToBank(int fieldIndex, Player player){
        incrementGameVersionId();
        validateActivePlayer(player);
        currentStatus.sellPropertyToBank(fieldIndex);
    }


    /**
     *
     * @param player the player who has bankrupted
     */
    public void startBankruptAuction(Player player){
        incrementGameVersionId();
        Player targetPlayer = game.findPlayerById(player.getPlayerId()).orElseThrow();
        if (!targetPlayer.isBankrupt()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Player is not bankrupt");
        } else {
            endStatus.addPlayer(player);
        }

        for(int i = 0; i<39; i++) {
            if(targetPlayer.getPossession(i)){
                auctionProperty(i);
            }
        }
    }

    public void auctionProperty(int fieldIndex){
        incrementGameVersionId();
        currentStatus = auctionStatus;
        try {
            currentStatus.startAuction(fieldIndex);
        } catch (InterruptedException e) {
            Logger.log("InterruptedException occurred. Auction ended.");
        }
    }

    public void tryHighestBid(PlayerBid playerBid){
        incrementGameVersionId();
        currentStatus.tryHighestBid(playerBid);
    }

    public void startMortgage(int fieldIndex, Player player){
        incrementGameVersionId();
        validateActivePlayer(player);
        currentStatus.startMortgage(fieldIndex);
    }


    public void endMortgage(int fieldIndex, Player player){
        incrementGameVersionId();
        validateActivePlayer(player);
        currentStatus.endMortgage(fieldIndex);
    }


    public void buyHouse(int fieldIndex, Player player){
        incrementGameVersionId();
        validateActivePlayer(player);
        currentStatus.buyHouse(fieldIndex);
    }


    public void sellHouse(int fieldIndex, Player player){
        incrementGameVersionId();
        validateActivePlayer(player);
        currentStatus.sellHouse(fieldIndex);
    }


    public static void transaction(Player paying_pl, Player paid_pl, int diff){
        paying_pl.adjustBalance(-diff);
        paid_pl.adjustBalance(diff);
    }


    // MQTT methods below

    public static void MqttPublishMessage(MqttPublishModel messagePublishModel, BindingResult bindingResult) throws MqttException {

        if (bindingResult.hasErrors()) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "MQTT ERROR: Some parameter(s) invalid");
        }

        MqttMessage mqttMessage = new MqttMessage(messagePublishModel.getMessage().getBytes());
        mqttMessage.setQos(messagePublishModel.getQos());
        mqttMessage.setRetained(messagePublishModel.getRetained());

        MqttClientSingleton.getInstance().publish(messagePublishModel.getTopic(), mqttMessage);
    }

    public static void MqttSubscribeChannelTest(String topic, Integer waitMillis)
            throws InterruptedException, MqttException {
        //CountDownLatch countDownLatch = new CountDownLatch(10);
        MqttClientSingleton.getInstance().subscribeWithResponse(topic, (mqttTopic, mqttMessage) -> {
            Logger.log("Under Topic: " + mqttTopic + "\n" + mqttMessage);
            //countDownLatch.countDown();
        });

        //countDownLatch.await(waitMillis, TimeUnit.MILLISECONDS);
    }
}
