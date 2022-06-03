/*
 * Copyright (c) ReichTUM 2022.
 */

package com.adveisor.g2.monopoly.engine.service.status;

import com.adveisor.g2.monopoly.engine.service.GameService;
import com.adveisor.g2.monopoly.engine.service.model.Dice;
import com.adveisor.g2.monopoly.engine.service.model.board.Field;
import com.adveisor.g2.monopoly.engine.service.model.player.Player;
import com.adveisor.g2.monopoly.util.SimulatedDice;

public class DiceStatus extends AbstractStatus {

    public DiceStatus(GameService gameService) {
        super(gameService);
    }

    @Override
    public Dice diceThrow(Dice dice) {
        Player player = gameService.getCurrentPlayer();

        // Würfeln
        // if no dice result is given, use simulated dice
        if (dice == null) {
            dice = SimulatedDice.generateDiceResult();
        }
        dice.setIfPasch();

        player.handlePasch(dice.isPasch());

        //Bewegen des Spielers und Evaluation der Position
        player.moveForward(dice.getTotal());

        //Option zum Bauen, Tauschen, Hypothek
        //manage();

        evaluateStandingField(player);
        gameService.setCurrentStatus(gameService.getTurnStatus());
        return dice;
    }

    private void evaluateStandingField(Player player) {
        Field standingField = player.standingOnField();
        switch (standingField.getType()) {
            case chance, community -> player.setCurrentStatus(player.getCardObligatedStatus());
        }
    }
}
