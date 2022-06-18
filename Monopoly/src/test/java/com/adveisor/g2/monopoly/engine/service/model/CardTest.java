package com.adveisor.g2.monopoly.engine.service.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardTest {
    Card card1, card2, card3, card4;
    Game game;
    Player player;

    @BeforeEach
    void setUp() {
        card1 = new Card("Gehen Sie in das Gefängnis. Begeben Sie sich direkt dorthin. Gehen Sie nicht über Los." +
                "Ziehen Sie nicht Ω 200 ein.", "move_not_GO", "Gefängnis", true);
        card2 = new Card("Krankenhausgebühren. Zahlen Sie Ω 100.", "pay_money_bank", "100", false);
        card3 = new Card("Sie lassen Ihre Häuser renovieren. Zahlen Sie: Ω 25 pro Haus, Ω 100 pro Hotel.", "renovation", "0", true);
        card4 = new Card("Rücken Sie vor bis zum nächsten Verkehrsfeld. Der Eigentümer erhält das Doppelte der normalen Miete. " +
                "Wenn das Verkehrsfeld noch niemandem gehört, können Sie es von der Bank kaufen.", "move_via_GO", "Bahnhof", true);
        game = new Game("/text/board.txt", "/text/chanceDeck.txt", "/text/CommunityDeck.txt");
        player = new Player("Mr. Monopoly", game);
    }

    @Test
    void getCardType() {
        Card.CardType expected = Card.CardType.move_not_GO;
        Card.CardType actual = card1.getCardType();
        assertEquals(expected, actual);
    }

    @Test
    void getDescription() {
        String expected = "Krankenhausgebühren. Zahlen Sie Ω 100.";
        String actual = card2.getDescription();
        assertEquals(expected, actual);
    }

    @Test
    void evaluateCard1() {
        card2.evaluateCard(player, game);
        int expected = 1400;
        int actual = player.getBalance();
        assertEquals(expected, actual);
    }

    @Test
    void evaluateCard2() {
        player.setNumHouses(4);
        player.setNumHotels(2);
        card3.evaluateCard(player, game);
        int expected = 1200;
        int actual = player.getBalance();
        assertEquals(expected, actual);
    }

    @Test
    void identifyTargetPosition1() {
        int expected = 30;
        int actual = card1.identifyTargetPosition(3);
        assertEquals(expected, actual);
    }

    @Test
    void identifyTargetPosition2() {
        int expected = 5;
        int actual = card4.identifyTargetPosition(36);
        assertEquals(expected, actual);
    }
}