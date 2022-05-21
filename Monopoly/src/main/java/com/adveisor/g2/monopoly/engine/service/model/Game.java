package com.adveisor.g2.monopoly.engine.service.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Game {

    //private enum Status {START, WAITING, DICE, CARD, PROPERTY, TURN, AUCTION, JAIL, END}
    private Status status;
    private int numPlayers;
    private int numActivePlayers;
    private int numBankruptPlayers;

    private int numHouses;

    private int numHotels;
    private int currentPlayer;
    private int numRounds;
    private boolean running;

    private String cardDescription;

    private int highestBidderIndex;

    private int highestBid;

    // reference attributes
    private List<Player> players = new ArrayList<>();
    //private ArrayList<Field> fields;
    private Deck communityDeck;
    private Deck chanceDeck;
    private Board board;

    // constructor
    @Autowired
    public Game(String boardfile, String chancefile, String communityfile){
        // set up board
        this.board = new Board(boardfile);

        // set up fields
        /*fields = new ArrayList<Field>;
        Field.setup(fields, fieldsFile);*/

        // set up chance Deck
        this.chanceDeck = new Deck(true, chancefile);

        // set up community Deck
        this.communityDeck = new Deck(false, communityfile);

        // Load players
        /*try{
            BufferedReader input = new BufferedReader(new FileReader(playerFile));
            numPlayers = Integer.parseInt(input.readLine())/2;
            for(int i = 0; i<numPlayers; i++){
                players.add(new Player(input.readLine(), this, Piece.valueOf(input.readLine())));
            }
            input.close();
        }
        catch(java.io.IOException exception){
            System.err.println(exception);
        }*/

        this.status = Status.WAITING;
        this.numHotels = 12;
        this.numHouses = 32;
        this.currentPlayer = 0;

    }


    public void join(String name, Piece piece){
        // check for failure
        if(status != status.WAITING) throw new IllegalStateException("Failed to join game: already running.");

        for(Player player : players){
            if(player.getName().equals(name)) throw new IllegalArgumentException("This name already exists.");
            if(player.getPiece().equals(piece)) throw new IllegalArgumentException("This color is already used by another player.");
        }

        // create player
        Player player = new Player(name, this, piece);
        players.add(player);
    }

    public void start(){
        if(status != status.WAITING) throw new IllegalStateException("Failed to start game: wrong status.");
        if(players.size()>4) throw new IllegalStateException("Monopoly is limited to only a maximum of 4 players.");
        if(players.size()<2) return;
    }

    public String end(){
        status = Status.END;
        String winner = "";
        int greatestWealth = 0;
        for(int i = 0; i<players.size(); i++){
            if(players.get(i).calculateWealth()>greatestWealth) winner = players.get(i).getName();
        }
        return winner;
    }

    public void turn1(){
        Player player = players.get(currentPlayer);

        if(status == Status.END) return;

        if(player.isBankrupt()==false && player.isPasch()==true){
            if(player.getNumPasch()==3) player.jail();
            else if (player.getNumPasch()<3) player.setNumPasch(player.getNumPasch()+1);
        }

        if(player.isPasch()==false){
            player.setNumPasch(0);
            while(player.isBankrupt()){
                currentPlayer = (currentPlayer +1)%players.size();
            }
        }

        //Abbruch falls ins Gefägnis gekommen
        if(player.isInJail()){
            status = Status.JAIL;
            return;
        }

        turn2();
    }

    public void turn2(){
        Player player = players.get(currentPlayer);

        //Würfeln
        status = Status.DICE;
        player.throwDices();

        if(player.isInJail()){
            if(player.isPasch()){
                player.setPasch(false); // Nach Gefägnis führt Pasch zu keinem zweiten Zug
                player.setRoundsInJail(0);
                player.setInJail(false);
                player.moveAndEvaluate(this.getBoard());
            } else{
                player.setRoundsInJail(player.getRoundsInJail()+1);
                manage();
                return;
            }
        }

        player.moveAndEvaluate(this.getBoard());

        manage();
    }
    /*public void rollAndMove(){
        Player player = players.get(currentPlayer);
        //if(player.isInJail()){
            status = Status.JAIL;
            return;
        //}
        if(status == Status.END) return;

        if(player.isInJail()==false && player.getPasch()==true){
            if(player.getNumPasch()==3) player.jail();
            else if (player.getNumPasch()<3) player.setNumPasch(player.getNumPasch()+1);
        }

        if(player.getPasch()==false){
            player.setNumPasch(0);
            while(player.isBankrupt()){
                currentPlayer = (currentPlayer +1)%players.size();
            }
        }

        //Abbruch falls ins Gefägnis gekommen
        if(player.isInJail()){
            status = Status.JAIL;
            return;
        }

        //Würfeln
        status = Status.DICE;
        player.throwDices();

        if(player.isInJail()){
            if(player.getPasch()){
                player.setPasch(false); // Nach Gefägnis führt Pasch zu keinem zweiten Zug
                player.setRoundsInJail(0);
                player.moveAndEvaluate(this.getBoard());
            } else{
                player.setRoundsInJail(player.getRoundsInJail()+1);
                return;
            }
        }

        player.moveAndEvaluate(this.getBoard());

        manage();
    }*/

    public void decideJail(boolean choice){
        Player player = getPlayers().get(currentPlayer);
        if(choice == true && player.getBalance()>=50){
            player.adjustBalance(-50);
            player.setRoundsInJail(0);
            player.setInJail(false);
            turn1();
        }
        turn2();
    }

    public void useJailCard(){
        Player player = getPlayers().get(currentPlayer);
        if(player.getNumJailCards()>1){
            player.setRoundsInJail(0);
            player.setInJail(false);
            turn1();
        }
    }
    public void setStatus(Status status){
        this.status = status;
    }

    public void setCardDescription(String desc){
        this.cardDescription = desc;
    }

    public String getCardDescription(){
        return this.cardDescription;
    }

    public void buy(){
        Player player = players.get(currentPlayer);
        Field field = board.getFields().get(player.getPosition());
        player.buy(field);
    }

    public void sellBank(int fieldIndex){
        if (status != Status.TURN) throw new IllegalStateException("Tried to sell despite not being in TURN");
        Player player = players.get(currentPlayer);
        Field field = getBoard().getFields().get(fieldIndex);
        if(player.checkPossession(fieldIndex)) throw new IllegalStateException("Tried to sell property not in possession");
        if(field.isHypothek()){
            player.endMortgage(fieldIndex);
            return;
        }
        if(field.getNumHouses()>0) return;
        player.adjustBalance(field.getPrice()/2);
        player.setPossession(fieldIndex, false);
        field.reset();

    }

    public void bankrupt(Player player){
        numBankruptPlayers++;
        numActivePlayers--;
        player.setBankrupt(true);

        if(numBankruptPlayers>=players.size()-1){
            end();
            return;
        }

        for(int i = 0; i<39; i++){
            if(player.getPossession(i)){
                Field field = board.getFields().get(i);
                auctionProperty(i);
            }
        }
    }

    public void auctionProperty(int fieldIndex){
        boolean timeout = false;
        setStatus(Status.AUCTION);

        int startingBid = board.getFields().get(fieldIndex).getPrice()/2;
        int highestBid = startingBid;
        int highestBidderIndex = -1;

        int timer = 10;
        while(!timeout){
            if(timer>0) timer--;
            else{
                if(highestBid>startingBid){
                    Player player = getPlayers().get(highestBidderIndex);
                    Field field = getBoard().getFields().get(fieldIndex);
                    player.setPossession(fieldIndex, true);
                    field.setOwned(true);
                    field.setOwner(highestBidderIndex);
                }
                timeout=true;
            }
            try {
                Thread.sleep(1000);
            } catch(InterruptedException e){
                e.printStackTrace();
            }
        }

        for(Player p: getPlayers()){
            p.setBid(0);
        }
        manage();
    }

    public void setBid(String name, int bid){
        int bidderIndex = -1;
        for(Player p: getPlayers()){
            if(p.getName().equals(name)){
                bidderIndex = getPlayers().indexOf(p);
                break;
            }
        }
        Player player = getPlayers().get(bidderIndex);

        if(bidderIndex==-1) throw new IllegalArgumentException("Bidder not found");
        if(player.getBalance()<bid) return;

        player.setBid(bid);
        if(bid>highestBid){
            highestBidderIndex = bidderIndex;
        }

    }

    public void startMortgage(int fieldIndex){
        Player player = getPlayers().get(currentPlayer);
        player.startMortgage(fieldIndex);
    }

    public void endMortgage(int fieldIndex){
        Player player = getPlayers().get(currentPlayer);
        player.endMortgage(fieldIndex);
    }

    public void buyHouse(int fieldIndex){
        if(status != Status.TURN) throw new IllegalStateException("Can not buy house while not being in TURN");

        Player player = getPlayers().get(currentPlayer);
        if(!player.getPossession(fieldIndex)) return;

        Field field = getBoard().getFields().get(fieldIndex);

        if(numHouses<=0) return;
        if(field.getNumHouses()==4 && numHotels<=0) return;
        if(field.isHypothek()) return;

        Color color = field.getColor();
        int minHouses = Integer.MAX_VALUE;
        // check if all the properties of a color are in players possession
        for(int i = 0; i<40; i++){
            Field running = getBoard().getFields().get(i);
            if(running.getColor()==color){
                if(running.getNumHouses()<minHouses) minHouses = running.getNumHouses();
                if(!player.getPossession(i)) return;
            }
        }

        if(field.getNumHouses()>minHouses) return;

        if(field.getNumHouses()==4){
            player.adjustBalance(-field.getHouseCost());
            field.setNumHouses(field.getNumHouses()+1);
            numHotels--;
            numHouses = numHouses +4;
        } else{
            player.adjustBalance(-field.getHouseCost());
            field.setNumHouses(field.getNumHouses()+1);
            numHouses--;
        }
    }
    public void sellHouse(int fieldIndex){
        if(status != Status.TURN) throw new IllegalStateException("Can not buy house while not being in TURN");
        if(numHouses<=0) return;

        Player player = getPlayers().get(currentPlayer);
        if(!player.getPossession(fieldIndex)) return;

        Field field = getBoard().getFields().get(fieldIndex);

        Color color = field.getColor();
        int maxHouses = Integer.MIN_VALUE;
        // check if all the properties of a color are in players possession
        for(int i = 0; i<40; i++){
            Field running = getBoard().getFields().get(i);
            if(running.getColor()==color){
                if(running.getNumHouses()>maxHouses) maxHouses = running.getNumHouses();
            }
        }

        if(field.getNumHouses()<maxHouses) return;

        if(field.getNumHouses()==5){
            if(numHouses<4) return;
            player.adjustBalance(field.getHouseCost()/2);
            field.setNumHouses(field.getNumHouses()-1);
            numHotels++;
            numHouses = numHouses -4;
        } else{
            player.adjustBalance(field.getHouseCost()/2);
            field.setNumHouses(field.getNumHouses()-1);
            numHouses++;
        }
    }
    public void manage(){
        setStatus(Status.TURN);
    }

    public void setCurrentPlayer(int currentPlayer){
        this.currentPlayer = currentPlayer;
    }
}
