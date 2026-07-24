package com.conduit.service;

import com.conduit.dto.UserResult;
import com.conduit.entity.User;
import com.conduit.exception.DuplicateIdException;
import com.conduit.mapper.UserMapper;
import com.conduit.openapi.model.NewUser;
import com.conduit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegistrationService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final UserMapper userMapper;

    public UserResult registerUser(NewUser newUser) {
        if (userRepository.findByEmail(newUser.getEmail()).isPresent()) {
            throw new DuplicateIdException("Email already registered: " + newUser.getEmail());
        }
        try {
            User user = userMapper.newUserToUser(newUser);
            user.setHashPassword(passwordEncoder.encode(newUser.getPassword()));
            var savedUser = userRepository.save(user);
            var token = jwtService.generateToken(savedUser);
            return new UserResult(savedUser, token);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateIdException("Email already registered: " + newUser.getEmail());
        }

    }
}
