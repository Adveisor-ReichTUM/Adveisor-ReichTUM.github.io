/*
 * Copyright (c) ReichTUM 2022.
 */

package com.adveisor.g2.monopoly.engine.service.model;

import com.adveisor.g2.monopoly.engine.service.model.board.Board;
import com.adveisor.g2.monopoly.engine.service.model.deck.Deck;
import com.adveisor.g2.monopoly.engine.service.model.player.Player;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

@Getter
@Setter
public class Game {

    // numbering of the current game status
    // increment every time the game status update
    private Long versionId = 0L;

    private int numPlayers;
    private int numActivePlayers;
    private int numBankruptPlayers;

    private int numHouses;
    private int numHotels;
    private String currentPlayerId;

    private List<Player> players;

    @JsonIgnore
    private Deck communityDeck;
    @JsonIgnore
    private Deck chanceDeck;
    @JsonIgnore
    private Board board;

    public Game(String boardFile, String chanceFile, String communityFile){
        // set up board
        this.board = new Board(boardFile);

        // set up chance Deck
        this.chanceDeck = new Deck(true, chanceFile);

        // set up community Deck
        this.communityDeck = new Deck(false, communityFile);

        this.numHotels = 12;
        this.numHouses = 32;
        this.players = new CopyOnWriteArrayList<>();

    }

    public void resetGame() {
        chanceDeck.shuffleDeck();
        communityDeck.shuffleDeck();

        this.numHotels = 12;
        this.numHouses = 32;

        players.clear();

        numActivePlayers = numPlayers = numBankruptPlayers = 0;
    }

    public Optional<Player> findPlayerById(String target) {
        for(Player player : players) {
            if (Objects.equals(player.getPlayerId(), target)) {
                return Optional.of(player);
            }
        }
        return Optional.empty();
    }

    public Player findCurrentPlayer() {
        return findPlayerById(getCurrentPlayerId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Player data lost"));
    }

    public void bankrupt(Player player){
        numBankruptPlayers++;
        numActivePlayers--;
        player.setBankrupt(true);
    }

    public void setNextPlayer() {
        int currentPlayerIndex = players.indexOf(findCurrentPlayer());
        int nextPlayerIndex = currentPlayerIndex + 1 >= players.size() ? 0 : currentPlayerIndex + 1;
        setCurrentPlayerId(players.get(nextPlayerIndex).getPlayerId());
    }

    public void incrementVersionId() {
        this.versionId++;
    }

}
