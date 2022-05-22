/*
 * Copyright (c) ReichTUM 2022.
 */

package com.adveisor.g2.monopoly.engine.service.model.status;

import com.adveisor.g2.monopoly.engine.service.model.Game;
import com.adveisor.g2.monopoly.engine.service.model.Piece;

public abstract class AbstractStatus {

    protected Game game;

    AbstractStatus(Game game) {
        this.game = game;
    }

    // default method throw exception, overridden in concrete statuses
    public void join(String name, Piece piece) {
        throw new IllegalStateException("Failed to join game: already running.");
    }

    public void start() {
        throw new IllegalStateException("Failed to start game: wrong status.");
    }
}
