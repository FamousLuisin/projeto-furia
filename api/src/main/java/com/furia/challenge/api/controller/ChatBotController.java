package com.furia.challenge.api.controller;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.furia.challenge.api.models.chats.ChatModel;
import com.furia.challenge.api.models.messages.MessageModel;
import com.furia.challenge.api.models.messages.dto.MessageDto;
import com.furia.challenge.api.models.users.UserModel;
import com.furia.challenge.api.repository.ChatRepository;
import com.furia.challenge.api.repository.MessageRepository;
import com.furia.challenge.api.repository.UserRepository;
import com.furia.challenge.api.services.ChatBotService;


@RestController
@RequestMapping(path = "/chatbot")
public class ChatBotController {

    @Autowired
    private ChatBotService chatBotService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ChatRepository chatRepository;

    @PostMapping
    public ResponseEntity<?> chatBotMessage(@RequestBody MessageDto message){

        String resposta = chatBotService.sentenceAnalysis(message.getContent());

        MessageModel botResposta = new MessageModel();
        
        Optional<UserModel> user = userRepository.findByUsername(message.getUsername());
        Optional<UserModel> bot = userRepository.findByUsername("chat bot ai");
        if (user.isPresent()) {
            String chatTitle = user.get().getUsername() + "Bot";
            
            ChatModel chatBot = chatRepository.findByTitle(chatTitle).orElseGet(() -> chatRepository.save(new ChatModel(chatTitle)));

            MessageModel messageToBot = new MessageModel();
            messageToBot.setChat(chatBot);
            messageToBot.setContent(message.getContent());
            messageToBot.setUser(user.get());
            messageToBot.setSentAt(message.getSent_at());

            message.setId(messageToBot.getId());

            messageRepository.save(messageToBot);

            if (bot.isPresent()) {
                botResposta.setContent(resposta);
                botResposta.setIs_bot(true);
                botResposta.setUser(bot.get());
                botResposta.setChat(chatBot);
                botResposta.setSentAt(Calendar.getInstance());
                messageRepository.save(botResposta);
            }

            MessageDto botRespostaDto = new MessageDto();
            botRespostaDto.setContent(resposta);
            botRespostaDto.setId(botResposta.getId());
            botRespostaDto.setUsername("chat bot ai");
            botRespostaDto.setIs_bot(true);
            botRespostaDto.setSent_at(botResposta.getSentAt());

            List<MessageDto> mensagens = List.of(message, botRespostaDto);
            return ResponseEntity.ok(mensagens);
        }

        return ResponseEntity.badRequest().build();
    }
}
