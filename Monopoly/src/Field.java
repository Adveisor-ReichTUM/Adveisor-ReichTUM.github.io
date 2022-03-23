import java.util.ArrayList;
import java.util.Scanner;

public class Field {
    private String name;
    private int position;

    private boolean owned;
    private int owner;

    private int numHouses;
    private final int price;
    private int[] rent_stages;      // Hypothek; Normal = 1; (Alle Farben = 2); ++houses;
    private boolean isHypothek;

    private enum colorType {no_color, braun, hellblau, pink, orange, rot, gelb, gruen, dunkelblau};
    private final colorType color;

    private enum fieldType {los, street, station, jail, police, parking, tax, chance, community, utilities};
    private final fieldType type;

    // constructor
    public Field(String name, String type, String color, int position, int price, int[] rent_stages){
        this.name = name;
        this.position = position;
        this.owned = false;
        this.type = fieldType.valueOf(type);
        this.price = price;
        this.rent_stages = rent_stages;
        this.color = colorType.valueOf(color);

        this.owned = false;
        this.owner = -1;
        this.numHouses = 0;
        this.isHypothek = false;
    }

    public String getName(){
        return this.name;
    }

    public int getPosition(){
        return this.position;
    }

    public boolean isOwned(){
        return owned;
    }

    public void setOwned(boolean owned){
        this.owned = owned;
    }

    public void evaluateField(Player player, Game game, Player players){
        switch(type){
            case los: break;
            case station:
            case street: evaluateStreet(player, game); break;
            case jail: break;
            case police: evaluatePolice(player); break;
            case parking: break;
            case tax: player.adjustBalance(this.price); break;
            case chance: game.getChanceDeck().takeCard(player, game); break;
            case community: game.getCommunityDeck().takeCard(player, game); break;
            case utilities: evaluateUtilities(player, game);
        }
    }

    public void evaluateUtilities(Player player, Game game){

    }

    public void evaluateStreet(Player player, Game game){
        ArrayList<Player> players = game.getPlayers();
        if(this.owner != player.getId() && this.owned){
            // Spieler nicht Besitzer des gekauften Feldes
            if(this.owner>=0 && this.owner < players.size())
                    payRent(player, players.get(this.owner), game.getBoard());       // Miete bezahlen
            else
                System.err.println("Fehler: Besitzer nicht identifizierbar");
        }
        else if(this.owned == false){
            decideBuy(player);
        }
    }


    public void decideBuy(Player player){
        if(player.getBalance()<this.price)
            System.out.print("You cannot afford to buy this street. ");
            System.out.print("You may be able to increase your balance by trading, selling houses, mortgage.\n");
        else{
            System.out.println("Would you like to buy this street? y/n");
            char decision = 'n';
            Scanner input = new Scanner(System.in);
            decision = java.lang.Character.toLowerCase(input.next().charAt(0));

            boolean valid = false;
            while(!valid)
                switch(decision){
                    case 'y':
                        valid = true;
                        player.buy(this); break;
                    case 'n':
                        valid = true;
                        break;
                    default:
                        System.out.println("Falsche Eingabe! Would you like to buy this street? y/n");
                        break;
                }
        }
    }

    public void transaction(Player paying_pl, Player paid_pl, int diff){
        paying_pl.adjustBalance(diff);
        paid_pl.adjustBalance(diff);
    }

    public void payRent(Player paying_pl, Player paid_pl, Board board){
        int stage = 0;
        int diff = 0;
        switch(this.type) {
            case street:
                stage = determineStreetStage();
                diff = this.rent_stages[stage];
                break;
            case station:
                stage = determineStationStage(paid_pl, board);
                diff = this.rent_stages[stage];
                break;
            case utilities:
                stage = determineUtilityStage();
                diff = this.rent_stages[stage] * paying_pl.getDiceResult();
                break;
        }
        transaction(paying_pl, paid_pl, diff);
    }

    public int determineStationStage(Player paid_pl, Board board){
        if(this.isHypothek)
            return 0;
        else
            return board.countType(this, paid_pl);
    }

    public int determineUtilityStage(Player paid_pl, Board board, Dice dice){
        if(this.isHypothek)
            return 0;
        else
            return board.countType(this, paid_pl);
    }

    public int determineStreetStage(){
        if(this.isHypothek)
            return 0;
        else
            return (1 + this.numHouses);
    }

    public int getPrice(){
        return this.price;
    }

    public void setOwner(int owner_id){
        this.owner = owner_id;
    }

    public void evaluatePolice(Player player){
        player.setInJail(true);
        player.setPosition(10);
    }

    public fieldType getType(){
        return this.type;
    }

    public int getOwner(){
        return this.owner;
    }

}
