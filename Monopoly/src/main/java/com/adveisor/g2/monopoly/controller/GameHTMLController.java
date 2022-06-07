/*
 * Copyright (c) ReichTUM 2022.
 */

package com.adveisor.g2.monopoly.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * The controller for serving html webpage to the client
 */
@Controller
public class GameHTMLController {

    @GetMapping(value = "/chat", produces = "text/html")
    public String chatPage() {
        return "chat";
    }

    @GetMapping(value = "/login", produces = "text/html")
    public String loginPage() {
        return "/login-page/index";
    }
}
