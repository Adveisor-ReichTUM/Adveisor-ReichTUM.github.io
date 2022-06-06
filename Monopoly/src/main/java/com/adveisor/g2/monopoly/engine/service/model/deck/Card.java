/*
 * Copyright (c) ReichTUM 2022.
 */

package com.adveisor.g2.monopoly.engine.service.model.deck;

import com.adveisor.g2.monopoly.util.Logger;
import lombok.Data;

@Data
public class Card {
    private final CardType cardType;
    private final String description;
    private final String value;
    private final boolean chanceCard;     // true if chance card, false if community card

    public Card(String desc, String cardType, String value, boolean chanceDeck) {
        this.cardType = CardType.valueOf(cardType);
        this.description = desc;
        this.value = value;
        this.chanceCard = chanceDeck;
    }


    public int identifyTargetPosition(int current_pos){

        if(!this.chanceCard) {
            switch (value) {
                case "Gefängnis": return 30;
                case "LOS": return 0;
                default: System.out.println("Ungültige Karte"); return -1;
            }
        }
        else{
            switch (value){
                case "Schlossalle": return 39;
                case "Südbahnhof": return 5;
                case "Bahnhof":
                    if(current_pos < 5 || current_pos >= 35) return 5;
                    else if(current_pos < 15) return 15;
                    else if(current_pos < 25) return 25;
                    else return 35;
                case "Opernplatz": return 24;
                case "Werke":
                    if(current_pos < 12 || current_pos >= 28) return 12;
                    else return 28;
                case "Gefängnis": return 30;
                case "LOS": return 0;
                case "Seestraße": return 11;
                case "Rückwärts": return (current_pos-3);
                default: Logger.log("Ungültige Karte"); return current_pos;
            }
        }
    }


}
