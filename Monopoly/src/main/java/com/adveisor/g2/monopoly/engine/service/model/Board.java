package com.adveisor.g2.monopoly.engine.service.model;

import java.io.*;
import java.util.ArrayList;

public class Board {
    private ArrayList<Field> fields;

    public Board(String boardfile) {
        try{
            InputStream inputStream = getClass().getResourceAsStream(boardfile);
            BufferedReader fieldset = new BufferedReader(new InputStreamReader(inputStream));
            String input = fieldset.readLine();
            String[] args;
            while(input != null){
                args = input.split(" - ");
                int position = fields.size();
                String name = args[0];
                String type = args[1];
                String color = args[2];
                int price = Integer.parseInt(args[3]);
                int housecost = Integer.parseInt(args[4]);
                String[] stages = args[5].split(":");
                int[] rent_stages = new int[stages.length+1];
                rent_stages[0] = 0;
                for(int i = 1; i<stages.length+1; i++){
                    rent_stages[i] = Integer.parseInt(stages[i]);
                }
                fields.add(new Field(name, type, color, position, price, housecost, rent_stages));
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

    public ArrayList<Field> getFields(){
        return this.fields;
    }

}
