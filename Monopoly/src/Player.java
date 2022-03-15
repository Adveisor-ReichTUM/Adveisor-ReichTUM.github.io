import java.util.*;

public class Player {
    private final int startmoney;   // amount of money available in the beginning

    private int position;       // position on the field array: 0 - 39
    private int dice;           // total value of dices
    private boolean pasch;      // dices with same value

    private int balance;        // amount of money the player owns
    private boolean bankrupt;   // criteria defining if player is still in the game
    private String name;        // name of player
    private boolean[] streets;  // fields owned by player: true if in possession

    private int roundsInJail;   // number of consecutive rounds the player has spent in jail
    private boolean inJail;     // jailed status
    private int numJailCards;   // number of Out-of-jail cards in players possession

    private Game game;          // reference object to interact with game

    public Player(String name, int startmoney, Game game){
        this.startmoney = startmoney;
        this.balance = startmoney;
        this.name = name;
        this.bankrupt = false;
        this.position = 0;
        this.game = game;
        this.streets = new boolean[40];    // initializes the elements with false

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

    public void adjustBalance(int diff){
        this.balance += diff;
    }

    public int getPosition(){
        return this.position;
    }

    public void setPosition(int position_new){
        this.position = position_new;
    }

    public boolean throwDices(){
        int[] dice_values = Dice.throwDices();
        this.dice = dice_values[0] + dice_values[1];
        this.pasch = (dice_values[0] == dice_values[1]);

        return this.pasch;
    }

    public void move(){
        setPosition((getPosition()+dice)%40);
        if(this.position<dice){
            adjustBalance(200);
        }
    }

}
