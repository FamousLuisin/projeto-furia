package com.furia.challenge.api.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.furia.challenge.api.models.messages.MessageModel;

@Repository
public interface MessageRepository extends JpaRepository<MessageModel, UUID> {
    
}
