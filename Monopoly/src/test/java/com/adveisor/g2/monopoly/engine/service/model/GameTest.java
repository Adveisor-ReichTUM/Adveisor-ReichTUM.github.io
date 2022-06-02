package com.adveisor.g2.monopoly.engine.service.model;

import com.adveisor.g2.monopoly.engine.service.model.status.AbstractStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    Game game;
    @BeforeEach
    void setUp() {
        game = new Game("/text/board.txt", "/text/chanceDeck.txt", "/text/CommunityDeck.txt");
        game.join("Mr. Monopoly", Piece.GREEN);
        game.join("Mr. Monopoly 2", Piece.BLUE);
    }

    @Test
    void join() {
        int expected = 2;
        int actual = game.getPlayers().size();
        assertEquals(expected, actual);
    }

    @Test()
    void joinException(){
        try{
            game.join("Mr. Monopoly", Piece.RED);
        } catch(IllegalArgumentException e){
        }
    }

    @Test
    void start() {
        game.start();
        AbstractStatus expected = game.getStartStatus();
        AbstractStatus actual = game.getCurrentStatus();
        assertEquals(expected, actual);
    }

    @Test
    void startException() {
        try{
            game.start();
        } catch(IllegalStateException e){

        }
    }

    @Test
    void end() {
        Player player1 = game.getPlayers().get(0);
        Player player2 = game.getPlayers().get(1);
        player1.setBalance(1700);
        player2.setBalance(1400);
        player2.setPossession(39, true);
        game.getBoard().getFields().get(39).setNumHouses(4);
        String expected = player2.getName();
        String actual = game.end();
        assertEquals(expected, actual);
    }

    @Test
    void turn1() {
        game.turn1();
    }

    @Test
    void turn2() {
        game.turn2();
    }

    @Test
    void decideJail() {
        game.setCurrentPlayer(1);
        game.getPlayers().get(1).setBalance(1500);
        game.decideJail(true);
        int expected = 1450;
        int actual = game.getPlayers().get(1).getBalance();
        assertEquals(expected, actual);
    }

    @Test
    void decideJail2() {
        //game.setCurrentPlayer(0);
        game.getPlayers().get(0).setInJail(true);
        game.decideJail(true);
        boolean expected = false;
        boolean actual = game.getPlayers().get(0).isInJail();
        assertEquals(expected, actual);
    }

    @Test
    void useJailCard() {
        Player p = game.getPlayers().get(0);
        p.setNumJailCards(2);
        p.setInJail(true);
        game.useJailCard();
        boolean expected = false;
        boolean actual = game.getPlayers().get(0).isInJail();
        assertEquals(expected, actual);
    }

    @Test
    void buy() {
        game.setCurrentPlayer(0);
        Player p = game.getPlayers().get(0);
        p.setPosition(15);
        game.buy();
        boolean expected = true;
        boolean actual = game.getBoard().getFields().get(15).getOwner() == p.getId();
        assertEquals(expected, actual);
    }

    @Test
    void sellBank() {
        game.setCurrentStatus(game.getTurnStatus());
        Player p = game.getPlayers().get(game.getCurrentPlayer());
        Field field = game.getBoard().getFields().get(15);
        p.setPossession(15, true);
        game.sellBank(15);
        boolean actual = p.getPossession(15);
        boolean expected = false;
        assertEquals(expected, actual);
    }

    @Test
    void sellBank2() {
        game.setCurrentStatus(game.getTurnStatus());
        Player p = game.getPlayers().get(game.getCurrentPlayer());
        Field field = game.getBoard().getFields().get(15);
        p.setPossession(15, true);
        game.sellBank(15);
        int actual = p.getBalance();
        int expected = 1600;
        assertEquals(expected, actual);
    }

    /*@Test
    void auctionProperty() {
        Player p1 = game.getPlayers().get(0);
        Player p2 = game.getPlayers().get(1);

        game.setBid("Mr. Monopoly", 400);
        game.setBid("Mr. Monopoly 2", 420);

        game.auctionProperty(39);

        int expected = 1;
        int actual = game.getBoard().getFields().get(39).getOwner();
        assertEquals(expected, actual);
    }*/
    @Test
    void setBid() {
        Player p1 = game.getPlayers().get(0);
        Player p2 = game.getPlayers().get(1);

        game.setBid("Mr. Monopoly", 400);
        game.setBid("Mr. Monopoly 2", 420);

        int expected = 1;
        int actual = game.getHighestBidderIndex();
        assertEquals(expected, actual);
    }

    @Test
    void startMortgageException() {
        game.setCurrentStatus(game.getBuyPropertyStatus());
        try{
            game.startMortgage(39);
        } catch(IllegalStateException e){
        }
    }
    @Test
    void endMortgageException() {
        game.setCurrentStatus(game.getBuyPropertyStatus());
        try{
            game.startMortgage(39);
        } catch(IllegalStateException e){
        }
    }

    @Test
    void buyHouse1() {
        game.setCurrentStatus(game.getTurnStatus());
        Field field = game.getBoard().getFields().get(39);
        field.setOwner(0);
        field.setOwned(true);
        game.getPlayers().get(0).setPossession(39, true);
        game.buyHouse(39);
        int actual = game.getBoard().getFields().get(39).getNumHouses();
        int expected = 0;
        assertEquals(expected, actual);
    }

    @Test
    void buyHouse2() {
        game.setCurrentStatus(game.getTurnStatus());
        Field field1 = game.getBoard().getFields().get(37);
        Field field2 = game.getBoard().getFields().get(39);
        field1.setOwner(0);
        field1.setOwned(true);
        game.getPlayers().get(game.getCurrentPlayer()).setPossession(37, true);
        field2.setOwner(0);
        field2.setOwned(true);
        game.getPlayers().get(game.getCurrentPlayer()).setPossession(39, true);
        game.buyHouse(39);
        int actual = game.getBoard().getFields().get(39).getNumHouses();
        int expected = 1;
        assertEquals(expected, actual);
    }

    @Test
    void buyHouse3() {
        game.setCurrentStatus(game.getTurnStatus());
        Field field1 = game.getBoard().getFields().get(37);
        Field field2 = game.getBoard().getFields().get(39);
        field1.setOwner(0);
        field1.setOwned(true);
        game.getPlayers().get(game.getCurrentPlayer()).setPossession(37, true);
        field2.setOwner(0);
        field2.setOwned(true);
        game.getPlayers().get(game.getCurrentPlayer()).setPossession(39, true);
        field2.setNumHouses(1);
        game.buyHouse(39);
        int actual = game.getBoard().getFields().get(39).getNumHouses();
        int expected = 1;
        assertEquals(expected, actual);
    }
    @Test
    void sellHouse1() {
        game.setCurrentStatus(game.getTurnStatus());
        Field field = game.getBoard().getFields().get(39);
        field.setOwner(0);
        field.setOwned(true);
        game.getPlayers().get(0).setPossession(39, true);
        field.setNumHouses(1);
        game.sellHouse(39);
        int expected = 1600;
        int actual = game.getPlayers().get(game.getCurrentPlayer()).getBalance();
    }

    @Test
    void sellHouse2() {
        game.setCurrentStatus(game.getTurnStatus());
        Field field1 = game.getBoard().getFields().get(37);
        Field field2 = game.getBoard().getFields().get(39);
        field1.setOwner(0);
        field1.setOwned(true);
        game.getPlayers().get(game.getCurrentPlayer()).setPossession(37, true);
        field2.setOwner(0);
        field2.setOwned(true);
        game.getPlayers().get(game.getCurrentPlayer()).setPossession(39, true);
        field2.setNumHouses(2);
        field1.setNumHouses(1);
        game.sellHouse(37);
        int actual = game.getBoard().getFields().get(37).getNumHouses();
        int expected = 1;
        assertEquals(expected, actual);
    }

    @Test
    void manage() {
        game.manage();
        AbstractStatus expected = game.getTurnStatus();
        AbstractStatus actual = game.getCurrentStatus();
        assertEquals(expected, actual);
    }

    @Test
    void trade(){
        game.setCurrentStatus(game.getTurnStatus());

        Field field_offer = game.getBoard().getFields().get(39);
        Field field_receive = game.getBoard().getFields().get(34);
        Player p1 = game.getPlayers().get(0);
        Player p2 = game.getPlayers().get(1);

        field_offer.setOwner(0);
        field_offer.setOwned(true);
        field_receive.setOwner(1);
        field_receive.setOwned(true);
        p1.setPossession(39, true);
        p2.setPossession(34, true);

        ArrayList<String> offer = new ArrayList<String>();
        offer.add("Ohmplatz");
        ArrayList<String> receive = new ArrayList<String>();
        receive.add("Tayl-Tor");

        game.trade(offer, receive, 0, 200, 1);

        boolean expected = true;
        boolean actual = p2.getPossession(39);

        assertEquals(expected, actual);
    }
}