/*
 * Copyright (c) ReichTUM 2022.
 */

package com.adveisor.g2.monopoly.engine.service.status;

import com.adveisor.g2.monopoly.engine.service.GameService;
import com.adveisor.g2.monopoly.engine.service.model.Dice;
import com.adveisor.g2.monopoly.engine.service.model.PlayerBid;
import com.adveisor.g2.monopoly.engine.service.model.board.Field;
import com.adveisor.g2.monopoly.engine.service.model.deck.Card;
import com.adveisor.g2.monopoly.engine.service.model.player.Player;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public abstract class AbstractStatus {

    protected GameService gameService;

    AbstractStatus(GameService gameService) {
        this.gameService = gameService;
    }


    // default method throw exception, overridden in concrete statuses
    public Player join(Player player) {
        throw new IllegalStateException("Failed to join game: already running.");
    }

    public Dice diceThrow() {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dice throw not possible in current state");
    }

    public void sellPropertyToBank(int fieldIndex){
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tried to sell despite not being in TURN");
    }

    public void buyHouse(int fieldIndex){
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can not buy house while not being in TURN");
    }

    public void sellHouse(int fieldIndex){
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can not sell house while not being in TURN");
    }

    public void endMortgage(int fieldIndex){
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot end mortgage while not being in TURN.");
    }

    public void startMortgage(int fieldIndex){
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can not start Mortgage while not being in TURN");
    }

    public PlayerBid startAuction(int fieldIndex) throws InterruptedException {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    public PlayerBid tryHighestBid(PlayerBid newBid) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    public void start() {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to start game: wrong status.");
    }

    public Card takeCard(Player player) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong status to take card");
    }

    public void continueGame(){
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "must be in turn status to continue");
    }

    public void endGame() {
        gameService.setCurrentStatus(gameService.getEndStatus());
    }

    public void useJailCard() {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Must be in turn to take action");
    }

    public void buyOutOfJail() {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Must be in turn to take action");
    }

    public Field buyProperty(Field currentPlayerStandingField) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Must be in turn to take action");
    }

    @Override
    public String toString() {
        return "unspecified";
    }

}
