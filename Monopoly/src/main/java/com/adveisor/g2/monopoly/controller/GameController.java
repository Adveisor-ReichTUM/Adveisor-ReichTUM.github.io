package com.adveisor.g2.monopoly.controller;

import com.adveisor.g2.monopoly.engine.service.GameService;
import com.adveisor.g2.monopoly.engine.service.model.Dice;
import com.adveisor.g2.monopoly.engine.service.model.Game;
import com.adveisor.g2.monopoly.engine.service.model.PlayerBid;
import com.adveisor.g2.monopoly.engine.service.model.board.Field;
import com.adveisor.g2.monopoly.engine.service.model.deck.Card;
import com.adveisor.g2.monopoly.engine.service.model.player.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@Controller
public class GameController {
    private final GameService gameService;
    @Autowired
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }


    @RequestMapping(value="api/game", method = RequestMethod.GET, produces="application/json")
    @ResponseBody
    public Object getGame(@RequestParam(required = false) Optional<Long> id){
        if (id.orElse(0L).equals(gameService.getGameVersionId()))
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        else
            return gameService.getGame();
    }

    @RequestMapping(value="api/start", method=RequestMethod.GET, produces="application/json")
    @ResponseBody
    public Game start(){
        gameService.start();
        return gameService.getGame();
    }

    @RequestMapping(value="api/new-game", method = RequestMethod.GET, produces="application/json")
    @ResponseBody
    public Object reset(){
        gameService.resetGame();
        return gameService.getGame();
    }

    @RequestMapping(value="api/end", method = RequestMethod.GET, produces="application/json")
    @ResponseBody
    public Game end(){
        gameService.end();
        return gameService.getGame();
    }
    @PostMapping(value="api/join", produces="application/json")
    @ResponseBody
    public Player join(@RequestBody Player player){
        return gameService.join(player);
    }

    @PostMapping(value="api/dice-throw", produces="application/json")
    @ResponseBody
    public Dice throwDice(@Valid @RequestBody(required = false)Dice dice){
        return gameService.diceThrow(dice);
    }
    @PostMapping(value="api/end-round", produces="application/json")
    @ResponseBody
    public Game endCurrentRound(@RequestBody Player player){
        gameService.continueGame(player);
        return gameService.getGame();
    }
    @PostMapping(value = "/api/take-card", produces = "application/json")
    @ResponseBody
    public Card takeCard(@RequestBody Player player) {
        return gameService.takeCard(player);
    }


    @GetMapping("/api/current-player-field")
    @ResponseBody
    public Field currentPlayerField() {
        return gameService.currentPlayerStandingField();
    }



    @RequestMapping(value="api/buy-out-of-jail", method = RequestMethod.GET, produces="application/json")
    @ResponseBody
    public Game decideJail(@RequestBody Player player){
        gameService.buyOutOfJail(player);
        return gameService.getGame();
    }

    @RequestMapping(value="/jailcard", method = RequestMethod.GET, produces="application/json")
    @ResponseBody
    public Game useJailCard(@RequestBody Player player){
        gameService.useJailCard(player);
        return gameService.getGame();
    }

    @RequestMapping(value="/buyproperty", method = RequestMethod.GET, produces="application/json")
    @ResponseBody
    public Game buy(){
        gameService.buy();
        return gameService.getGame();
    }

    @RequestMapping(value="/sellpropertybank", method = RequestMethod.GET, produces="application/json")
    @ResponseBody
    public Game sellProp2Bank(@RequestParam int fieldIndex){
        gameService.sellBank(fieldIndex);
        return gameService.getGame();
    }

    @RequestMapping(value="/auction", method = RequestMethod.GET, produces="application/json")
    @ResponseBody
    public Game passProperty(@RequestParam int fieldIndex){
        gameService.auctionProperty(fieldIndex);
        return gameService.getGame();
    }

    @RequestMapping(value="/api/bid", method = RequestMethod.GET, produces="application/json")
    @ResponseBody
    public Game bid(@RequestBody PlayerBid playerBid){
        gameService.tryHighestBid(playerBid);
        return gameService.getGame();
    }

    @RequestMapping(value="/startmortgage", method = RequestMethod.GET, produces="application/json")
    @ResponseBody
    public Game startMortgage(@RequestParam int fieldIndex){
        gameService.startMortgage(fieldIndex);
        return gameService.getGame();
    }

    @RequestMapping(value="/endmortgage", method = RequestMethod.GET, produces="application/json")
    @ResponseBody
    public Game endMortgage(@RequestParam int fieldIndex){
        gameService.endMortgage(fieldIndex);
        return gameService.getGame();
    }

    @RequestMapping(value="/buyHouse", method = RequestMethod.GET, produces="application/json")
    @ResponseBody
    public Game buyHouse(@RequestParam int fieldIndex){
        gameService.buyHouse(fieldIndex);
        return gameService.getGame();
    }

    @RequestMapping(value="/sellHouse", method = RequestMethod.GET, produces="application/json")
    @ResponseBody
    public Game sellHouse(@RequestParam int fieldIndex){
        gameService.sellHouse(fieldIndex);
        return gameService.getGame();
    }

}
