public class Field {
    private String name;
    private int position;

    private boolean owned;
    private int owner;

    private int numHouses;
    private final int price;
    private int[] rent_stages;

    private enum colorType {braun, hellblau, pink, orange, rot, gelb, gruen, dunkelblau};
    private final colorType color;

    private enum fieldType {no_color, los, street, station, jail, police, parking, tax, chance, community, utilities};
    private final fieldType type;

    // constructor
    public Field(String name, String type, String color, int position, int price, int[] rent_stages){
        this.name = name;
        this.position = position;
        this.owned = false;
        this.type = fieldType.valueOf(type);
        this.price = price;
        this.rent_stages = rent_stages;
        this.color = colorType.valueOf(color);

        this.owned = false;
        this.owner = -1;
        this.numHouses = 0;
    }

    public String getName(){
        return this.name;
    }

    public int getPosition(){
        return this.position;
    }

    public boolean isOwned(){
        return owned;
    }

    public void setOwned(boolean owned){
        this.owned = owned;
    }
}
