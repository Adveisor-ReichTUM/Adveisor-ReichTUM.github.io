/*
 * Copyright (c) ReichTUM 2022.
 */

package com.adveisor.g2.monopoly.engine.service.model.player.status;

import com.adveisor.g2.monopoly.engine.service.model.deck.Card;
import com.adveisor.g2.monopoly.engine.service.model.player.Player;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public abstract class PlayerStatus {
    protected Player player;

    PlayerStatus(Player player) {
        this.player = player;
    }

    public void jail(){}

    public boolean isInJail() {
        return false;
    }

    abstract public void moveForward(int steps);


    public void setFree() {}

    public void handlePasch(boolean inPausch){}

    public Card takeCard() {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not allowed to take card");
    }


    public boolean inPausch() {
        return false;
    }

    public void useJailCard() {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "It's literally a waste of jail cards.");
    }

    public void buyOutOfJail() {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You are not in jail.");
    }
}
