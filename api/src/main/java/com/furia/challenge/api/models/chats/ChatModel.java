package com.furia.challenge.api.models.chats;

import java.io.Serializable;
import java.util.Calendar;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Entity(name = "tb_chats") @Table(name = "tb_chats")
public class ChatModel implements Serializable{
    private static final long serialVersionUID = 1L;

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String title;
    private Calendar created_at;
    private Calendar updated_at;

    public ChatModel(String title){
        this.title = title;
    }
}
