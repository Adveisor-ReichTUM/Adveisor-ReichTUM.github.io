package com.adveisor.g2.monopoly.engine.service.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    Player player;
    Game game;
    @BeforeEach
    void setUp() {
        game = new Game("/text/board.txt", "/text/chanceDeck.txt", "/text/CommunityDeck.txt");
        player = new Player("Mr. Monopoly", game, Piece.GREEN);
    }

    @Test
    void move() {
        player.setPosition(39);
        player.setDice(10);
        player.move();
        int expected = 9;
        int actual = player.getPosition();
        assertEquals(expected, actual);
    }

    @Test
    void moveGO() {
        player.setPosition(39);
        player.setDice(10);
        player.move();
        int expected = 1700;
        int actual = player.getBalance();
        assertEquals(expected, actual);
    }

    @Test
    void buy() {
        Field field = game.getBoard().getFields().get(39);
        player.buy(field);
        boolean expected = true;
        boolean actual = field.getOwner() == player.getId();
        assertEquals(expected, actual);
    }

    @Test
    void calculateWealth() {
        player.setNumJailCards(1);
        player.setPossession(39, true);
        game.getBoard().getFields().get(39).setNumHouses(1);
        player.setPossession(1, true);
        game.getBoard().getFields().get(1).setNumHouses(3);
        player.setPossession(5, true);
        player.setPossession(3, true);
        int expected = 1500 + 50 + 400 + 60 + 60 + 200 + 3*50 + 200;
        int actual = player.calculateWealth();
        assertEquals(expected, actual);
    }

    @Test
    void endMortgage() {
        Field field = game.getBoard().getFields().get(5);
        field.setHypothek(true);
        player.setPossession(5, true);
        game.setCurrentStatus(game.getTurnStatus());
        player.endMortgage(5);
        int expected = 1390;
        int actual = player.getBalance();
        assertEquals(expected, actual);
    }

    @Test
    void startMortgage1() {
        Field field = game.getBoard().getFields().get(5);
        player.setPossession(5, true);
        game.setCurrentStatus(game.getTurnStatus());
        player.startMortgage(5);
        int expected = 1600;
        int actual = player.getBalance();
        assertEquals(expected, actual);
    }

    @Test
    void startMortgage2() {
        Field field = game.getBoard().getFields().get(5);
        field.setNumHouses(1);
        player.setPossession(5, true);
        game.setCurrentStatus(game.getTurnStatus());
        player.startMortgage(5);
        boolean expected = false;
        boolean actual = field.isHypothek();
        assertEquals(expected, actual);
    }

}