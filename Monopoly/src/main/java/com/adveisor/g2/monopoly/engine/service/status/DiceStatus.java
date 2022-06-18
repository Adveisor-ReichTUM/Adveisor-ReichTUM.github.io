/*
 * Copyright (c) ReichTUM 2022.
 */

package com.adveisor.g2.monopoly.engine.service.status;

import com.adveisor.g2.monopoly.engine.service.GameService;
import com.adveisor.g2.monopoly.engine.service.model.Dice;
import com.adveisor.g2.monopoly.engine.service.model.board.Field;
import com.adveisor.g2.monopoly.engine.service.model.mqtt.MqttPublishModel;
import com.adveisor.g2.monopoly.engine.service.model.player.Player;
import com.adveisor.g2.monopoly.util.Logger;
import com.adveisor.g2.monopoly.util.SimulatedDice;
import lombok.Getter;

@Getter
public class DiceStatus extends AbstractStatus {

    private Dice dice = new Dice();
    public DiceStatus(GameService gameService) {
        super(gameService);
    }

    @Override
    public Dice diceThrow() {
        Player player = gameService.getCurrentPlayer();
        // if no dice result is given, use simulated dice
        if (!dice.isValid()) {
            Logger.log("Using simulated dice");
            dice = SimulatedDice.generateDiceResult();
        }
        dice.setIfPasch();

        player.handlePasch(dice.isPasch());

        //Bewegen des Spielers und Evaluation der Position
        player.moveForward(dice.getTotal());

        gameService.setStatus("TURN");
        return dice;
    }

    @Override
    public String toString() {
        return "DICE";
    }
}
