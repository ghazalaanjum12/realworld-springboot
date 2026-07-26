package com.conduit.service;

import com.conduit.dto.UserResult;
import com.conduit.entity.User;
import com.conduit.exception.DuplicateUserException;
import com.conduit.mapper.UserMapper;
import com.conduit.openapi.model.NewUser;
import com.conduit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegistrationService {
    public static final String ALREADY_REGISTERED = "User already registered with the these details: email %s and username %s";
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final UserMapper userMapper;

    public UserResult registerUser(NewUser newUser) {
        if (userRepository.findByEmail(newUser.getEmail()).isPresent()) {
            log.error(String.format(ALREADY_REGISTERED, newUser.getEmail(), newUser.getUsername()));
            Map<String, List<String>> fieldErrors = new HashMap<>();
            fieldErrors.put("email", List.of("has already been taken"));
            throw new DuplicateUserException(fieldErrors);
        }
        if (userRepository.findByHandle(newUser.getUsername()).isPresent()) {
            log.error(String.format(ALREADY_REGISTERED, newUser.getEmail(), newUser.getUsername()));
            Map<String, List<String>> fieldErrors = new HashMap<>();
            fieldErrors.put("username", List.of("has already been taken"));
            throw new DuplicateUserException(fieldErrors);
        }

        User user = userMapper.newUserToUser(newUser);
        user.setHashPassword(passwordEncoder.encode(newUser.getPassword()));
        var savedUser = userRepository.save(user);
        var token = jwtService.generateToken(savedUser);
        return new UserResult(savedUser, token);


    }
}
