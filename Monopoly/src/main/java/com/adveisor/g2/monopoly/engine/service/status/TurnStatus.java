/*
 * Copyright (c) ReichTUM 2022.
 */

package com.adveisor.g2.monopoly.engine.service.status;

import com.adveisor.g2.monopoly.engine.service.GameService;
import com.adveisor.g2.monopoly.engine.service.model.board.Color;
import com.adveisor.g2.monopoly.engine.service.model.board.Field;
import com.adveisor.g2.monopoly.engine.service.model.deck.Card;
import com.adveisor.g2.monopoly.engine.service.model.player.Player;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class TurnStatus extends AbstractStatus {

    public TurnStatus(GameService gameService) {
        super(gameService);
    }

    @Override
    public void sellBank(int fieldIndex){
        updateGameVersionId();
        Player player = gameService.getCurrentPlayer();
        Field field = gameService.getGame().getBoard().getFields().get(fieldIndex);
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
    public void buyHouse(int fieldIndex){
        updateGameVersionId();
        Player player = gameService.getCurrentPlayer();
        if(!player.getPossession(fieldIndex)) return;

        Field field = gameService.getGame().getBoard().getFields().get(fieldIndex);

        if(gameService.getGame().getNumHouses()<=0) return;
        if(field.getNumHouses()==4 && gameService.getGame().getNumHotels()<=0) return;
        if(field.isHypothek()) return;

        Color color = field.getColor();
        int minHouses = Integer.MAX_VALUE;
        // check if all the properties of a color are in players possession
        for(int i = 0; i<40; i++){
            Field running = gameService.getGame().getBoard().getFields().get(i);
            if(running.getColor()==color){
                if(running.getNumHouses()<minHouses) minHouses = running.getNumHouses();
                if(!player.getPossession(i)) return;
            }
        }

        if(field.getNumHouses()>minHouses) return;

        if(field.getNumHouses()==4){
            player.adjustBalance(-field.getHouseCost());
            field.setNumHouses(field.getNumHouses()+1);
            gameService.getGame().setNumHotels(gameService.getGame().getNumHotels()-1);
            gameService.getGame().setNumHouses(gameService.getGame().getNumHouses() +4);
        } else{
            player.adjustBalance(-field.getHouseCost());
            field.setNumHouses(field.getNumHouses()+1);
            gameService.getGame().setNumHouses(gameService.getGame().getNumHouses()-1);
        }
    }

    @Override
    public void sellHouse(int fieldIndex){
        updateGameVersionId();
        if(gameService.getGame().getNumHouses()<=0) return;

        Player player = gameService.getCurrentPlayer();
        if(!player.getPossession(fieldIndex)) return;

        Field field = gameService.getGame().getBoard().getFields().get(fieldIndex);

        Color color = field.getColor();
        int maxHouses = Integer.MIN_VALUE;
        // check if all the properties of a color are in players possession
        for(int i = 0; i<40; i++){
            Field running = gameService.getGame().getBoard().getFields().get(i);
            if(running.getColor()==color){
                if(running.getNumHouses()>maxHouses) maxHouses = running.getNumHouses();
            }
        }

        if(field.getNumHouses()<maxHouses) return;

        if(field.getNumHouses()==5){
            if(gameService.getGame().getNumHouses()<4) return;
            player.adjustBalance(field.getHouseCost()/2);
            field.setNumHouses(field.getNumHouses()-1);
            gameService.getGame().setNumHouses(gameService.getGame().getNumHotels()+1);
            gameService.getGame().setNumHouses(gameService.getGame().getNumHouses() -4);
        } else{
            player.adjustBalance(field.getHouseCost()/2);
            field.setNumHouses(field.getNumHouses()-1);
            gameService.getGame().setNumHouses(gameService.getGame().getNumHouses()+1);
        }
    }

    @Override
    public void endMortgage(int fieldIndex){
        updateGameVersionId();
        Player player = gameService.getCurrentPlayer();
        player.endMortgage(fieldIndex);
    }

    @Override
    public void startMortgage(int fieldIndex){
        updateGameVersionId();
        Player player = gameService.getCurrentPlayer();
        player.startMortgage(fieldIndex);
    }

    @Override
    public Card takeCard(Player player) {
        gameService.validateActivePlayer(player);
        return gameService.getCurrentPlayer().takeCard();
    }

    @Override
    public Player takeCardInstruction(Player player) {
        gameService.validateActivePlayer(player);
        return gameService.getCurrentPlayer().takeCardInstruction();
    }

    @Override
    public void continueGame() {
        int bankruptPlayerCount = 0;
        if (!this.gameService.getCurrentPlayer().inPausch()) {
            do {
                gameService.setNextPlayer();
            } while (gameService.getCurrentPlayer().isBankrupt() && ++bankruptPlayerCount < gameService.getPlayerCount());
        }
        if (bankruptPlayerCount >= gameService.getPlayerCount() - 1) {
            gameService.setTurnStatus(gameService.getEndStatus());
        } else {
            gameService.setCurrentStatus(gameService.getDiceStatus());
        }
    }
}
