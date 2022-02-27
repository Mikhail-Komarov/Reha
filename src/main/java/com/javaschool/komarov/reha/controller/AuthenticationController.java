package com.javaschool.komarov.reha.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthenticationController {

    @GetMapping
    public String login() {
        return "login";
    }
}
