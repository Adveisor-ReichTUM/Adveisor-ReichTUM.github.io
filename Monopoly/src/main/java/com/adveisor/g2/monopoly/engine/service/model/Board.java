package com.adveisor.g2.monopoly.engine.service.model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Board {
    private List<Field> fields = new ArrayList<>();

    public Board(String boardFile) {
        try{
            BufferedReader fieldset = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(boardFile)));
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
        }
        catch(java.io.IOException exception){
            System.err.println(exception);
        }
    }

    public int countType(Field field, Player player){
        int counter = 0;
        for(Field f: fields){
            if((f.getType()==field.getType()) && (f.getOwner()==player.getId()))
                counter++;
        }
        return counter;
    }

    public List<Field> getFields(){
        return this.fields;
    }

}
