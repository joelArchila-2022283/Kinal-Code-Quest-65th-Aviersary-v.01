package com.J.M_CODERS.KinalCodeQuest.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping("/")
    public String index() {
        // Redirige a la ruta oficial
        return "redirect:/auth/login";
    }
}
