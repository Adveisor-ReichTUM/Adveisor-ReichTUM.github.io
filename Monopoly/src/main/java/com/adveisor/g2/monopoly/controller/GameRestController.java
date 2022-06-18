package com.adveisor.g2.monopoly.controller;

import com.adveisor.g2.monopoly.engine.service.GameService;
import com.adveisor.g2.monopoly.engine.service.model.Dice;
import com.adveisor.g2.monopoly.engine.service.model.Game;
import com.adveisor.g2.monopoly.engine.service.model.PlayerBid;
import com.adveisor.g2.monopoly.engine.service.model.board.Field;
import com.adveisor.g2.monopoly.engine.service.model.deck.Card;
import com.adveisor.g2.monopoly.engine.service.model.player.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class GameRestController {
    private final GameService gameService;
    @Autowired
    public GameRestController(GameService gameService) {
        this.gameService = gameService;
    }


    @RequestMapping(value="/game", method = RequestMethod.GET, produces="application/json")
    public Object getGame(@RequestParam(required = false) Optional<Long> id){
        if (id.orElse(0L).equals(gameService.getGameVersionId()))
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        else
            return gameService.getGame();
    }

    @RequestMapping(value="/start", method=RequestMethod.GET, produces="application/json")
    public Game start(){
        gameService.start();
        return gameService.getGame();
    }

    @RequestMapping(value="/new-game", method = RequestMethod.GET, produces="application/json")
    public Object reset(){
        gameService.resetGame();
        return gameService.getGame();
    }

    @RequestMapping(value="/end", method = RequestMethod.GET, produces="application/json")
    public List<Player> end(){
        return gameService.end();
    }
    @PostMapping(value="/join", produces="application/json")
    public Player join(@RequestBody Player player){
        return gameService.join(player);
    }

    @PostMapping(value="/use-sim-dice", produces="application/json")
    public Dice useSimDice() {
        return gameService.diceThrow();
    }
    @PostMapping(value="/end-round", produces="application/json")
    public Game endCurrentRound(@RequestBody Player player){
        gameService.continueGame(player);
        return gameService.getGame();
    }
    @PostMapping(value = "/take-card", produces = "application/json")
    public Card takeCard(@RequestBody Player player) {
        return gameService.takeCard(player);
    }


    @GetMapping("/current-player-field")
    public Field currentPlayerField() {
        return gameService.currentPlayerStandingField();
    }



    @PostMapping(value="/buy-out-of-jail", produces="application/json")
    public Game buyOutOfJail(@RequestBody Player player){
        gameService.buyOutOfJail(player);
        return gameService.getGame();
    }

    @PostMapping(value="/use-jail-card", produces="application/json")
    public Game useJailCard(@RequestBody Player player){
        gameService.useJailCard(player);
        return gameService.getGame();
    }

    @PostMapping(value="/buy-property", produces="application/json")
    public Field buy(@RequestBody Player player){
        return gameService.buyProperty(player);
    }

    @RequestMapping(value="/sell-property-to-bank", method = RequestMethod.POST, produces="application/json")
    public Game sellPropertyToBank(@RequestParam int fieldIndex, @RequestBody Player player){
        gameService.sellPropertyToBank(fieldIndex, player);
        return gameService.getGame();
    }

    @RequestMapping(value="/start-auction", method = RequestMethod.POST, produces="application/json")
    public Game passProperty(@RequestParam int fieldIndex){
        gameService.auctionProperty(fieldIndex);
        return gameService.getGame();
    }

    @RequestMapping(value="/bid", method = RequestMethod.POST, produces="application/json")
    public Game bid(@RequestBody PlayerBid playerBid){
        gameService.tryHighestBid(playerBid);
        return gameService.getGame();
    }

    @RequestMapping(value="/start-mortgage", method = RequestMethod.POST, produces="application/json")
    public Game startMortgage(@RequestParam int fieldIndex, @RequestBody Player player){
        gameService.startMortgage(fieldIndex, player);
        return gameService.getGame();
    }

    @RequestMapping(value="/end-mortgage", method = RequestMethod.POST, produces="application/json")
    public Game endMortgage(@RequestParam int fieldIndex, @RequestBody Player player){
        gameService.endMortgage(fieldIndex, player);
        return gameService.getGame();
    }

    @RequestMapping(value="/buy-house", method = RequestMethod.POST, produces="application/json")
    public Game buyHouse(@RequestParam int fieldIndex, @RequestBody Player player){
        gameService.buyHouse(fieldIndex, player);
        return gameService.getGame();
    }

    @RequestMapping(value="/sell-House", method = RequestMethod.POST, produces="application/json")
    public Game sellHouse(@RequestParam int fieldIndex, @RequestBody Player player) {
        gameService.sellHouse(fieldIndex, player);
        return gameService.getGame();
    }

    @PostMapping(value = "/auction-player-property", produces = "application/json")
    public void auctionPlayerProperty(@RequestBody Player player) {
        gameService.startBankruptAuction(player);
    }

    @GetMapping(value = "/get-game-result", produces = "application/json")
    public List<Player> getGameResult() {
        return gameService.getGameResult();
    }
}
