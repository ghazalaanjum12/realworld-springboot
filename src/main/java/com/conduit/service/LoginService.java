package com.conduit.service;

import com.conduit.dto.UserResult;
import com.conduit.openapi.model.LoginUser;
import com.conduit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    public UserResult authenticate(LoginUser loginUser) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginUser.getEmail(), loginUser.getPassword()));
        var user = userRepository.findByEmail(loginUser.getEmail()).orElseThrow();
        var token = jwtService.generateToken(user);
        return new UserResult(user, token);
    }
}
