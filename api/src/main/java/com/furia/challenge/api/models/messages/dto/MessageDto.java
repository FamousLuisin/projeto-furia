package com.furia.challenge.api.models.messages.dto;

import java.util.Calendar;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class MessageDto {
    private UUID id;
    private String username;
    private String content;
    private Calendar sent_at;
    private Boolean is_bot;
}
