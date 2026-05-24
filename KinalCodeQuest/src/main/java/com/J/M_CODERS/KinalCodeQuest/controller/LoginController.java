package com.J.M_CODERS.KinalCodeQuest.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/")
    public String index() {
        return "player/login";
    }

    @GetMapping("/login")
    public String login() {
        return "player/login";
    }
}
