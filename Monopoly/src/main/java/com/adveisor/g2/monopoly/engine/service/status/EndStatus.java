/*
 * Copyright (c) ReichTUM 2022.
 */

package com.adveisor.g2.monopoly.engine.service.status;

import com.adveisor.g2.monopoly.engine.service.GameService;
import com.adveisor.g2.monopoly.engine.service.model.player.Player;
import lombok.Getter;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Getter
public class EndStatus extends AbstractStatus {

    private final List<Player> playersList = new CopyOnWriteArrayList<>();
    public EndStatus(GameService gameService) {
        super(gameService);
    }
    @Override
    public String toString() {
        return "End-Status";
    }

    public void addPlayer(Player player) {
        this.playersList.add(player);
    }
}
