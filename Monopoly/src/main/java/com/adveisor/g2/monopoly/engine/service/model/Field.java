package com.adveisor.g2.monopoly.engine.service.model;

import lombok.Data;

import java.util.List;

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
            default: break;
        }
    }
    public void evaluateStreet(Player player, Game game){
        List<Player> players = game.getPlayers();
        if((this.owner != player.getId()) && this.owned){
            // Spieler nicht Besitzer des gekauften Feldes
            if(this.owner>=0 && this.owner < players.size()) {
                payRent(player, players.get(this.owner), game.getBoard());       // Miete bezahlen
            }
            else
                System.err.println("Fehler: Besitzer nicht identifizierbar");
        }
        else if(!this.owned){
            game.setStatus(Status.BUY_PROPERTY);
        }
    }

    public static void transaction(Player paying_pl, Player paid_pl, int diff){
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
                if(stage==1 && paid_pl.ownsAllOfColor(this.getColor())) diff*=2;
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
        return this.getPrice()/2;
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
