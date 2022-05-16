package com.adveisor.g2.monopoly.engine.service.model;

import java.util.ArrayList;
import java.util.Scanner;

public class Field {
    private String name;
    private int position;

    private boolean owned;
    private int owner;

    private int numHouses;
    private final int price;

    private final int housecost;
    private int[] rent_stages;      // Hypothek; Normal = 1; (Alle Farben = 2); ++houses;
    private boolean isHypothek;

    //private enum colorType {no_color, braun, hellblau, pink, orange, rot, gelb, gruen, dunkelblau};
    private final Color color;

    private enum fieldType {los, street, station, jail, police, parking, tax, chance, community, utilities};
    private final fieldType type;

    // constructor
    public Field(String name, String type, String color, int position, int price, int housecost, int[] rent_stages){
        this.name = name;
        this.position = position;
        this.type = fieldType.valueOf(type);
        this.price = price;
        this.housecost = housecost;
        this.rent_stages = rent_stages;
        this.color = Color.valueOf(color);

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

    public static void evaluateField(Field field, Player player, Game game){
        switch(field.getType()){
            case los: break;
            case station:
            case street: field.evaluateStreet(player, game); break;
            case jail: break;
            case police: field.evaluatePolice(player, game); break;
            case parking: break;
            case tax: player.adjustBalance(-field.getPrice()); break;
            case chance: game.getChanceDeck().takeCard(player, game); break;
            case community: game.getCommunityDeck().takeCard(player, game); break;
            case utilities: field.evaluateUtilities(player, game);
            default: break;
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
            game.setStatus(Status.BUY_PROPERTY);
        }
    }


    public void decideBuy(Player player){
        if(player.getBalance()<this.price) {
            System.out.print("You cannot afford to buy this street. ");
            System.out.print("You may be able to increase your balance by trading, selling houses, mortgage.\n");
        }
        else {
            System.out.println("Would you like to buy this street? y/n");
            char decision = 'n';
            Scanner input = new Scanner(System.in);
            decision = Character.toLowerCase(input.next().charAt(0));

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
            input.close();
        }
    }

    public void transaction(Player paying_pl, Player paid_pl, int diff){
        paying_pl.adjustBalance(-diff);
        paid_pl.adjustBalance(diff);
    }

    public void payRent(Player paying_pl, Player paid_pl, Board board){
        if(this.isHypothek) return;

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
                stage = determineUtilityStage(paid_pl, board);
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

    public int determineUtilityStage(Player paid_pl, Board board){
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

    public void evaluatePolice(Player player, Game game){
        player.jail();
        game.rollAndMove();
    }

    public fieldType getType(){
        return this.type;
    }

    public Color getColor() {
        return this.color;
    }
    public int getOwner(){
        return this.owner;
    }

    public int getHouseCost(){
        return this.housecost;
    }

    public int getNumHouses(){
        return this.numHouses;
    }

    public boolean getIsHypothek(){
        return this.isHypothek;
    }

    public int getMortgageValue(){
        return this.rent_stages[0];
    }

    public void setIsHypothek(boolean isHypothek){
        this.isHypothek = isHypothek;
    }

    public void reset(){
        this.owned = false;
        this.owner = -1;
        this.numHouses = 0;
        this.isHypothek = false;
    }

    public void setNumHouses(int numHouses){
        this.numHouses = numHouses;
    }

}
