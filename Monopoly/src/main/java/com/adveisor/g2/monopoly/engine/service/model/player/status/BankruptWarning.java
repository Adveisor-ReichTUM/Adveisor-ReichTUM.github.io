/*
 * Copyright (c) ReichTUM 2022.
 */

package com.adveisor.g2.monopoly.engine.service.model.player.status;

import com.adveisor.g2.monopoly.engine.service.model.player.Player;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class BankruptWarning extends PlayerStatus{

    public BankruptWarning(Player player) {
        super(player);
    }

    @Override
    public void moveForward(int steps) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Clear your bankrupt warning!");
    }


    @Override
    public String toString() {
        return "Bankrupt-Warning";
    }

    @Override
    public void endCurrentRound() {
        if (player.getPotentialAbilityToPay() + player.getBalance() > 0) { // potentially able to pay
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Try to pay off your debt!");
        } else {
            player.getGame().bankrupt(player);
        }
    }
}
