package com.furia.challenge.api.models.messages;

import java.io.Serializable;
import java.util.Calendar;
import java.util.UUID;

import com.furia.challenge.api.models.chats.ChatModel;
import com.furia.challenge.api.models.users.UserModel;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Entity(name = "tb_messages") @Table(name = "tb_messages")
public class MessageModel implements Serializable{
    private static final long serialVersionUID = 1L;

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String content;
    
    @Column(name = "sent_at")
    private Calendar sentAt;
    private Calendar edited_at;
    private Boolean is_deleted = false;
    private Boolean is_bot = false;

    @ManyToOne @JoinColumn(name = "sender_id")
    private UserModel user;
    
    @ManyToOne @JoinColumn(name = "chat_id")
    private ChatModel chat;

    public MessageModel(String content, Calendar sentAt){
        this.content = content;
        this.sentAt = sentAt;
    }
}
