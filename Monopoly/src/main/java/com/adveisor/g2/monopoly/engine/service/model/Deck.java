package com.adveisor.g2.monopoly.engine.service.model;

import com.adveisor.g2.monopoly.engine.service.GameService;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.util.List;
import java.util.Collections;

public class Deck {
    private final boolean isChanceDeck;
    private List<Card> cards = new ArrayList<Card>();

    public Deck(boolean isChanceDeck, String filename){
        this.isChanceDeck = isChanceDeck;
        try{
            InputStream inputStream = getClass().getResourceAsStream(filename);
            BufferedReader cardset = new BufferedReader(new InputStreamReader(inputStream));
            String input = cardset.readLine();
            String[] args;
            while(input != null){
                args = input.split(" - ");
                cards.add(new Card(args[2], args[0], args[1], isChanceDeck));
                input = cardset.readLine();
            }
            cardset.close();
        }
        catch(java.io.IOException exception){
            System.err.println(exception);
        }

        shuffleDeck();
    }

    public void shuffleDeck(){
        Collections.shuffle(this.getCards());
    }

    public void takeCard(Player player, GameService gameService){
        Card card = cards.get(0);
        gameService.setCurrentStatus(gameService.getCardStatus());
        card.evaluateCard(player, gameService);
        gameService.setCardDescription(card.getDescription());
        // put card to the back of the deck
        cards.remove(card);
        cards.add(card);
    }

    public List<Card> getCards(){
        return this.cards;
    }

}
