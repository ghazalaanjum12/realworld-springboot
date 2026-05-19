package com.conduit.service;

import com.conduit.dto.UserResult;
import com.conduit.entity.User;
import com.conduit.mapper.UserMapper;
import com.conduit.openapi.model.NewUser;
import com.conduit.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserMapper userMapper;

    public UserResult registerUser(NewUser newUser) {
        User user = userMapper.newUserToUser(newUser);
        user.setHashPassword(passwordEncoder.encode(newUser.getPassword()));
        var savedUser =  userRepository.save(user);
        var token = jwtService.generateToken(savedUser);
        return new UserResult(savedUser, token);
    }
}
