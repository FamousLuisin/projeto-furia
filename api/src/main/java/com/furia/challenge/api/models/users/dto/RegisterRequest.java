package com.furia.challenge.api.models.users.dto;

import java.util.Calendar;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RegisterRequest {
    private String first_name;
    private String last_name;
    private String email; 
    private String password;
    private String cpf; 
    private String username; 
    private Calendar birth_date;
} 