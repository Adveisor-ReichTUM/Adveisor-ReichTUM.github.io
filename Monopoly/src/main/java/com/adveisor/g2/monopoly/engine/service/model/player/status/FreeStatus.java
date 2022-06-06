/*
 * Copyright (c) ReichTUM 2022.
 */

package com.adveisor.g2.monopoly.engine.service.model.player.status;

import com.adveisor.g2.monopoly.engine.service.model.Game;
import com.adveisor.g2.monopoly.engine.service.model.board.Color;
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
            player.jail();
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

    @Override
    public void sellPropertyToBank(int fieldIndex){
        Field field = player.getGame().getBoard().getFields().get(fieldIndex);
        if(!player.getPossession(fieldIndex)) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Can't sell properties not in procession");
        if(field.isHypothek()){
            player.endMortgage(fieldIndex);
            return;
        }
        if(field.getNumHouses()>0) return;
        player.adjustBalance(field.getPrice()/2);
        player.setPossession(fieldIndex, false);
        field.reset();
    }

    @Override
    public void investRealEstate(int fieldIndex) {
        Game game = player.getGame();
        if (!player.getPossession(fieldIndex))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Must own the property before buying house");

        Field field = player.getGame().getBoard().getFields().get(fieldIndex);

        if (game.getNumHouses() <= 0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No house left in the bank");
        if (field.getNumHouses() == 4 && game.getNumHotels() <= 0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No hotel left in the bank");
        if (field.isHypothek())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You can't build house on mortgaged property");

        Color color = field.getColor();
        int minHouses = Integer.MAX_VALUE;
        // check if all the properties of a color are in players possession
        for (int i = 0; i < 40; i++) {
            Field running = game.getBoard().getFields().get(i);
            if (running.getColor() == color) {
                if (running.getNumHouses() < minHouses) minHouses = running.getNumHouses();
                if (!player.getPossession(i)) return;
            }
        }


        if(field.getNumHouses()>minHouses) return;

        if(field.getNumHouses()==4){
            player.adjustBalance(-field.getHouseCost());
            field.setNumHouses(field.getNumHouses()+1);
            game.setNumHotels(game.getNumHotels()-1);
            game.setNumHouses(game.getNumHouses() +4);
        } else{
            player.adjustBalance(-field.getHouseCost());
            field.setNumHouses(field.getNumHouses()+1);
            game.setNumHouses(game.getNumHouses()-1);
        }
    }

    @Override
    public void sellHouse(int fieldIndex){
        Game game = player.getGame();
        Field field = game.getBoard().getField(fieldIndex);

        if(!player.getPossession(fieldIndex))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You do not own this property, can't sell house");

        if(field.getNumHouses() <= 0)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No houses on this property to sell");



        Color color = field.getColor();
        int maxHouses = Integer.MIN_VALUE;
        // check if all the properties of a color are in players possession
        for(int i = 0; i<40; i++){
            Field running = game.getBoard().getFields().get(i);
            if(running.getColor()==color){
                if(running.getNumHouses()>maxHouses) maxHouses = running.getNumHouses();
            }
        }

        if(field.getNumHouses()<maxHouses) return;

        if(field.getNumHouses()==5){
            if(game.getNumHouses()<4) return;
            player.adjustBalance(field.getHouseCost()/2);
            field.setNumHouses(field.getNumHouses()-1);
            game.setNumHouses(game.getNumHotels()+1);
            game.setNumHouses(game.getNumHouses() -4);
        } else{
            player.adjustBalance(field.getHouseCost()/2);
            field.setNumHouses(field.getNumHouses()-1);
            game.setNumHouses(game.getNumHouses()+1);
        }
    }
}
