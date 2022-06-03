/*
 * Copyright (c) ReichTUM 2022.
 */

package com.adveisor.g2.monopoly.util;

import com.adveisor.g2.monopoly.engine.service.GameService;

public class Logger {

    private static int logNumber;

    public static void log(String msg) {
        System.out.println("------- log #" + ++logNumber + ": " + msg);
    }

    public static void log(GameService game) {
        System.out.printf(
                "------- log #%d -------\nGame-status: %s\ncurrent-player: %s\nstanding-on-field: %s\n",
                ++logNumber, game.getCurrentStatus(), game.getCurrentPlayerId(), game.currentPlayerStandingField()
        );
    }



}
