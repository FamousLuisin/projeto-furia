package com.furia.challenge.api.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.furia.challenge.api.models.messages.MessageModel;
import com.furia.challenge.api.models.chats.ChatModel;


@Repository
public interface MessageRepository extends JpaRepository<MessageModel, UUID> {
    
    List<MessageModel> findByChatInOrderBySentAt(List<ChatModel> chats);
}
