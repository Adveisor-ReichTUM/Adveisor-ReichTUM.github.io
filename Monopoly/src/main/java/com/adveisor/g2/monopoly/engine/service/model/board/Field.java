/*
 * Copyright (c) ReichTUM 2022.
 */

package com.adveisor.g2.monopoly.engine.service.model.board;

import com.adveisor.g2.monopoly.engine.service.GameService;
import com.adveisor.g2.monopoly.engine.service.model.player.Player;
import lombok.Data;

import java.util.Objects;

@Data
public class Field {
    
    private String name;
    private int position;

    private boolean owned;
    private String ownerId;

    private int numHouses;
    private final int price;

    private final int houseCost;
    private int[] rent_stages;      // Hypothek; Normal = 1; (Alle Farben = 2); ++houses;
    private boolean Hypothek;

    private final Color color;

    private final FieldType type;

    // constructor
    public Field(String name, String type, String color, int position, int price, int houseCost, int[] rent_stages){
        this.name = name;
        this.position = position;
        this.type = FieldType.valueOf(type);
        this.price = price;
        this.houseCost = houseCost;
        this.rent_stages = rent_stages;
        this.color = Color.valueOf(color);

        this.owned = false;
        this.numHouses = 0;
        this.Hypothek = false;
    }

    public static void evaluateField(Field field, Player player, GameService gameService){
        switch(field.getType()){
            case station:
            case utilities:
            case street:
                field.evaluateStreet(player, gameService);
                return;
            case tax: player.adjustBalance(-field.getPrice()); break;
        }
    }

    public void evaluateStreet(Player player, GameService gameService){
        if((this.owned && !Objects.equals(this.ownerId, player.getPlayerId()))){
            // Spieler nicht Besitzer des gekauften Feldes
//                payRent(player, this.ownerId, gameService.getBoard());       // Miete bezahlen
        } else if(!this.owned){
//            gameService.setCurrentStatus(gameService.getBuyPropertyStatus());
        }
    }


    public void payRent(Player paying_pl, Player paid_pl, Board board){
        if(this.Hypothek) return;

        int stage = 0;
        int diff = 0;
        switch(this.type) {
            case street:
                stage = determineStreetStage();
                diff = this.rent_stages[stage];
                if(stage==1 && paid_pl.monopolyOverColor(this.getColor())) diff*=2;
                break;
            case station:
                stage = determineStationStage(paid_pl, board);
                diff = this.rent_stages[stage];
                break;
            case utilities:
                stage = determineUtilityStage(paid_pl, board);
//                diff = this.rent_stages[stage] * paying_pl.getDiceResult();
                break;
        }
        GameService.transaction(paying_pl, paid_pl, diff);
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

    public int getMortgageValue(){
        return this.getPrice()/2;
    }

    public void reset(){
        this.owned = false;
        this.ownerId = null;
        this.numHouses = 0;
        this.Hypothek = false;
    }

    public int getRentStage(int stage){
        return this.rent_stages[stage];
    }
}
