package com.furia.challenge.api.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
public class Home {
    
    @GetMapping
    public String home(JwtAuthenticationToken token){
        return "Home " + token.getToken().getSubject();
    }
}