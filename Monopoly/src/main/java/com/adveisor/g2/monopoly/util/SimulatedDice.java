/*
 * Copyright (c) ReichTUM 2022.
 */

package com.adveisor.g2.monopoly.util;

import com.adveisor.g2.monopoly.engine.service.model.Dice;

import java.util.Random;

public class SimulatedDice {
    private static final Random rand = new Random();

    public static Dice generateDiceResult() {
        Dice simulatedDice = new Dice();
        simulatedDice.setFirstThrow(rand.nextInt(5) + 1);
        simulatedDice.setSecondThrow(rand.nextInt(5) + 1);
        return simulatedDice;
    }
}
