public class Field {
    private String street_name;
    private int position;
    private boolean owned;

    // reference attribute
    private Board board;

    // constructor
    public Field(String street_name, int position, Board board){
        this.street_name = street_name;
        this.position = position;
        this.board = board;
        this.owned = false;
    }

    public String getStreet_name(){
        return this.street_name;
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
