package com.adveisor.g2.monopoly.controller;

import com.adveisor.g2.monopoly.engine.service.GameService;
import com.adveisor.g2.monopoly.engine.service.model.Piece;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@EnableAutoConfiguration
public class GameController {
    @Autowired
    private static GameService gameService = new GameService("/text/board.txt", "/text/chanceDeck.txt", "/text/communityDeck.txt");

    // -----------------------
    @RequestMapping(value="/{file_name:.+}", method=RequestMethod.GET)
    public FileSystemResource getFrontend(@PathVariable("file_name") String file)
    {
        return new FileSystemResource("frontend/" + file);
    }

    @RequestMapping(value="/", method = RequestMethod.GET)
    public FileSystemResource get() {
        return getFrontend("index.html");
    }

    // -------------------------

    @RequestMapping(value="/game", method = RequestMethod.GET, produces="application/json")
    public Object getGame(@RequestParam(required = false) Optional<Long> id){
        if (id.orElse(0L).equals(gameService.getStatusId()))
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        else
            return gameService;
    }

    @RequestMapping(value="/join", method = RequestMethod.GET, produces="application/json")
    public GameService join(@RequestParam String name, @RequestParam Piece piece){
        gameService.join(name, piece);
        return gameService;
    }

    @RequestMapping(value="/start", method=RequestMethod.GET, produces="application/json")
    public GameService start(){
        gameService.start();
        return gameService;
    }

    @RequestMapping(value="/reset", method = RequestMethod.GET, produces="application/json")
    public GameService reset(){
        gameService = new GameService("setupfiles/board.txt", "setupfiles/chanceDeck.txt", "communityDeck.txt");
        return gameService;
    }

    @RequestMapping(value="/end", method = RequestMethod.GET, produces="application/json")
    public GameService end(){
        gameService.end();
        return gameService;
    }

    @RequestMapping(value="/rollingdices", method = RequestMethod.GET, produces="application/json")
    public GameService rollAndMove(){
        gameService.turn2();
        return gameService;
    }

    @RequestMapping(value="/jaildecision", method = RequestMethod.GET, produces="application/json")
    public GameService decideJail(@RequestParam boolean choice){
        gameService.decideJail(choice);
        return gameService;
    }

    @RequestMapping(value="/jailcard", method = RequestMethod.GET, produces="application/json")
    public GameService useJailCard(){
        gameService.useJailCard();
        return gameService;
    }

    @RequestMapping(value="/buyproperty", method = RequestMethod.GET, produces="application/json")
    public GameService buy(){
        gameService.buy();
        return gameService;
    }

    @RequestMapping(value="/sellpropertybank", method = RequestMethod.GET, produces="application/json")
    public GameService sellProp2Bank(@RequestParam int fieldIndex){
        gameService.sellBank(fieldIndex);
        return gameService;
    }

    @RequestMapping(value="/auction", method = RequestMethod.GET, produces="application/json")
    public GameService passProperty(@RequestParam int fieldIndex){
        gameService.auctionProperty(fieldIndex);
        return gameService;
    }

    @RequestMapping(value="/bid", method = RequestMethod.GET, produces="application/json")
    public GameService bid(@RequestParam String name, @RequestParam int bid){
        gameService.setBid(name, bid);
        return gameService;
    }

    @RequestMapping(value="/startmortgage", method = RequestMethod.GET, produces="application/json")
    public GameService startMortgage(@RequestParam int fieldIndex){
        gameService.startMortgage(fieldIndex);
        return gameService;
    }

    @RequestMapping(value="/endmortgage", method = RequestMethod.GET, produces="application/json")
    public GameService endMortgage(@RequestParam int fieldIndex){
        gameService.endMortgage(fieldIndex);
        return gameService;
    }

    @RequestMapping(value="/buyHouse", method = RequestMethod.GET, produces="application/json")
    public GameService buyHouse(@RequestParam int fieldIndex){
        gameService.buyHouse(fieldIndex);
        return gameService;
    }

    @RequestMapping(value="/sellHouse", method = RequestMethod.GET, produces="application/json")
    public GameService sellHouse(@RequestParam int fieldIndex){
        gameService.sellHouse(fieldIndex);
        return gameService;
    }

    public static void main(String[] args) throws Exception{
        SpringApplication.run(GameController.class, args);
        //game = new Game("/text/board.txt", "/text/chanceDeck.txt", "/text/communityDeck.txt");
    }
}
