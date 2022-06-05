/*
 * Copyright (c) ReichTUM 2022.
 */

package com.adveisor.g2.monopoly.engine.service.model.status;

import com.adveisor.g2.monopoly.engine.service.model.Game;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class AuctionStatus extends AbstractStatus {

    public AuctionStatus(Game game) {
        super(game);
    }
}
