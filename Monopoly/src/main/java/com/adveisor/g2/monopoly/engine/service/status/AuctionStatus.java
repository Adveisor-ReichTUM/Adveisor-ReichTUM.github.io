/*
 * Copyright (c) ReichTUM 2022.
 */

package com.adveisor.g2.monopoly.engine.service.status;

import com.adveisor.g2.monopoly.engine.service.GameService;
import com.adveisor.g2.monopoly.engine.service.model.PlayerBid;
import com.adveisor.g2.monopoly.engine.service.model.board.Field;
import com.adveisor.g2.monopoly.engine.service.model.player.Player;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class AuctionStatus extends AbstractStatus {

    public static final int MAX_AUCTION_TIME = 10 * 1000;

    Field auctionedProperty;
    private PlayerBid highestBid;
    private boolean auctionRunning;
    public AuctionStatus(GameService gameService) {
        super(gameService);
        auctionRunning = false;
        highestBid = new PlayerBid();
    }


    @Override
    public PlayerBid startAuction(int fieldIndex) throws InterruptedException{
        auctionedProperty = gameService.getGame().getBoard().getField(fieldIndex);
        Thread timer = new Thread(() ->
        {
            try {
                Thread.sleep(MAX_AUCTION_TIME);
            } catch (InterruptedException e) {
                auctionRunning = false;
            }
        });
        timer.start();
        highestBid.setBid(auctionedProperty.getPrice()/2);
        auctionRunning = true;
        timer.join();
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
        Player winner = gameService.getGame().findPlayerById(highestBid.getPlayerId()).orElseThrow();
        winner.adjustBalance(-highestBid.getBid());
        auctionedProperty.setOwned(true);
        auctionedProperty.setOwnerId(winner.getPlayerId());
        gameService.setCurrentStatus(gameService.getTurnStatus());
        return highestBid;
    }

    @Override
    public String toString() {
        return "Auction-Status";
    }
}
