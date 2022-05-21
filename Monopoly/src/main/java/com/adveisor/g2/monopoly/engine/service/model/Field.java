package com.adveisor.g2.monopoly.engine.service.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.Scanner;

@Data
public class Field {
    
    private String name;
    private int position;

    private boolean owned;
    private int owner;

    private int numHouses;
    private final int price;

    private final int houseCost;
    private int[] rent_stages;      // Hypothek; Normal = 1; (Alle Farben = 2); ++houses;
    private boolean Hypothek;

    //private enum colorType {no_color, braun, hellblau, pink, orange, rot, gelb, gruen, dunkelblau};
    private final Color color;

    private enum fieldType {los, street, station, jail, police, parking, tax, chance, community, utilities}
    private final fieldType type;

    // constructor
    public Field(String name, String type, String color, int position, int price, int housecost, int[] rent_stages){
        this.name = name;
        this.position = position;
        this.type = fieldType.valueOf(type);
        this.price = price;
        this.houseCost = housecost;
        this.rent_stages = rent_stages;
        this.color = Color.valueOf(color);

        this.owned = false;
        this.owner = -1;
        this.numHouses = 0;
        this.Hypothek = false;
    }


    public static void evaluateField(Field field, Player player, Game game){
        switch(field.getType()){
            case los: break;
            case station:
            case utilities:
            case street:
                field.evaluateStreet(player,game);
                if(game.getStatus()==Status.BUY_PROPERTY) return;
                break;
            case police: field.evaluatePolice(player, game); return;
            case jail:
            case parking: break;
            case tax: player.adjustBalance(-field.getPrice()); break;
            case chance: game.getChanceDeck().takeCard(player, game); return;
            case community: game.getCommunityDeck().takeCard(player, game); return;
            //case utilities: field.evaluateUtilities(player, game);
            default: break;
        }
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
        else if(!this.owned){
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
        if(this.Hypothek) return;

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
        if(this.Hypothek)
            return 0;
        else
        return board.countType(this, paid_pl);
    }

    public int determineUtilityStage(Player paid_pl, Board board){
        if(this.Hypothek)
            return 0;
        else
        return board.countType(this, paid_pl);
    }

    public int determineStreetStage(){
        if(this.Hypothek)
            return 0;
        else
        return (1 + this.numHouses);
    }


    public void evaluatePolice(Player player, Game game){
        player.jail();
        game.turn1();
    }

    public int getMortgageValue(){
        return this.rent_stages[0];
    }

    public void reset(){
        this.owned = false;
        this.owner = -1;
        this.numHouses = 0;
        this.Hypothek = false;
    }

    public int getRentStage(int stage){
        return this.rent_stages[stage];
    }
}
