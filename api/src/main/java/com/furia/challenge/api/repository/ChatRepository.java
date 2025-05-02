package com.furia.challenge.api.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.furia.challenge.api.models.chats.ChatModel;


@Repository
public interface ChatRepository extends JpaRepository<ChatModel, UUID>{

    Optional<ChatModel> findByTitle(String title);
}