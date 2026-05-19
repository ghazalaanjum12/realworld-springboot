package com.conduit.api;

import com.conduit.dto.UserResult;
import com.conduit.mapper.UserMapper;
import com.conduit.openapi.api.UsersApiDelegate;
import com.conduit.openapi.model.CreateUserRequest;
import com.conduit.openapi.model.LoginRequest;
import com.conduit.openapi.model.UserResponse;
import com.conduit.service.LoginService;
import com.conduit.service.RegistrationService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class UsersApiDelegateImpl implements UsersApiDelegate {

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private LoginService loginService;

    @Autowired
    private UserMapper userMapper;

    @Override
    public ResponseEntity<UserResponse> createUser(CreateUserRequest createUserRequest) {

        UserResult result = registrationService.registerUser(createUserRequest.getUser());
        UserResponse userResponse = new UserResponse();
        var userDTO = userMapper.toUserDTO(result.user());
        userDTO.setToken(result.token());
        userResponse.setUser(userDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
    }

    @Override
    public ResponseEntity<UserResponse> login(
            @Parameter(name = "body", description = "Credentials to use", required = true) @Valid @RequestBody LoginRequest body
    ) {
        UserResponse userResponse = new UserResponse();
        UserResult result = loginService.authenticate(body.getUser());
        var userDTO = userMapper.toUserDTO(result.user());
        userDTO.setToken(result.token());
        userResponse.setUser(userDTO);
        return ResponseEntity.status(HttpStatus.OK).body(userResponse);
    }
}
