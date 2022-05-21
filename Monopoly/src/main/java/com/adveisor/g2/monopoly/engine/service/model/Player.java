package com.adveisor.g2.monopoly.engine.service.model;

import lombok.Data;

import java.util.Scanner;

@Data
public class Player {

    public static int nextId = 0;             // identification number for player
    //private static int counter;     // number of players
    private int id;
    private final int startMoney;   // amount of money available in the beginning


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
        this.startMoney = 1500;
        this.balance = startMoney;
        this.name = name;
        this.bankrupt = false;
        this.position = 0;
        this.game = game;
        this.streets = new boolean[40];    // initializes the elements with false
        this.bid = 0;
        this.piece = piece;

        this.id = ++nextId;

        this.numPasch = 0;
    }


    public void jail(){
        setInJail(true);
        setRoundsInJail(0);
        setPosition(10);
        this.setNumPasch(0);
        this.setPasch(false);
    }
    public void adjustBalance(int diff){
        this.balance += diff;
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


    public void endMortgage(int fieldIndex){
        if(game.getStatus() != Status.TURN) throw new IllegalStateException("Cannot end mortgage while not being in TURN.");

        Field field = game.getBoard().getFields().get(fieldIndex);

        if(field.isHypothek() && getPossession(fieldIndex)){
            int diff = (int) (field.getMortgageValue() + field.getMortgageValue()*0.1);    //10% aufschlag beim Zurückzahlen
            adjustBalance(-diff);
            field.setHypothek(false);
        }
    }

    public void startMortgage(int fieldIndex){
        if(game.getStatus()!=Status.TURN) throw new IllegalStateException("Can not start Mortgage while not being in TURN");

        Field field = game.getBoard().getFields().get(fieldIndex);

        if(field.getNumHouses()>0) return;

        if(getPossession(fieldIndex) && !field.isHypothek()){
            adjustBalance(field.getMortgageValue());
            field.setHypothek(true);
        }
    }

    public boolean checkPossession(int fieldIndex){
        return this.streets[fieldIndex];
    }


    public void setNumHouses(int numHouses){
        this.numHouses = numHouses;
    }

    public void setNumHotels(int numHotels){
        this.numHotels = numHotels;
    }

    public boolean ownsAllOfColor(Color color){
        for(int i = 0; i<40; i++){
            Field running = game.getBoard().getFields().get(i);
            if(running.getColor()==color){
                if(!getPossession(i)) return false;
            }
        }
        return true;
    }
}
