/*
 * Copyright (c) ReichTUM 2022.
 */

package com.adveisor.g2.monopoly.engine.service.model.player.status;

import com.adveisor.g2.monopoly.engine.service.model.Game;
import com.adveisor.g2.monopoly.engine.service.model.deck.Card;
import com.adveisor.g2.monopoly.engine.service.model.player.Player;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class CardObligatedStatus extends PlayerStatus {

    private Card cardTaken;

    public CardObligatedStatus(Player player) {
        super(player);
    }

    @Override
    public void moveForward(int steps) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You are obligated to take a card");
    }

    @Override
    public Card takeCard() {
        Game game = player.getGame();
        int currentPosition = player.getPosition();
        cardTaken = switch (game.getBoard().getField(currentPosition).getType()) {
            case chance -> game.getChanceDeck().takeCard();
            case community -> game.getCommunityDeck().takeCard();
            default -> throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not in a take card field");
        };
        takeCardInstruction();
        return cardTaken;
    }

    private void takeCardInstruction() {
//        if (cardTaken == null) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No card taken yet");
//        }
        player.consumeCard(cardTaken);
        player.setCurrentStatus(player.getFreeStatus()); // The player is free after taking the card action
    }
}
