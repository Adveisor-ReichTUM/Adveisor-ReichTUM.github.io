/*
 * Copyright (c) ReichTUM 2022.
 */

package com.adveisor.g2.monopoly.engine.service;

import com.adveisor.g2.monopoly.engine.service.model.Dice;
import com.adveisor.g2.monopoly.engine.service.model.PlayerBid;
import com.adveisor.g2.monopoly.engine.service.model.board.Field;
import com.adveisor.g2.monopoly.engine.service.model.Game;
import com.adveisor.g2.monopoly.engine.service.model.deck.Card;
import com.adveisor.g2.monopoly.engine.service.model.player.Player;
import com.adveisor.g2.monopoly.engine.service.status.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Service
public class GameService {
    private final Game game;

    // all the possible statuses initialized here
    private AbstractStatus auctionStatus = new AuctionStatus(this);
    private AbstractStatus diceStatus = new DiceStatus(this);
    private AbstractStatus turnStatus = new TurnStatus(this);
    private AbstractStatus endStatus = new EndStatus(this);
    private AbstractStatus waitingStatus = new WaitingStatus(this);
    //

    private AbstractStatus currentStatus;

    @Autowired
    public GameService(String boardFile, String chanceFile, String communityFile) {

        this.game = new Game(boardFile, chanceFile, communityFile);
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

    public Player join(Player player){
       return currentStatus.join(player);
    }

    public void start(){
        currentStatus.start();
    }

    public void validateActivePlayer(Player player) {
        if (!Objects.equals(player.getPlayerId(), getCurrentPlayerId())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Player validation failed, non active player cannot take action");
        }
    }

    public Field currentPlayerStandingField() {
        return getCurrentPlayer().standingOnField();
    }

    public Card takeCard(Player player) {
        return this.currentStatus.takeCard(player);
    }

    public Player takeCardInstruction(Player player) {
        return this.currentStatus.takeCardInstruction(player);
    }

    public void end(){
        currentStatus = endStatus;
    }

    public Dice diceThrow(Dice dice) {
        return currentStatus.diceThrow(dice);
    }

    public void continueGame(Player player) {
        validateActivePlayer(player);
        currentStatus.continueGame();
    }
    public void useJailCard(Player player){
        validateActivePlayer(player);
        getCurrentPlayer().useJailCard();
    }

    public void buyOutOfJail(Player player) {
        validateActivePlayer(player);
        getCurrentPlayer().buyOutOfJail();
    }


    public void buy(){
        Player player = game.findCurrentPlayer();
        Field field = game.getBoard().getFields().get(player.getPosition());
        player.buy(field);
    }

    public void sellBank(int fieldIndex){
        currentStatus.sellBank(fieldIndex);
    }


    public void bankrupt(Player player){
        game.bankrupt(player);
        if (game.getNumActivePlayers() < 1) {
            this.setCurrentStatus(endStatus);
        }

        for(int i = 0; i<39; i++){
            if(player.getPossession(i)){
                //Field field = game.getBoard().getFields().get(i);
                auctionProperty(i);
            }
        }
    }

    public void auctionProperty(int fieldIndex){
        currentStatus = auctionStatus;
        currentStatus.startAuction(fieldIndex);
    }

    public void tryHighestBid(PlayerBid playerBid){
        currentStatus.tryHighestBid(playerBid);
    }

    public void startMortgage(int fieldIndex){
        currentStatus.startMortgage(fieldIndex);
    }


    public void endMortgage(int fieldIndex){
        currentStatus.endMortgage(fieldIndex);
    }


    public void buyHouse(int fieldIndex){
        currentStatus.buyHouse(fieldIndex);
    }


    public void sellHouse(int fieldIndex){
        currentStatus.sellHouse(fieldIndex);
    }

}
