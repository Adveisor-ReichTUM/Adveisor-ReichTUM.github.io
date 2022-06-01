/*
 * Copyright (c) ReichTUM 2022.
 */

package com.adveisor.g2.monopoly.engine.service.model.status;

import com.adveisor.g2.monopoly.engine.service.model.Color;
import com.adveisor.g2.monopoly.engine.service.model.Field;
import com.adveisor.g2.monopoly.engine.service.GameService;
import com.adveisor.g2.monopoly.engine.service.model.Player;

public class TurnStatus extends AbstractStatus {

    public TurnStatus(GameService gameService) {
        super(gameService);
    }

    @Override
    public void sellBank(int fieldIndex){
        updateGame();
        int currentPlayer = gameService.getCurrentPlayer();
        Player player = gameService.getPlayers().get(currentPlayer);
        Field field = gameService.getBoard().getFields().get(fieldIndex);
        if(!player.getPossession(fieldIndex)) throw new IllegalStateException("Tried to sell property not in possession");
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
        updateGame();
        int currentPlayer = gameService.getCurrentPlayer();
        Player player = gameService.getPlayers().get(currentPlayer);
        if(!player.getPossession(fieldIndex)) return;

        Field field = gameService.getBoard().getFields().get(fieldIndex);

        if(gameService.getNumHouses()<=0) return;
        if(field.getNumHouses()==4 && gameService.getNumHotels()<=0) return;
        if(field.isHypothek()) return;

        Color color = field.getColor();
        int minHouses = Integer.MAX_VALUE;
        // check if all the properties of a color are in players possession
        for(int i = 0; i<40; i++){
            Field running = gameService.getBoard().getFields().get(i);
            if(running.getColor()==color){
                if(running.getNumHouses()<minHouses) minHouses = running.getNumHouses();
                if(!player.getPossession(i)) return;
            }
        }

        if(field.getNumHouses()>minHouses) return;

        if(field.getNumHouses()==4){
            player.adjustBalance(-field.getHouseCost());
            field.setNumHouses(field.getNumHouses()+1);
            gameService.setNumHotels(gameService.getNumHotels()-1);
            gameService.setNumHouses(gameService.getNumHouses() +4);
        } else{
            player.adjustBalance(-field.getHouseCost());
            field.setNumHouses(field.getNumHouses()+1);
            gameService.setNumHouses(gameService.getNumHouses()-1);
        }
    }

    @Override
    public void sellHouse(int fieldIndex){
        updateGame();
        if(gameService.getNumHouses()<=0) return;

        Player player = gameService.getPlayers().get(gameService.getCurrentPlayer());
        if(!player.getPossession(fieldIndex)) return;

        Field field = gameService.getBoard().getFields().get(fieldIndex);

        Color color = field.getColor();
        int maxHouses = Integer.MIN_VALUE;
        // check if all the properties of a color are in players possession
        for(int i = 0; i<40; i++){
            Field running = gameService.getBoard().getFields().get(i);
            if(running.getColor()==color){
                if(running.getNumHouses()>maxHouses) maxHouses = running.getNumHouses();
            }
        }

        if(field.getNumHouses()<maxHouses) return;

        if(field.getNumHouses()==5){
            if(gameService.getNumHouses()<4) return;
            player.adjustBalance(field.getHouseCost()/2);
            field.setNumHouses(field.getNumHouses()-1);
            gameService.setNumHouses(gameService.getNumHotels()+1);
            gameService.setNumHouses(gameService.getNumHouses() -4);
        } else{
            player.adjustBalance(field.getHouseCost()/2);
            field.setNumHouses(field.getNumHouses()-1);
            gameService.setNumHouses(gameService.getNumHouses()+1);
        }
    }

    @Override
    public void endMortgage(int fieldIndex){
        updateGame();
        Player player = gameService.getPlayers().get(gameService.getCurrentPlayer());
        player.endMortgage(fieldIndex);
    }

    @Override
    public void startMortgage(int fieldIndex){
        updateGame();
        Player player = gameService.getPlayers().get(gameService.getCurrentPlayer());
        player.startMortgage(fieldIndex);
    }
}
