/*
 * Copyright (c) ReichTUM 2022.
 */

package com.adveisor.g2.monopoly.engine.service.status;

import com.adveisor.g2.monopoly.engine.service.GameService;
import com.adveisor.g2.monopoly.engine.service.model.deck.Card;
import com.adveisor.g2.monopoly.engine.service.model.player.Player;

public class TurnStatus extends AbstractStatus {

    public TurnStatus(GameService gameService) {
        super(gameService);
    }

    @Override
    public void sellPropertyToBank(int fieldIndex){
        gameService.getCurrentPlayer().sellPropertyToBank(fieldIndex);
    }

    @Override
    public void buyHouse(int fieldIndex){
        gameService.getCurrentPlayer().investRealEstate(fieldIndex);
    }

    @Override
    public void sellHouse(int fieldIndex){
        gameService.getCurrentPlayer().sellHouse(fieldIndex);
    }

    @Override
    public void endMortgage(int fieldIndex){
        Player player = gameService.getCurrentPlayer();
        player.endMortgage(fieldIndex);
    }

    @Override
    public void startMortgage(int fieldIndex){
        Player player = gameService.getCurrentPlayer();
        player.startMortgage(fieldIndex);
    }

    @Override
    public Card takeCard(Player player) {
        return player.takeCard();
    }


    @Override
    public void continueGame() {
        int bankruptPlayerCount = 0;
        gameService.getCurrentPlayer().endCurrentRound();
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

    @Override
    public String toString() {
        return "Turn-Status";
    }
}
