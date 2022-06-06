/*
 * Copyright (c) ReichTUM 2022.
 */

package com.adveisor.g2.monopoly.engine.service.model.player;

import com.adveisor.g2.monopoly.engine.service.GameService;
import com.adveisor.g2.monopoly.engine.service.model.*;
import com.adveisor.g2.monopoly.engine.service.model.board.Color;
import com.adveisor.g2.monopoly.engine.service.model.board.Field;
import com.adveisor.g2.monopoly.engine.service.model.deck.Card;
import com.adveisor.g2.monopoly.engine.service.model.player.status.CardObligatedStatus;
import com.adveisor.g2.monopoly.engine.service.model.player.status.FreeStatus;
import com.adveisor.g2.monopoly.engine.service.model.player.status.InJailStatus;
import com.adveisor.g2.monopoly.engine.service.model.player.status.PlayerStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;
import java.util.UUID;

import static com.adveisor.g2.monopoly.engine.service.model.board.FieldType.tax;

@Data
@NoArgsConstructor
@Component
public class Player {

    private static final int START_MONEY = 1500;   // amount of money available in the beginning
    // player basic info, for identification
    private String playerId;
    private String playerName;

    // player position and figure, visualization related
    private Piece piece;
    private int position;       // position on the field array: 0 - 39

    // player financial info
    private int balance;        // amount of money the player owns
    private boolean bankrupt;   // criteria defining if player is still in the game
    private boolean[] streets;  // fields owned by player: true if in possession
    private int numHouses;         // number of houses owned, required for renovation cost calculation
    private int numHotels;         // number of hotels owned, required for renovation cost calculation

    // other details
    private int numJailCards;   // number of Out-of-jail cards in players possession

    private int lastDiceThrow;

    // reference attribute
    @JsonIgnore
    private Game game;          // reference object to interact with game

    // statuses
    @JsonIgnore
    private PlayerStatus freeStatus = new FreeStatus(this);
    @JsonIgnore
    private PlayerStatus inJailStatus = new InJailStatus(this);
    @JsonIgnore
    private PlayerStatus cardObligatedStatus = new CardObligatedStatus(this);
    @JsonIgnore
    private PlayerStatus currentStatus;


    // player initialization when a new player join the game
    public void initialize(Game game){
        this.balance = Player.START_MONEY;
        this.bankrupt = false;
        this.position = 0;
        this.streets = new boolean[40];    // initializes the elements with false
        this.playerId = UUID.randomUUID().toString();
        this.currentStatus = freeStatus;
        this.game = game;
    }

    public Field standingOnField() {
        return game.getBoard().getField(this.position);
    }
    public void adjustBalance(int diff){
        this.balance += diff;
    }

    public void jail(){
        currentStatus.jail();
    }

    public void setFree() {
        currentStatus.setFree();
    }

    public void handlePasch(boolean isPausch) {
        this.currentStatus.handlePasch(isPausch);
    }

    public boolean inPausch() {
        return currentStatus.inPausch();
    }
    public boolean isInJail() {
        return currentStatus.isInJail();
    }

    public void moveForward(int steps) {
        lastDiceThrow = steps;
        currentStatus.moveForward(steps);
        evaluateStandingField();
    }

    public boolean getPossession(int field_num){
        return this.streets[field_num];
    }

    public void setPossession(int field_num, boolean ownership){
        this.streets[field_num] = ownership;
    }

    public void useJailCard() {
        currentStatus.useJailCard();
    }

    public void buyOutOfJail() {
        currentStatus.buyOutOfJail();
    }


    public Field buyProperty(Field field) {
        return currentStatus.buyProperty(field);
    }

    public Card takeCard() {
        return currentStatus.takeCard();
    }

    /**
     *
     * @param card the card that should take its effect on the player.
     */
    public void consumeCard(Card card) {
        int diff;
        int target;
        switch (card.getCardType()) {
            case get_money_bank:
                diff = Integer.parseInt(card.getValue());
                adjustBalance(diff);
                break;
            case get_money_player:
                diff = Integer.parseInt(card.getValue());
                adjustBalance((game.getNumActivePlayers() - 1) * diff);
                for (Player p : game.getPlayers()) {
                    if (p != this)
                        p.adjustBalance(-diff);
                }
            case pay_money_bank:
                diff = -Integer.parseInt(card.getValue());
                adjustBalance(diff);
                break;
            case pay_money_player:
                diff = -Integer.parseInt(card.getValue());
                adjustBalance((game.getNumActivePlayers() - 1) * diff);
                for (Player p : game.getPlayers()) {
                    if (p != this)
                        p.adjustBalance(-diff);
                }
                break;
            case out_of_jail:
                this.numJailCards++;
                break;
            case move_via_GO:
                target = card.identifyTargetPosition(this.position);
                if (this.position > target) {
                    adjustBalance(200);
                }
                setPosition(target);
            case move_not_GO:
                target = card.identifyTargetPosition(this.position);
                setPosition(target);
            case renovation:
                int sum;
                int numHouses = this.numHouses;
                int numHotels = this.numHotels;
                if (card.isChanceCard())
                    sum = numHouses * 25 + numHotels * 100;
                else
                    sum = numHouses * 40 + numHotels * 115;
                adjustBalance(-sum);
        }
    }

    public void evaluateStandingField(){
        Field currentField = game.getBoard().getField(position);
        if (currentField.getType() == tax) {
            adjustBalance(-currentField.getPrice());
        } else if (currentField.isOwned() && !Objects.equals(currentField.getOwnerId(), playerId)) {
            payRentTo(game.findPlayerById(currentField.getOwnerId()).orElseThrow());
        }
    }

    public void payRentTo(Player landLord) {
        Field currentField = game.getBoard().getField(position);
        currentField.payRent(this, landLord, game.getBoard());
    }


    public void sellPropertyToBank(int fieldIndex) {
        currentStatus.sellPropertyToBank(fieldIndex);
    }

    public void investRealEstate(int fieldIndex) {
        currentStatus.investRealEstate(fieldIndex);
    }

    public void sellHouse(int fieldIndex){
        currentStatus.sellHouse(fieldIndex);
    }

    public void endMortgage(int fieldIndex){
        Field field = game.getBoard().getFields().get(fieldIndex);

        if(field.isHypothek() && getPossession(fieldIndex)){
            int diff = (int) (field.getMortgageValue() + field.getMortgageValue()*0.1);    //10% aufschlag beim Zurückzahlen
            adjustBalance(-diff);
            field.setHypothek(false);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Field not mortgaged");
        }
    }

    public void startMortgage(int fieldIndex){
        Field field = game.getBoard().getFields().get(fieldIndex);

        if(field.getNumHouses()>0) return;

        if(getPossession(fieldIndex) && !field.isHypothek()){
            adjustBalance(field.getMortgageValue());
            field.setHypothek(true);
        }
    }

    public boolean monopolyOverColor(Color color){
        for(int i = 0; i<40; i++){
            Field running = game.getBoard().getField(i);
            if(running.getColor()==color){
                if(!getPossession(i)) return false;
            }
        }
        return true;
    }

    public int calculateWealth(){

        int wealth = getBalance();
        wealth += getNumJailCards() * 50;

        for(int i = 0; i < streets.length; i++){
            if(streets[i]){
                Field field = game.getBoard().getFields().get(i);
                wealth += field.getPrice();
                wealth += field.getNumHouses() * field.getHouseCost();
            }
        }
        return wealth;
    }
}
