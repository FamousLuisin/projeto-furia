package com.furia.challenge.api.services;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.furia.challenge.api.models.users.UserModel;
import com.furia.challenge.api.models.users.dto.RegisterRequest;
import com.furia.challenge.api.models.users.dto.UserResponse;
import com.furia.challenge.api.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private ModelMapper modelMapper;
    
    @Autowired
    private UserRepository userRepository;

    public UserResponse RegisterUser(RegisterRequest data){
        try {
            UserModel newUser = new UserModel();

            newUser.setFirst_name(data.getFirst_name());
            newUser.setLast_name(data.getLast_name());
            newUser.setEmail(data.getEmail());
            newUser.setPassword(data.getPassword());
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
}
