public class Player {
    private final int startmoney;

    int position;

    private int balance;
    private boolean bankrupt;
    private String name;

    private int roundsInJail;
    private boolean inJail;
    private int numJailCards;

    //Game game = new Game()

    public Player(String name, int startmoney){
        this.startmoney = startmoney;
        this.balance = startmoney;
        this.name = name;
        this.position = 0;
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

}
