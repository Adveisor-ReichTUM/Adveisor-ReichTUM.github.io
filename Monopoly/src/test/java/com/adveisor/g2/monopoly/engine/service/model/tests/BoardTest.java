package com.adveisor.g2.monopoly.engine.service.model.tests;

import com.adveisor.g2.monopoly.engine.service.model.Board;
import com.adveisor.g2.monopoly.engine.service.model.Game;
import com.adveisor.g2.monopoly.engine.service.model.Piece;
import com.adveisor.g2.monopoly.engine.service.model.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    Board board;
    @BeforeEach
    void setUp() throws Exception{
        board = new Board("/text/board.txt");
    }
    @Test
    void generation(){
        assertNotNull(board);
    }
    @Test
    void countType1() {
        Game game = new Game("/text/board.txt", "/text/chanceDeck.txt", "/text/CommunityDeck.txt");
        Player player = new Player("Mr. Monopoly", game, Piece.GREEN);
        player.setId(1);
        board.getFields().get(1).setOwner(1);
        board.getFields().get(3).setOwner(1);
        int expected = 2;
        int actual = board.countType(board.getFields().get(1), player);
        assertEquals(expected, actual);
    }
    @Test
    void countType2() {
        Game game = new Game("/text/board.txt", "/text/chanceDeck.txt", "/text/CommunityDeck.txt");
        Player player = new Player("Mr. Monopoly", game, Piece.GREEN);
        player.setId(1);
        board.getFields().get(5).setOwner(1);
        int expected = 1;
        int actual = board.countType(board.getFields().get(15), player);
        assertEquals(expected, actual);
    }
    @Test
    void getFieldsName() {
        String expected = "Byte Allee";
        String actual = board.getFields().get(6).getName();
        assertEquals(expected, actual);
    }
    @Test
    void getFieldsHouseCost(){
        int expected = 200;
        int actual = board.getFields().get(37).getHouseCost();
        assertEquals(expected, actual);
    }
    @Test
    void getFieldsRentStage(){
        int expected = 200;
        int actual = board.getFields().get(35).getRentStage(4);
        assertEquals(expected, actual);
    }
}