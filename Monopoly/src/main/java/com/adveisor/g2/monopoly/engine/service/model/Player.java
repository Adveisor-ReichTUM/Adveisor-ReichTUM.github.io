package com.adveisor.g2.monopoly.engine.service.model;

import java.util.Scanner;

import java.lang.Math;

public class Player {
    private static int id = 0;             // identification number for player
    //private static int counter;     // number of players

    private final int startmoney;   // amount of money available in the beginning

    private int position;       // position on the field array: 0 - 39
    private int dice;           // total value of dices
    private boolean pasch;      // dices with same value

    private int numPasch;
    private int bid;

    private final Piece piece;
    private int balance;        // amount of money the player owns
    private boolean bankrupt;   // criteria defining if player is still in the game
    private String name;        // name of player
    private boolean[] streets;  // fields owned by player: true if in possession
    private int numHouses;         // number of houses owned, required for renovation cost calculation
    private int numHotels;         // number of hotels owned, required for renovation cost calculation

    private int roundsInJail;   // number of consecutive rounds the player has spent in jail
    private boolean inJail;     // jailed status
    private int numJailCards;   // number of Out-of-jail cards in players possession

    // reference attribute
    private Game game;          // reference object to interact with game

    // constructor
    public Player(String name, Game game, Piece piece){
        this.startmoney = 1500;
        this.balance = startmoney;
        this.name = name;
        this.bankrupt = false;
        this.position = 0;
        this.game = game;
        this.streets = new boolean[40];    // initializes the elements with false
        this.bid = -1;
        this.piece = piece;
        this.id = id+1;
        this.numPasch = 0;
    }

    public int getBalance(){
        return balance;
    }

    public boolean isBankrupt(){
        return bankrupt;
    }

    public boolean isInJail(){
        return inJail;
    }

    public int getNumJailCards(){
        return numJailCards;
    }

    public void setBalance(int balance_new){
        this.balance = balance_new;
    }

    public void setNumJailCards(int numJailCards_new){
        this.numJailCards = numJailCards_new;
    }

    public void setBankrupt(boolean isBankrupt_new){
        this.bankrupt = isBankrupt_new;
    }

    public void setInJail(boolean isInJail_new){
        this.inJail = isInJail_new;
    }

    public void jail(){
        setInJail(true);
        setRoundsInJail(0);
        setPosition(10);
        this.setNumPasch(0);
    }
    public void adjustBalance(int diff){
        this.balance += diff;
    }

    public int getPosition(){
        return this.position;
    }

    public void setPosition(int position_new){
        this.position = position_new;
    }

    public void throwDices(){
        int[] dice_values = Dice.throwDices();
        this.dice = Dice.getTotal(dice_values);
        this.pasch = Dice.isPasch(dice_values);
    }

    public int getDiceResult(){
        return this.dice;
    }

    public void move(){
        setPosition((getPosition()+dice)%40);
        if(this.position<dice){
            adjustBalance(200);
        }
    }

    public boolean getPossession(int field_num){
        return this.streets[field_num];
    }

    public void setPossession(int field_num, boolean ownership){
        this.streets[field_num] = ownership;
    }

    public int getNumHouses(){
        return this.numHouses;
    }

    public int getNumHotels(){
        return this.numHotels;
    }

    public int getRoundsInJail(){
        return this.roundsInJail;
    }

    public void setRoundsInJail(int roundsInJail){
        this.roundsInJail = roundsInJail;
    }

    public int getId(){
        return this.id;
    }

    public void buy(Field field){
        adjustBalance(-field.getPrice());
        streets[field.getPosition()] = true;
        field.setOwned(true);
        field.setOwner(id);
    }

    public void toJail(){
        setInJail(true);
        setPosition(10);
        setNumPasch(0);
    }

    public int getBid(){
        return this.bid;
    }

    public void setBid(int bid){
        this.bid = bid;
    }
    /*public void turn(){
        int counter = 0;
        if(inJail){
            System.out.println("Willst du 50€ zahlen oder eine Aus-dem-Gefängnis Karte nutzen, um aus den Gefängnis zu kommen? y/n");
            Scanner input = new Scanner(System.in);
            char decision = Character.toLowerCase(input.next().charAt(0));
            switch(decision){
                case 'y':
                    if(this.numJailCards>0)
                        this.numJailCards--;
                    else
                        adjustBalance(-50);
                    moveAndEvaluate(game.getBoard());
            }
        }
    }*/

    public void moveAndEvaluate(Board board){
        /*int counter = 0;
        this.pasch = true;
        while(this.pasch && (this.inJail == false) && counter <=2)
            if(counter == 2 && this.pasch) toJail();
            throwDices();
            move();*/
        move();
        Field field = board.getFields().get(this.position);
        Field.evaluateField(field, this, game);
        //buildTradeMortgage();
    }

    public void buildTradeMortgage(){
        System.out.println("Available actions: B (Bauen), T (Tausschen), H (Hypothek), F (Finish)");
        Scanner input = new Scanner(System.in);
        char selection = Character.toLowerCase(input.next().charAt(0));
    }

    public String getName(){
        return this.name;
    }

    public Piece getPiece(){
        return this.piece;
    }

    public int calculateWealth(){
        int wealth = getBalance();
        wealth += getNumJailCards()*50;
        for(int i = 0; i<streets.length; i++){
            if(streets[i]){
                Field field = game.getBoard().getFields().get(i);
                wealth += field.getPrice();
                wealth += field.getNumHouses() * field.getHouseCost();
            }
        }
        return wealth;
    }

    public boolean getPasch(){
        return this.pasch;
    }

    public int getNumPasch(){
        return this.numPasch;
    }

    public void setNumPasch(int numPasch){
        this.numPasch = numPasch;
    }

    public void setPasch(boolean pasch){
        this.pasch = pasch;
    }

    /*public void sellPropertyToBank(int fieldIndex){

    }*/

    public void endMortgage(int fieldIndex){
        if(game.getStatus() != Status.TURN) throw new IllegalStateException("Cannot end mortgage while not being in TURN.");

        Field field = game.getBoard().getFields().get(fieldIndex);

        if(field.getIsHypothek() && getPossession(fieldIndex)){
            int diff = (int) (field.getMortgageCost() + field.getMortgageCost()*0.1);    //10% aufschlag beim Zurückzahlen
            adjustBalance(-diff);
            field.setIsHypothek(false);
        }
    }

    public boolean checkPossession(int fieldIndex){
        return this.streets[fieldIndex];
    }

}
