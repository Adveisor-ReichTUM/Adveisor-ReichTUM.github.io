package com.adveisor.g2.monopoly.engine.service.model;

import com.adveisor.g2.monopoly.engine.service.model.status.AbstractStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FieldTest {
    Field field, field2;
    Game game;
    Player paying_pl, paid_pl;

    @BeforeEach
    void setUp() {
        game = new Game("/text/board.txt", "/text/chanceDeck.txt", "/text/CommunityDeck.txt");
        game.join("Mr. Monopoly", Piece.GREEN);
        game.join("Mr. Monopoly 2", Piece.BLUE);
        Player.nextId=0;
        paying_pl = game.getPlayers().get(0);
        paid_pl = game.getPlayers().get(1);
        field = game.getBoard().getFields().get(39);
        field2 = game.getBoard().getFields().get(4);
    }

    @Test
    void evaluateField1() {
        paid_pl.setPossession(39, true);
        field.setOwner(paid_pl.getId());
        field.setOwned(true);
        Field.evaluateField(field, paying_pl, game);
        int expected = 1450;
        int actual = paying_pl.getBalance();
        assertEquals(expected, actual);
    }

    @Test
    void evaluateField2() {
        Field.evaluateField(field2, paying_pl, game);
        int expected = 1300;
        int actual = paying_pl.getBalance();
        assertEquals(expected, actual);
    }

    @Test
    void evaluateField3() {
        Deck deck = game.getChanceDeck();
        Card unexpected = deck.getCards().get(0);
        Field field3 = game.getBoard().getFields().get(7);
        Field.evaluateField(field3, paying_pl, game);
        Card actual = deck.getCards().get(0);
        assertNotEquals(unexpected, actual);
    }

    @Test
    void evaluateStreet1() {
        paid_pl.setPossession(39, true);
        field.setOwner(paid_pl.getId());
        field.setOwned(true);
        field.evaluateStreet(paying_pl, game);
        int expected = 1450;
        int actual = paying_pl.getBalance();
        assertEquals(expected, actual);
    }

    @Test
    void evaluateStreet2() {
        field.evaluateStreet(paying_pl, game);
        AbstractStatus expected = game.getBuyPropertyStatus();
        AbstractStatus actual = game.getCurrentStatus();
        assertEquals(expected, actual);
    }

    @Test
    void transaction() {
        Field.transaction(paying_pl, paid_pl, 200);
        int expected = 1700;
        int actual = paid_pl.getBalance();
        assertEquals(expected, actual);
    }

    @Test
    void payRent1() {
        field.setNumHouses(3);
        field.payRent(paying_pl, paid_pl, game.getBoard());
        int expected = 100;
        int actual = paying_pl.getBalance();
        assertEquals(expected, actual);
    }

    @Test
    void payRent2() {
        paid_pl.setPossession(39, true);
        paid_pl.setPossession(37, true);
        field.payRent(paying_pl, paid_pl, game.getBoard());
        int expected = 1400;
        int actual = paying_pl.getBalance();
        assertEquals(expected, actual);
    }

    @Test
    void determineStationStage1() {
        Field station1 = game.getBoard().getFields().get(5);
        station1.setOwner(1);
        station1.setOwned(true);
        paid_pl.setPossession(5, true);
        Field station2 = game.getBoard().getFields().get(15);
        station2.setOwner(1);
        station2.setOwned(true);
        paid_pl.setPossession(15, true);
        int expected = 2;
        int actual = station1.determineStationStage(paid_pl, game.getBoard());
        assertEquals(expected, actual);
    }

    @Test
    void determineStationStage2() {
        Field station1 = game.getBoard().getFields().get(5);
        station1.setOwner(1);
        station1.setOwned(true);
        paid_pl.setPossession(5, true);
        Field station2 = game.getBoard().getFields().get(15);
        station2.setOwner(1);
        station2.setOwned(true);
        paid_pl.setPossession(15, true);

        game.setCurrentStatus(game.getTurnStatus());
        game.setCurrentPlayer(paid_pl.getId());
        game.startMortgage(15);

        int expected = 0;
        int actual = station2.determineStationStage(paid_pl, game.getBoard());
        assertEquals(expected, actual);
    }

    @Test
    void determineUtilityStage() {
        Field utility = game.getBoard().getFields().get(12);
        utility.setOwner(1);
        utility.setOwned(true);
        paid_pl.setPossession(12, true);

        int expected = 1;
        int actual = utility.determineUtilityStage(paid_pl, game.getBoard());
        assertEquals(expected, actual);
    }

    @Test
    void determineStreetStage() {
        Field street = game.getBoard().getFields().get(1);
        street.setNumHouses(3);
        int expected = 4;
        int actual = street.determineStreetStage();
        assertEquals(expected, actual);
    }

}