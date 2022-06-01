/*
 * Copyright (c) ReichTUM 2022.
 */

package com.adveisor.g2.monopoly.engine.service;

import com.adveisor.g2.monopoly.engine.service.model.*;
import com.adveisor.g2.monopoly.engine.service.model.status.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Service
public class GameService {

    // numbering of the current game status
    // increment every time the game status update
    private Long statusId = 0L;

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
    private Deck communityDeck;
    private Deck chanceDeck;
    private Board board;

    // all the possible statuses initialized here
    @JsonIgnore
    private AbstractStatus auctionStatus = new AuctionStatus(this);
    @JsonIgnore
    private AbstractStatus buyPropertyStatus = new BuyPropertyStatus(this);
    @JsonIgnore
    private AbstractStatus cardStatus = new CardStatus(this);
    @JsonIgnore
    private AbstractStatus diceStatus = new DiceStatus(this);
    @JsonIgnore
    private AbstractStatus endStatus = new EndStatus(this);
    @JsonIgnore
    private AbstractStatus jailStatus = new JailStatus(this);
    @JsonIgnore
    private AbstractStatus startStatus = new StartStatus(this);
    @JsonIgnore
    private AbstractStatus turnStatus = new TurnStatus(this);
    @JsonIgnore
    private AbstractStatus waitingStatus = new WaitingStatus(this);
    //

    @JsonIgnore
    private AbstractStatus currentStatus;

    // constructor
    @Autowired
    public GameService(String boardfile, String chancefile, String communityfile){
        // set up board
        this.board = new Board(boardfile);

        // set up chance Deck
        this.chanceDeck = new Deck(true, chancefile);

        // set up community Deck
        this.communityDeck = new Deck(false, communityfile);

        //this.status = Status.WAITING;
        this.numHotels = 12;
        this.numHouses = 32;
        this.currentPlayer = 0;

        // game start at this status
        this.currentStatus = this.waitingStatus;

    }

    public void incrementId() {
        this.statusId++;
    }

    public void join(String name, Piece piece){
       currentStatus.join(name, piece);
    }

    public void start(){
        currentStatus.start();
    }

    public String end(){
        this.currentStatus = this.endStatus;
        String winner = "";
        int greatestWealth = 0;
        for(int i = 0; i<players.size(); i++){
            if(players.get(i).calculateWealth()>greatestWealth) winner = players.get(i).getName();
        }
        return winner;
    }

    public void turn1(){
        currentStatus.turn1();
    }


    public void turn2(){
        Player player = players.get(currentPlayer);

        //Würfeln
        setCurrentStatus(getDiceStatus());
        player.throwDices();

        // Anpassen von Gefängnissituation entsprechend nach Wurfergebnis
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
        //Bewegen des Spielers und Evaluation der Position
        player.moveAndEvaluate(this.getBoard());

        //Option zum Bauen, Tauschen, Hypothek
        manage();
    }

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
    /*public void setStatus(Status status){
        this.status = status;
    }*/

    public void buy(){
        Player player = players.get(currentPlayer);
        Field field = board.getFields().get(player.getPosition());
        player.buy(field);
    }

    public void sellBank(int fieldIndex){
        currentStatus.sellBank(fieldIndex);
    }
    /*public void sellBank(int fieldIndex){
        if (status != Status.TURN) throw new IllegalStateException("Tried to sell despite not being in TURN");
        Player player = players.get(currentPlayer);
        Field field = getBoard().getFields().get(fieldIndex);
        if(player.getPossession(fieldIndex)) throw new IllegalStateException("Tried to sell property not in possession");
        if(field.isHypothek()){
            player.endMortgage(fieldIndex);
            return;
        }
        if(field.getNumHouses()>0) return;
        player.adjustBalance(field.getPrice()/2);
        player.setPossession(fieldIndex, false);
        field.reset();
    }*/

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
        setCurrentStatus(getAuctionStatus());

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
        currentStatus.startMortgage(fieldIndex);
    }
    /*public void startMortgage(int fieldIndex){
        Player player = getPlayers().get(currentPlayer);
        player.startMortgage(fieldIndex);
    }*/

    public void endMortgage(int fieldIndex){
        currentStatus.endMortgage(fieldIndex);
    }
    /*public void endMortgage(int fieldIndex){
        if(game.getStatus() != Status.TURN) throw new IllegalStateException("Cannot end mortgage while not being in TURN.");
        Player player = getPlayers().get(currentPlayer);
        player.endMortgage(fieldIndex);
    }*/

    public void buyHouse(int fieldIndex){
        currentStatus.buyHouse(fieldIndex);
    }
    /*public void buyHouse(int fieldIndex){
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
    }*/

    public void sellHouse(int fieldIndex){
        currentStatus.sellHouse(fieldIndex);
    }
    /*public void sellHouse(int fieldIndex){
        if(status != Status.TURN) throw new IllegalStateException("Can not sell house while not being in TURN");
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
    }*/
    public void manage(){
        setCurrentStatus(getTurnStatus());
    }

}
