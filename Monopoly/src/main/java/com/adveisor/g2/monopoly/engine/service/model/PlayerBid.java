/*
 * Copyright (c) ReichTUM 2022.
 */

package com.adveisor.g2.monopoly.engine.service.model;

import lombok.Data;

@Data
public class PlayerBid {
    private String playerId;
    private int bid;
}
