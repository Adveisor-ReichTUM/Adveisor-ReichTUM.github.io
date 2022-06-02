/*
 * Copyright (c) ReichTUM 2022.
 */

package com.adveisor.g2.monopoly.engine.service.model.board;

import com.adveisor.g2.monopoly.engine.service.model.player.Player;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Board {
    private final List<Field> fields = new ArrayList<>();

    public Board(String boardFile) {
        try {
            BufferedReader fieldset = new BufferedReader(new InputStreamReader(Objects.requireNonNull(this.getClass().getResourceAsStream(boardFile))));
            String input = fieldset.readLine();
            String[] args;
            while(input != null){
                args = input.split(" - "); // name - type - color - price - houseCost - stage1:stage2
                int position = fields.size();
                String name = args[0];
                String type = args[1];
                String color = args[2];
                int price = Integer.parseInt(args[3]);
                int houseCost = Integer.parseInt(args[4]);
                String[] stages = args[5].split(":");
                int[] rent_stages = new int[stages.length+1];
                rent_stages[0] = 0;
                for(int i = 1; i<stages.length; i++){
                    rent_stages[i] = Integer.parseInt(stages[i]);
                }
                fields.add(new Field(name, type, color, position, price, houseCost, rent_stages));
                input = fieldset.readLine();
            }
            fieldset.close();
        } catch (NullPointerException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Game file not found");
        } catch (java.io.IOException exception){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error initializing game file");
        }
    }

    public int countType(Field field, Player player) {
        int counter = 0;
        for(Field f: fields){
            if((f.getType()==field.getType()) && (Objects.equals(f.getOwnerId(), player.getPlayerId())))
                counter++;
        }
        return counter;
    }

    // tell if the player has monopoly over the color field
    public boolean isMonopoly(Color color, Player player) {
        for (Field field : fields) {
            if (field.getColor() == color) {
                if (!Objects.equals(player.getPlayerId(), field.getOwnerId())) {
                    return false;
                }
            }
        }
        return true;
    }

    public List<Field> getFields(){
        return this.fields;
    }

    public Field getField(int index) {
        return this.fields.get(index);
    }

}
