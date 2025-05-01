package com.furia.challenge.api.models.users.dto;

import java.util.Calendar;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserResponse {
    private String first_name;
    private String last_name;
    private String username;
    private String email;
    private Calendar birth_date;
}
