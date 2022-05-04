package com.adveisor.g2.monopoly.engine.service.model;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.FileReader;
import java.io.BufferedReader;
public class Game {

    private enum status {START, WAITING, DICE, CARD, PROPERTY, TURN, AUCTION, JAIL, END}
    private int numPlayers;
    private int numActivePlayers;
    private int numRounds;
    private boolean running;

    // reference attributes
    private ArrayList<Player> players;
    //private ArrayList<Field> fields;
    private Deck communityDeck;
    private Deck chanceDeck;
    private Board board;

    // constructor
    public Game(String boardfile, String chancefile, String communityfile, String playerFile){
        // set up board
        board = new Board(boardfile);

        // set up fields
        /*fields = new ArrayList<Field>;
        Field.setup(fields, fieldsFile);*/

        // set up chance Deck
        chanceDeck = new Deck(true, chancefile);

        // set up community Deck
        communityDeck = new Deck(false, communityfile);

        // Load players
        try{
            BufferedReader input = new BufferedReader(new FileReader(playerFile));
            numPlayers = Integer.parseInt(input.readLine());
            for(int i = 0; i<numPlayers; i++){
                players.add(new Player(input.readLine(),this));
            }
            input.close();
        }
        catch(java.io.IOException exception){
            System.err.println(exception);
        }

    }

    public int getNumPlayers(){
        return this.numPlayers;
    }

    public int getNumRounds(){
        return this.numRounds;
    }

    public int getNumActivePlayers() {
        return this.numActivePlayers;
    }

    public ArrayList<Player> getPlayers(){
        return this.players;
    }

    public Board getBoard(){
        return this.board;
    }

    public Deck getCommunityDeck(){
        return this.communityDeck;
    }

    public Deck getChanceDeck(){
        return this.chanceDeck;
    }

}
