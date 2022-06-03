/*
 * Copyright (c) ReichTUM 2022.
 */

package com.adveisor.g2.monopoly.engine.service.model.player.status;

import com.adveisor.g2.monopoly.engine.service.model.board.Field;
import com.adveisor.g2.monopoly.engine.service.model.player.Player;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class FreeStatus extends PlayerStatus {

    private int consecutivePasch;
    public FreeStatus(Player player) {
        super(player);
    }

    @Override
    public void jail() {
        player.setPosition(10); // where the jail is at
        player.setCurrentStatus(player.getInJailStatus());
    }

    public void moveForward(int steps) {
        int originalPosition = player.getPosition();
        player.setPosition((player.getPosition() + steps) % 40);

        // if the player pass "go"
        if(player.getPosition() < originalPosition){
            player.adjustBalance(200);
        }
    }

    @Override
    public void handlePasch(boolean isPausch) {
        if (isPausch) {
            consecutivePasch++;
        } else {
            consecutivePasch = 0;
        }
        if (consecutivePasch >= 3) {
            consecutivePasch = 0;
            jail();
        }
    }

    @Override
    public boolean inPausch() {
        return consecutivePasch > 0;
    }

    @Override
    public Field buyProperty(Field field) {
        if (field.isOwned()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Property already owned by another player");
        }
        player.adjustBalance(-field.getPrice());
        player.getStreets()[field.getPosition()] = true;
        field.setOwned(true);
        field.setOwnerId(player.getPlayerId());
        return field;
    }
}
