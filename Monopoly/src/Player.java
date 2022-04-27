import java.util.Scanner;

public class Player {
    private int id;             // identification number for player
    //private static int counter;     // number of players

    private final int startmoney;   // amount of money available in the beginning

    private int position;       // position on the field array: 0 - 39
    private int dice;           // total value of dices
    private boolean pasch;      // dices with same value

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
    public Player(String name, Game game){
        this.startmoney = 1500;
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
        adjustBalance(field.getPrice());
        streets[field.getPosition()] = true;
        field.setOwned(true);
        field.setOwner(id);
    }

    public void toJail(){
        setInJail(true);
        setPosition(10);
    }

    public void turn(){
        int counter = 0;
        if(inJail){
            System.out.println("Willst du 50€ zahlen oder eine Aus-dem-Gefängnis Karte nutzen, um aus den Gefängnis zu kommen? y/n");
            Scanner input = new Scanner(System.in);
            char decision = java.lang.Character.toLowerCase(input.next().charAt(0));
            switch(decision){
                case 'y':
                    if(this.numJailCards>0)
                        this.numJailCards--;
                    else
                        adjustBalance(-50);
                    moveAndEvaluate();
            }
        }
    }

    public void moveAndEvaluate(Board board){
        int counter = 0;
        this.pasch = true;
        while(this.pasch && (this.inJail == false) && counter <=2)
            if(counter == 2 && this.pasch) toJail();
            throwDices();
            move();
            Field field = board.getFields().get(this.position);
            Field.evaluateField(field, this, game);
        buildTradeMortgage();
    }

    public void buildTradeMortgage(){
        System.out.println("Available actions: B (Bauen), T (Tausschen), H (Hypothek), F (Finish)");
        Scanner input = new Scanner(System.in);
        char selection = java.lang.Character.toLowerCase(input.next().charAt(0));

    }

}
