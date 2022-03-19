import java.util.ArrayList;
import java.io.FileReader;
import java.io.BufferedReader;

public class Deck {
    private final boolean isChanceDeck;
    private ArrayList<Card> cards;

    public Deck(boolean isChanceDeck, String filename){
        this.isChanceDeck = isChanceDeck;
        try{
            BufferedReader cardset = new BufferedReader(new FileReader(filename));
            String input = cardset.readLine();
            String[] parts;
            while(input != null){
                parts = input.split("-");
                cards.add(new Card(parts[2], parts[0], parts[1], isChanceDeck));
                input = cardset.readLine();
            }
        }
        catch(java.io.IOException exception){
            System.err.println(exception);
        }
    }
}
