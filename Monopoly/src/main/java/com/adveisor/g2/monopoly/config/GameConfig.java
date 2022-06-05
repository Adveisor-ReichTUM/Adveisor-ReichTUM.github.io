/*
 * Copyright (c) ReichTUM 2022.
 */

package com.adveisor.g2.monopoly.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GameConfig {

    @Bean(name = "boardFile")
    String getBoardFileName() {
        return "/text/board.txt";
    }
    @Bean(name = "chanceFile")
    String getChanceFileName() {
        return "/text/chanceDeck.txt";
    }
    @Bean(name = "communityFile")
    String getCommunityFileName() {
        return "/text/communityDeck.txt";
    }

}
