package com.adveisor.g2.monopoly.engine.service.model;

import com.adveisor.g2.monopoly.engine.service.GameService;
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

    @Test
    void shuffleDeck() {
        String unexpected = chanceDeck.getCards().get(10).getDescription();
        chanceDeck.shuffleDeck();
        String actual = chanceDeck.getCards().get(10).getDescription();
        assertNotEquals(unexpected, actual);
    }

    @Test
    void takeCard() {
        GameService gameService = new GameService("/text/board.txt", "/text/chanceDeck.txt", "/text/CommunityDeck.txt");
        Player player = new Player("Mr. Monopoly", gameService, Piece.GREEN);
        Card expected = communityDeck.getCards().get(0);
        communityDeck.takeCard(player, gameService);
        Card actual = communityDeck.getCards().get(communityDeck.getCards().size()-1);
        assertEquals(expected, actual);
    }
}