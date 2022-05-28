/*
 * Copyright (c) ReichTUM 2022.
 */

package com.adveisor.g2.monopoly.engine.service.model.status;

import com.adveisor.g2.monopoly.engine.service.model.Game;
import com.adveisor.g2.monopoly.engine.service.model.Piece;
import com.adveisor.g2.monopoly.engine.service.model.Player;

import java.util.List;

public class WaitingStatus extends AbstractStatus {

    public WaitingStatus(Game game) {
        super(game);
    }

    @Override
    public void join(String name, Piece piece){

        updateGame();
        // check for player limit
        if(game.getPlayers().size()==4) return;
        // get players from the game
        List<Player> players = game.getPlayers();

        for(Player player : players){
            if(player.getName().equals(name)) throw new IllegalArgumentException("This name already exists.");
            if(player.getPiece().equals(piece)) throw new IllegalArgumentException("This color is already used by another player.");
        }

        // create player
        Player player = new Player(name, game, piece);
        players.add(player);
    }

    @Override
    public void start() {
        updateGame();
        //start the game
        if(game.getPlayers().size()>=2) {
            // state transfer here?
            game.setCurrentStatus(game.getStartStatus());
        }

    }

}
