package com.furia.challenge.api.models.users;

import java.io.Serializable;
import java.util.Calendar;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Entity(name = "tb_users") @Table(name = "tb_users")
public class UserModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String first_name;
    private String last_name;
    private String email;
    private String password;
    private String cpf;
    private String username;
    private Calendar birth_date;
    private Boolean is_active = true;
    private Boolean is_verified = false;
    
    @CreationTimestamp
    private Calendar created_at;

    public UserModel(String first_name, String last_name, String email, String cpf, String username, Calendar birth_date) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.cpf = cpf;
        this.username = username;
        this.birth_date = birth_date;
    }
}
