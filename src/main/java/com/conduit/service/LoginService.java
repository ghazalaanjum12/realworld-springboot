package com.conduit.service;

import com.conduit.dto.UserResult;
import com.conduit.entity.User;
import com.conduit.openapi.model.LoginUser;
import com.conduit.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserRepository userRepository;

    public UserResult authenticate(LoginUser loginUser) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginUser.getEmail(), loginUser.getPassword()));
        var user = userRepository.findByEmail(loginUser.getEmail()).orElseThrow();
        var token = jwtService.generateToken(user);
        return new UserResult(user, token);
    }
}
