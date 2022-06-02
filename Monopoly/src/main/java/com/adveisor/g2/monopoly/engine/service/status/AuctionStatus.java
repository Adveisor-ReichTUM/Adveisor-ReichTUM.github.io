/*
 * Copyright (c) ReichTUM 2022.
 */

package com.adveisor.g2.monopoly.engine.service.status;

import com.adveisor.g2.monopoly.engine.service.GameService;
import com.adveisor.g2.monopoly.engine.service.model.PlayerBid;
import com.adveisor.g2.monopoly.engine.service.model.player.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class AuctionStatus extends AbstractStatus {

    public static final int MAX_AUCTION_TIME = 10 * 1000;
    private PlayerBid highestBid;
    private boolean auctionRunning;
    public AuctionStatus(GameService gameService) {
        super(gameService);
        auctionRunning = false;
        highestBid = new PlayerBid();
    }


    @Override
    public PlayerBid startAuction(int fieldIndex){
        Thread timer = new Thread(() ->
        {
            try {
                Thread.sleep(MAX_AUCTION_TIME);
            } catch (InterruptedException e) {
                auctionRunning = false;
            }
        });
        timer.start();
        highestBid.setBid(gameService.getGame().getBoard().getFields().get(fieldIndex).getPrice()/2);
        auctionRunning = true;

        try {
            timer.join();
        } catch (InterruptedException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        auctionRunning = false;
        return exitAuction();
    }

    public synchronized PlayerBid tryHighestBid(PlayerBid newBid) {
        if (!auctionRunning) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Auction is not active");
        }
        if (highestBid.getBid() < newBid.getBid()) {
            highestBid = newBid;
        }
        return highestBid;
    }


    public PlayerBid exitAuction() {
        gameService.setCurrentStatus(gameService.getTurnStatus());
        return highestBid;
    }
}
