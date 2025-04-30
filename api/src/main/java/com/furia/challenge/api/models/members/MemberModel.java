package com.furia.challenge.api.models.members;

import java.io.Serializable;
import java.util.Calendar;

import com.furia.challenge.api.models.chats.ChatModel;
import com.furia.challenge.api.models.users.UserModel;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Entity(name = "tb_chat_members") @Table(name = "tb_chat_members")
public class MemberModel implements Serializable{
    private static final long serialVersionUID = 1L;

    @Id @ManyToOne @JoinColumn(name = "user_id")
    private UserModel user;

    @Id @ManyToOne @JoinColumn(name = "chat_id")
    private ChatModel chat;

    private Calendar joined_at;
    private String role;

    public MemberModel(UserModel user, ChatModel chat, String role){
        this.user = user;
        this.chat = chat;
        this.role = role;
    }
}
