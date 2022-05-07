package com.adveisor.g2.monopoly.engine.service.model;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.FileReader;
import java.io.BufferedReader;
public class Game {

    private enum Status {START, WAITING, DICE, CARD, PROPERTY, TURN, AUCTION, JAIL, END}
    private final Status status;
    private int numPlayers;
    private int numActivePlayers;
    private int numBankruptPlayers;
    private int numRounds;
    private boolean running;

    // reference attributes
    private ArrayList<Player> players;
    //private ArrayList<Field> fields;
    private Deck communityDeck;
    private Deck chanceDeck;
    private Board board;

    // constructor
    public Game(String boardfile, String chancefile, String communityfile){
        // set up board
        this.board = new Board(boardfile);

        // set up fields
        /*fields = new ArrayList<Field>;
        Field.setup(fields, fieldsFile);*/

        // set up chance Deck
        this.chanceDeck = new Deck(true, chancefile);

        // set up community Deck
        this.communityDeck = new Deck(false, communityfile);

        // Load players
        /*try{
            BufferedReader input = new BufferedReader(new FileReader(playerFile));
            numPlayers = Integer.parseInt(input.readLine())/2;
            for(int i = 0; i<numPlayers; i++){
                players.add(new Player(input.readLine(), this, Piece.valueOf(input.readLine())));
            }
            input.close();
        }
        catch(java.io.IOException exception){
            System.err.println(exception);
        }*/

        this.status = Status.WAITING;

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

    public void join(String name, Piece piece){
        // check for failure
        if(status != status.WAITING) throw new IllegalStateException("Failed to join Game: already running.");

        for(Player player : players){
            if(player.getName().equals(name)) throw new IllegalArgumentException("This name already exists.");
            if(player.getPiece().equals(piece)) throw new IllegalArgumentException("This color is already used by another player.");
        }

        // create player
        Player player = new Player(name, this, piece);
        players.add(player);
    }
}
