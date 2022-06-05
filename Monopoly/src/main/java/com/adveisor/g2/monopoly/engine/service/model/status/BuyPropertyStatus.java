/*
 * Copyright (c) ReichTUM 2022.
 */

package com.adveisor.g2.monopoly.engine.service.model.status;

import com.adveisor.g2.monopoly.engine.service.model.Game;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class BuyPropertyStatus extends AbstractStatus {

    public BuyPropertyStatus(Game game) {
        super(game);
    }
}