package com.furia.challenge.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.furia.challenge.api.models.users.dto.LoginRequest;
import com.furia.challenge.api.models.users.dto.LoginResponse;
import com.furia.challenge.api.services.UserService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping(path = "/login")
public class LoginController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<?> login(@RequestBody LoginRequest data) {
        
        try {
            String tokenJwt = userService.LoginUser(data);
            LoginResponse response = new LoginResponse();
            response.setAccessToken(tokenJwt);

            return ResponseEntity.ok(response);
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getBody());
        }
    }
    
}
