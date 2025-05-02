package com.furia.challenge.api.configuration.initialConfig;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import com.furia.challenge.api.models.chats.ChatModel;
import com.furia.challenge.api.repository.ChatRepository;

import jakarta.transaction.Transactional;

@Configuration
public class InitialConfig implements CommandLineRunner {
    
    @Autowired
    private ChatRepository chatRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        
        Optional<ChatModel> chat = chatRepository.findByTitle("cs:go");

        if (chat.isEmpty()) {
            ChatModel newChat = new ChatModel("cs:go");
            chatRepository.save(newChat);
        }
    }
}
