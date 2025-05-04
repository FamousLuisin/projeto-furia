package com.furia.challenge.api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.furia.challenge.api.models.chats.ChatModel;
import com.furia.challenge.api.models.messages.MessageModel;
import com.furia.challenge.api.models.messages.dto.MessageDto;
import com.furia.challenge.api.repository.ChatRepository;
import com.furia.challenge.api.repository.MessageRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping(path = "/messages")
public class MessageController {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ChatRepository chatRepository;
    
    @GetMapping
    public ResponseEntity<?> getMessages(JwtAuthenticationToken token) {
        Optional<ChatModel> chatCs = chatRepository.findByTitle("cs:go");
        Optional<ChatModel> chatBot = chatRepository.findByTitle(token.getName() + "Bot");

        List<MessageModel> messages;
        List<ChatModel> chats;

        if (chatCs.isPresent()) {
            if (chatBot.isPresent()) {
                chats = List.of(chatCs.get(), chatBot.get());
            } else {
                chats = List.of(chatCs.get());
            }
            messages = messageRepository.findByChatInOrderBySentAt(chats);
        } else {
            messages = new ArrayList<>();
        }

        List<MessageDto> dto = messages.stream().map(message -> {
                MessageDto newDto = new MessageDto();
                newDto.setContent(message.getContent());
                newDto.setUsername(message.getUser().getUsername());
                newDto.setId(message.getId());
                newDto.setSent_at(message.getSentAt());
                newDto.setIs_bot(message.getIs_bot());

                return newDto;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(dto);
    }
    
}
