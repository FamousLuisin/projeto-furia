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
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping(path = "/messages")
public class MessageController {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ChatRepository chatRepository;
    
    @GetMapping
    public ResponseEntity<?> getMessages() {
        Optional<ChatModel> chatCs = chatRepository.findByTitle("cs:go");

        List<MessageModel> messages;

        if (chatCs.isPresent()) {
            messages = messageRepository.findByChatOrderBySentAt(chatCs.get());
        } else {
            messages = new ArrayList<>(); // Use ArrayList ou outra implementação de List
        }

        List<MessageDto> dto = messages.stream().map(message -> {
                MessageDto newDto = new MessageDto();
                newDto.setContent(message.getContent());
                newDto.setUsername(message.getUser().getUsername());
                newDto.setId(message.getId());
                newDto.setSent_at(message.getSentAt());

                return newDto;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(dto);
    }
    
}
