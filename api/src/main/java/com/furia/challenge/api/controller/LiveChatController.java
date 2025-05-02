package com.furia.challenge.api.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.furia.challenge.api.models.chats.ChatModel;
import com.furia.challenge.api.models.messages.MessageModel;
import com.furia.challenge.api.models.messages.dto.MessageDto;
import com.furia.challenge.api.models.users.UserModel;
import com.furia.challenge.api.repository.ChatRepository;
import com.furia.challenge.api.repository.MessageRepository;
import com.furia.challenge.api.repository.UserRepository;

@Controller
public class LiveChatController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ChatRepository chatRepository;
    
    @MessageMapping("/message")
    @SendTo("/topics/furia/livechat")
    public MessageDto newMessage(MessageDto message){
        MessageModel newMessage = new MessageModel();

        Optional<UserModel> user = userRepository.findByUsername(message.getUsername());
        Optional<ChatModel> chat = chatRepository.findByTitle("cs:go");

        newMessage.setContent(message.getContent());
        newMessage.setSentAt(message.getSent_at());
        newMessage.setUser(user.get());
        newMessage.setChat(chat.get());

        messageRepository.save(newMessage);

        message.setId(newMessage.getId());
      
        return message;
    }
}
