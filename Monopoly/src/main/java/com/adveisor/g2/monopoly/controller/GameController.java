package com.adveisor.g2.monopoly.controller;

import com.adveisor.g2.monopoly.engine.service.model.Game;
import com.adveisor.g2.monopoly.engine.service.model.Piece;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.web.bind.annotation.*;

@RestController
@EnableAutoConfiguration
public class GameController {

    public static Game game;

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
}
