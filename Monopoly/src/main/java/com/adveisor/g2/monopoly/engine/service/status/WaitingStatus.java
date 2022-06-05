/*
 * Copyright (c) ReichTUM 2022.
 */

package com.adveisor.g2.monopoly.engine.service.status;

import com.adveisor.g2.monopoly.engine.service.GameService;
import com.adveisor.g2.monopoly.engine.service.model.player.Player;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class WaitingStatus extends AbstractStatus {

    public static final int MAX_PLAYER_COUNT = 4;
    public WaitingStatus(GameService gameService) {
        super(gameService);
    }

    @Override
    public Player join(Player player){

        updateGameVersionId();
        // check for player limit
        if(gameService.getPlayerCount() >= MAX_PLAYER_COUNT) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Player number limit exceeded");
        // get players from the game

        // create player
        player.initialize(gameService.getGame());
        gameService.getPlayers().add(player);

        return player;
    }

    @Override
    public void start() {
        updateGameVersionId();
        //start the game
        if(gameService.getPlayers().size()>=2) {
            gameService.getGame().setCurrentPlayerId(gameService.getPlayers().get(0).getPlayerId());
            gameService.setCurrentStatus(gameService.getDiceStatus());
        }

    }

    @Override
    public String toString() {
        return "Waiting-Status";
    }

}
