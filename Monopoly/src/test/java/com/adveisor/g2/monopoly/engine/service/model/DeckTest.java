package com.adveisor.g2.monopoly.engine.service.model;

import com.adveisor.g2.monopoly.engine.service.GameService;
import com.adveisor.g2.monopoly.engine.service.model.deck.Card;
import com.adveisor.g2.monopoly.engine.service.model.deck.Deck;
import com.adveisor.g2.monopoly.engine.service.model.player.Piece;
import com.adveisor.g2.monopoly.engine.service.model.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeckTest {
    Deck chanceDeck;
    Deck communityDeck;
    @BeforeEach
    void setUp() throws Exception{
        chanceDeck = new Deck(true, "/text/chanceDeck.txt");
        communityDeck = new Deck(false, "/text/communityDeck.txt");
    }

    @Test
    void generation1(){
        assertNotNull(chanceDeck);
    }

    @Test
    void generation2(){
        assertNotNull(communityDeck);
    }

    @Test
    void generation3(){
        assertNotNull(communityDeck.getCards());
    }


//    @Test
//    void takeCard() {
//        GameService gameService = new GameService("/text/board.txt", "/text/chanceDeck.txt", "/text/CommunityDeck.txt");
//        Player player = new Player("Mr. Monopoly", gameService, Piece.GREEN);
//        Card expected = communityDeck.getCards().peek();
//        communityDeck.takeCard(player, gameService);
//        Card actual = communityDeck.getCards().get(communityDeck.getCards().size()-1);
//        assertEquals(expected, actual);
//    }
}