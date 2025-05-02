package com.furia.challenge.api.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.furia.challenge.api.models.users.UserModel;


@Repository
public interface UserRepository extends JpaRepository<UserModel, UUID> {
     
    Optional<UserModel> findByEmail(String email);

    Optional<UserModel> findByUsername(String username);

}
