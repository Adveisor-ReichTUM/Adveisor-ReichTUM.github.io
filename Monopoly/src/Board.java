import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;

public class Board {
    private ArrayList<Field> fields;

    public Board(String boardfile){
        try{
            BufferedReader fieldset = new BufferedReader(new FileReader(boardfile));
            String input = fieldset.readLine();
            String[] args;
            while(input != null){
                args = input.split(" - ");
                int position = fields.size();
                String name = args[0];
                String type = args[1];
                String color = args[2];
                int price = Integer.parseInt(args[3]);
                String[] stages = args[4].split(":");
                int[] rent_stages = new int[stages.length];
                for(int i = 0; i<stages.length; i++){
                    rent_stages[i] = Integer.parseInt(stages[i]);
                }
                fields.add(new Field(name, type, color, position, price, rent_stages));
            }
        }
        catch(java.io.IOException exception){
            System.err.println(exception);
        }
    }


}
