package com.furia.challenge.api.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
public class Home {
    
    @GetMapping
    public String home(){
        return "home";
    }
}