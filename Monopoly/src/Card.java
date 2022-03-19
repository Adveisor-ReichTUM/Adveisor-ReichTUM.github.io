public class Card {
    private enum cardType {renovation, get_money_bank, get_money_player, pay_money_bank, pay_money_player, out_of_jail, move_via_GO, move_not_GO, houses_renovation};
    private final cardType type;

    private final int id;
    private final String description;
    private final String value;
    private final boolean chance_deck;     // true if chance card, false if community card

    public Card(String desc, String type, String value, int id, boolean chance_deck) {
        this.type = cardType.valueOf(type);
        this.description = desc;
        this.id = id;
        this.value = value;
        this.chance_deck = chance_deck;
    }

    public cardType getCardType() {
        return this.type;
    }

    public String getDescription() {
        return this.description;
    }

    public void evaluateCard(Player player, Game game) {
        System.out.println(description);
        int diff = 0;
        int target = 0;
        switch (this.type) {
            case get_money_bank:
                diff = Integer.parseInt(value);
                player.adjustBalance(diff);
                break;
            case get_money_player:
                diff = Integer.parseInt(value);
                player.adjustBalance((game.getNumActivePlayers() - 1) * diff);
                for (Player p : game.getPlayers()) {
                    if (p != player)
                        p.adjustBalance(-diff);
                }
            case pay_money_bank:
                diff = -Integer.parseInt(value);
                player.adjustBalance(diff);
                break;
            case pay_money_player:
                diff = -Integer.parseInt(value);
                player.adjustBalance((game.getNumActivePlayers() - 1) * diff);
                for (Player p : game.getPlayers()) {
                    if (p != player)
                        p.adjustBalance(-diff);
                }
                break;
            case out_of_jail:
                player.setNumJailCards(player.getNumJailCards() + 1);
                break;
            case move_via_GO:
                target = identifyTargetPosition(player.getPosition());
                if (player.getPosition() > target) {
                    player.adjustBalance(200);
                }
                player.setPosition(target);
            case move_not_GO:
                target = identifyTargetPosition(player.getPosition());
                player.setPosition(target);
            case renovation:
                int sum = 0;
                int numHouses = player.getNumHouses();
                int numHotels = player.getNumHotels();
                if (this.chance_deck)
                    sum = numHouses * 25 + numHotels * 100;
                else
                    sum = numHouses * 40 + numHotels * 115;
                player.adjustBalance(-sum);
        }
    }

    public int identifyTargetPosition(int current_pos){
        int pos = 40;   // moving 40 fields will end up with player on the same spot

        if(chance_deck) {
            switch (value) {
                case "Gefängnis": return 30;
                case "LOS": return 0;
                default: System.out.println("Ungültige Karte"); return -1;
            }
        }
        else{
            switch (value){
                case "Schlossalle": return 39;
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
                default: System.out.println("Ungültige Karte"); return -1;
            }
        }
    }
}
