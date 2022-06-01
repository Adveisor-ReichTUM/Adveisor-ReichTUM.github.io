/*
 * Copyright (c) ReichTUM 2022.
 */

package com.adveisor.g2.monopoly.engine.service.model.status;

import com.adveisor.g2.monopoly.engine.service.GameService;

public class EndStatus extends AbstractStatus {

    public EndStatus(GameService gameService) {
        super(gameService);
    }

    @Override
    public void turn1(){
        updateGame();
    }
}
