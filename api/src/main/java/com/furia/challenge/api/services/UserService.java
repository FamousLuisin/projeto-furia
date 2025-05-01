package com.furia.challenge.api.services;
import java.time.Instant;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.furia.challenge.api.models.users.UserModel;
import com.furia.challenge.api.models.users.dto.LoginRequest;
import com.furia.challenge.api.models.users.dto.RegisterRequest;
import com.furia.challenge.api.models.users.dto.UserResponse;
import com.furia.challenge.api.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private ModelMapper modelMapper;
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtEncoder jwtEncoder;

    public UserResponse RegisterUser(RegisterRequest data){
        try {
            UserModel newUser = new UserModel();

            newUser.setFirst_name(data.getFirst_name());
            newUser.setLast_name(data.getLast_name());
            newUser.setEmail(data.getEmail());
            newUser.setPassword(passwordEncoder.encode(data.getPassword()));
            newUser.setCpf(data.getCpf());
            newUser.setUsername(data.getUsername());
            newUser.setBirth_date(data.getBirth_date());

            userRepository.save(newUser);

            return modelMapper.map(newUser, UserResponse.class);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error: dados inválidos");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error: falha ao salvar dados");
        }
    }

    public String LoginUser(LoginRequest data){

        Optional<UserModel> user = userRepository.findByEmail(data.getEmail());

        if (user.isEmpty() || !this.isLoginCorrect(user.get(), data)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error: usuario ou senha inválidos");
        }

        var expiresIn = 600L;
        var now = Instant.now();

        var claims = JwtClaimsSet.builder()
            .issuer("furia_api")
            .subject(user.get().getEmail())
            .issuedAt(now)
            .expiresAt(now.plusSeconds(expiresIn))
            .build();

        var jwtValue = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        return jwtValue;
    }

    public boolean isLoginCorrect(UserModel user, LoginRequest data){
        return passwordEncoder.matches(data.getPassword(), user.getPassword());
    }
}
