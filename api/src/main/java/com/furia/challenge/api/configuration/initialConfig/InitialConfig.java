package com.furia.challenge.api.configuration.initialConfig;

import java.util.Calendar;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import com.furia.challenge.api.models.chats.ChatModel;
import com.furia.challenge.api.models.users.UserModel;
import com.furia.challenge.api.repository.ChatRepository;
import com.furia.challenge.api.repository.UserRepository;

import jakarta.transaction.Transactional;

@Configuration
public class InitialConfig implements CommandLineRunner {
    
    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        
        Optional<ChatModel> chat = chatRepository.findByTitle("cs:go");

        if (chat.isEmpty()) {
            ChatModel newChat = new ChatModel("cs:go");
            chatRepository.save(newChat);
        }

        Optional<UserModel> bot = userRepository.findByUsername("bot-ai");

        if (bot.isEmpty()) {
            UserModel bot_ai = new UserModel();
            bot_ai.setFirst_name("bot");
            bot_ai.setFirst_name("ai");
            bot_ai.setEmail("bot@email.com");
            bot_ai.setUsername("bot-ai");
            bot_ai.setCpf("000000000");
            bot_ai.setBirth_date(Calendar.getInstance());
        }
    }
}
