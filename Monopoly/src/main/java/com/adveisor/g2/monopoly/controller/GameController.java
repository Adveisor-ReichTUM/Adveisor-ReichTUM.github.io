package com.adveisor.g2.monopoly.controller;

import com.adveisor.g2.monopoly.engine.service.model.Game;
import com.adveisor.g2.monopoly.engine.service.model.Piece;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.web.bind.annotation.*;

@RestController
@EnableAutoConfiguration
public class GameController {
    @Autowired
    private static Game game = new Game("/text/board.txt", "/text/chanceDeck.txt", "/text/communityDeck.txt");

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
    public Game getGame(){
        return game;
    }

    @RequestMapping(value="/join", method = RequestMethod.GET, produces="application/json")
    public Game join(@RequestParam String name, @RequestParam Piece piece){
        game.join(name, piece);
        return game;
    }

    @RequestMapping(value="/start", method=RequestMethod.GET, produces="application/json")
    public Game start(){
        game.start();
        return game;
    }

    @RequestMapping(value="/reset", method = RequestMethod.GET, produces="application/json")
    public Game reset(){
        game = new Game("setupfiles/board.txt", "setupfiles/chanceDeck.txt", "communityDeck.txt");
        return game;
    }

    @RequestMapping(value="/end", method = RequestMethod.GET, produces="application/json")
    public Game end(){
        game.end();
        return game;
    }

    @RequestMapping(value="/rollingdices", method = RequestMethod.GET, produces="application/json")
    public Game rollAndMove(){
        game.rollAndMove();
        return game;
    }

    @RequestMapping(value="/buyproperty", method = RequestMethod.GET, produces="application/json")
    public Game buy(){
        game.buy();
        return game;
    }

    public static void main(String[] args) throws Exception{
        SpringApplication.run(GameController.class, args);
        //game = new Game("/text/board.txt", "/text/chanceDeck.txt", "/text/communityDeck.txt");
    }
}
