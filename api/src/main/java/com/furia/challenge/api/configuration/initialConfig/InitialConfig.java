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

import io.github.cdimascio.dotenv.Dotenv;
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
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
		
        Optional<ChatModel> chat = chatRepository.findByTitle("cs:go");

        if (chat.isEmpty()) {
            ChatModel newChat = new ChatModel("cs:go");
            chatRepository.save(newChat);
        }

        Optional<UserModel> userOptional = userRepository.findByUsername("chat bot ai");

        if (userOptional.isEmpty()) {
            String botEmail = dotenv.get("BOT_EMAIL");
            String botUsername = dotenv.get("BOT_USERNAME");
            String botPassword = dotenv.get("BOT_PASSWORD");
            String botCpf = dotenv.get("BOT_CPF");
            String botFirstName = dotenv.get("BOT_FIRST_NAME");
            String botLastName = dotenv.get("BOT_LAST_NAME");

            UserModel bot = new UserModel();

            bot.setFirst_name(botFirstName);
            bot.setLast_name(botLastName);
            bot.setEmail(botEmail);
            bot.setCpf(botCpf);
            bot.setUsername(botUsername);
            bot.setBirth_date(Calendar.getInstance());
            bot.setPassword(botPassword);

            bot.setIs_active(true);
            bot.setIs_verified(true);

            userRepository.save(bot);
        }
    }
}
