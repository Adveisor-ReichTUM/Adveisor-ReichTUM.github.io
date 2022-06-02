package com.adveisor.g2.monopoly.engine.service.model;

import com.adveisor.g2.monopoly.engine.service.GameService;
import com.adveisor.g2.monopoly.engine.service.model.board.Field;
import com.adveisor.g2.monopoly.engine.service.model.player.Player;
import com.adveisor.g2.monopoly.engine.service.status.AbstractStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameServiceTest {
    GameService gameService;
    @BeforeEach
    void setUp() {
        gameService = new GameService("/text/board.txt", "/text/chanceDeck.txt", "/text/CommunityDeck.txt");
        gameService.join(new Player());
        gameService.join(new Player());
    }

    @Test
    void join() {
        int expected = 2;
        int actual = gameService.getPlayers().size();
        assertEquals(expected, actual);
    }

    @Test()
    void joinException(){
        try{
            gameService.join(new Player());
        } catch(IllegalArgumentException e){
        }
    }

//    @Test
//    void start() {
//        gameService.start();
//        AbstractStatus expected = gameService.getStartStatus();
//        AbstractStatus actual = gameService.getCurrentStatus();
//        assertEquals(expected, actual);
//    }

    @Test
    void startException() {
        try{
            gameService.start();
        } catch(IllegalStateException e){

        }
    }

//    @Test
//    void end() {
//        Player player1 = gameService.getPlayers().get(0);
//        Player player2 = gameService.getPlayers().get(1);
//        player1.setBalance(1700);
//        player2.setBalance(1400);
//        player2.setPossession(39, true);
//        gameService.getBoard().getFields().get(39).setNumHouses(4);
//        String expected = player2.getName();
//        String actual = gameService.end();
//        assertEquals(expected, actual);
//    }

//    @Test
//    void turn1() {
//        gameService.turn1();
//    }

//    @Test
//    void turn2() {
//        gameService.diceThrow();
//    }

//    @Test
//    void decideJail() {
//        gameService.setCurrentPlayer(1);
//        gameService.getPlayers().get(1).setBalance(1500);
//        gameService.decideJail(true);
//        int expected = 1450;
//        int actual = gameService.getPlayers().get(1).getBalance();
//        assertEquals(expected, actual);
//    }

//    @Test
//    void decideJail2() {
//        //game.setCurrentPlayer(0);
//        gameService.getPlayers().get(0).setInJail(true);
//        gameService.decideJail(true);
//        boolean expected = false;
//        boolean actual = gameService.getPlayers().get(0).isInJail();
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    void useJailCard() {
//        Player p = gameService.getPlayers().get(0);
//        p.setNumJailCards(2);
//        p.setInJail(true);
//        gameService.useJailCard();
//        boolean expected = false;
//        boolean actual = gameService.getPlayers().get(0).isInJail();
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    void buy() {
//        gameService.setCurrentPlayer(0);
//        Player p = gameService.getPlayers().get(0);
//        p.setPosition(15);
//        gameService.buy();
//        boolean expected = true;
//        boolean actual = gameService.getBoard().getFields().get(15).getOwner() == p.getId();
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    void sellBank() {
//        gameService.setCurrentStatus(gameService.getTurnStatus());
//        Player p = gameService.getPlayers().get(gameService.getCurrentPlayerId());
//        Field field = gameService.getBoard().getFields().get(15);
//        p.setPossession(15, true);
//        gameService.sellBank(15);
//        boolean actual = p.getPossession(15);
//        boolean expected = false;
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    void sellBank2() {
//        gameService.setCurrentStatus(gameService.getTurnStatus());
//        Player p = gameService.getPlayers().get(gameService.getCurrentPlayerId());
//        Field field = gameService.getBoard().getFields().get(15);
//        p.setPossession(15, true);
//        gameService.sellBank(15);
//        int actual = p.getBalance();
//        int expected = 1600;
//        assertEquals(expected, actual);
//    }

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
//    @Test
//    void setBid() {
//        Player p1 = gameService.getPlayers().get(0);
//        Player p2 = gameService.getPlayers().get(1);
//
//        gameService.setBid("Mr. Monopoly", 400);
//        gameService.setBid("Mr. Monopoly 2", 420);
//
//        int expected = 1;
//        int actual = gameService.getHighestBidderIndex();
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    void startMortgageException() {
//        gameService.setCurrentStatus(gameService.getBuyPropertyStatus());
//        try{
//            gameService.startMortgage(39);
//        } catch(IllegalStateException e){
//        }
//    }
//    @Test
//    void endMortgageException() {
//        gameService.setCurrentStatus(gameService.getBuyPropertyStatus());
//        try{
//            gameService.startMortgage(39);
//        } catch(IllegalStateException e){
//        }
//    }
//
//    @Test
//    void buyHouse1() {
//        gameService.setCurrentStatus(gameService.getTurnStatus());
//        Field field = gameService.getBoard().getFields().get(39);
//        field.setOwner(0);
//        field.setOwned(true);
//        gameService.getPlayers().get(0).setPossession(39, true);
//        gameService.buyHouse(39);
//        int actual = gameService.getBoard().getFields().get(39).getNumHouses();
//        int expected = 0;
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    void buyHouse2() {
//        gameService.setCurrentStatus(gameService.getTurnStatus());
//        Field field1 = gameService.getBoard().getFields().get(37);
//        Field field2 = gameService.getBoard().getFields().get(39);
//        field1.setOwner(0);
//        field1.setOwned(true);
//        gameService.getPlayers().get(gameService.getCurrentPlayerId()).setPossession(37, true);
//        field2.setOwner(0);
//        field2.setOwned(true);
//        gameService.getPlayers().get(gameService.getCurrentPlayerId()).setPossession(39, true);
//        gameService.buyHouse(39);
//        int actual = gameService.getBoard().getFields().get(39).getNumHouses();
//        int expected = 1;
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    void buyHouse3() {
//        gameService.setCurrentStatus(gameService.getTurnStatus());
//        Field field1 = gameService.getBoard().getFields().get(37);
//        Field field2 = gameService.getBoard().getFields().get(39);
//        field1.setOwner(0);
//        field1.setOwned(true);
//        gameService.getPlayers().get(gameService.getCurrentPlayerId()).setPossession(37, true);
//        field2.setOwner(0);
//        field2.setOwned(true);
//        gameService.getPlayers().get(gameService.getCurrentPlayerId()).setPossession(39, true);
//        field2.setNumHouses(1);
//        gameService.buyHouse(39);
//        int actual = gameService.getBoard().getFields().get(39).getNumHouses();
//        int expected = 1;
//        assertEquals(expected, actual);
//    }
//    @Test
//    void sellHouse1() {
//        gameService.setCurrentStatus(gameService.getTurnStatus());
//        Field field = gameService.getBoard().getFields().get(39);
//        field.setOwner(0);
//        field.setOwned(true);
//        gameService.getPlayers().get(0).setPossession(39, true);
//        field.setNumHouses(1);
//        gameService.sellHouse(39);
//        int expected = 1600;
//        int actual = gameService.getPlayers().get(gameService.getCurrentPlayerId()).getBalance();
//    }
//
//    @Test
//    void sellHouse2() {
//        gameService.setCurrentStatus(gameService.getTurnStatus());
//        Field field1 = gameService.getBoard().getFields().get(37);
//        Field field2 = gameService.getBoard().getFields().get(39);
//        field1.setOwner(0);
//        field1.setOwned(true);
//        gameService.getPlayers().get(gameService.getCurrentPlayerId()).setPossession(37, true);
//        field2.setOwner(0);
//        field2.setOwned(true);
//        gameService.getPlayers().get(gameService.getCurrentPlayerId()).setPossession(39, true);
//        field2.setNumHouses(2);
//        field1.setNumHouses(1);
//        gameService.sellHouse(37);
//        int actual = gameService.getBoard().getFields().get(37).getNumHouses();
//        int expected = 1;
//        assertEquals(expected, actual);
//    }

    @Test
    void manage() {
        gameService.manage();
        AbstractStatus expected = gameService.getTurnStatus();
        AbstractStatus actual = gameService.getCurrentStatus();
        assertEquals(expected, actual);
    }
}