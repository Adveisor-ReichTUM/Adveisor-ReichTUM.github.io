/*
 * Copyright (c) ReichTUM 2022.
 */

package com.adveisor.g2.monopoly.engine.service.model.deck;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Deck {
    private final boolean isChanceDeck; // if it's not chance deck, it's community deck

    private final List<Card> cards = new CopyOnWriteArrayList<>();

    public Deck(boolean isChanceDeck, String filename){
        this.isChanceDeck = isChanceDeck;
        try{
            InputStream inputStream = getClass().getResourceAsStream(filename);
            assert inputStream != null;
            BufferedReader cardSet = new BufferedReader(new InputStreamReader(inputStream));
            String input = cardSet.readLine();
            String[] args;
            while(input != null){
                args = input.split(" - ");
                cards.add(new Card(args[2], args[0], args[1], isChanceDeck));
                input = cardSet.readLine();
            }
            cardSet.close();
        } catch(java.io.IOException exception){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Deck initialization failed");
        }

        shuffleDeck();
    }

    public void shuffleDeck(){
        Collections.shuffle(this.getCards());
    }

    public Card takeCard(){
        Card card = cards.remove(0);
        // put card to the back of the deck
        cards.add(card);
        return card;
    }

    public List<Card> getCards(){
        return this.cards;
    }

}
