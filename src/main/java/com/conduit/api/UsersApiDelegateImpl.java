package com.conduit.api;

import com.conduit.dto.UserResult;
import com.conduit.exception.RequestValidationException;
import com.conduit.mapper.UserMapper;
import com.conduit.openapi.api.UsersApiDelegate;
import com.conduit.openapi.model.CreateUserRequest;
import com.conduit.openapi.model.LoginRequest;
import com.conduit.openapi.model.UserResponse;
import com.conduit.service.LoginService;
import com.conduit.service.RegistrationService;
import com.conduit.validation.NewUserValidator;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@RequiredArgsConstructor
public class UsersApiDelegateImpl implements UsersApiDelegate {

    private final RegistrationService registrationService;
    private final LoginService loginService;
    private final UserMapper userMapper;
    private final NewUserValidator userValidator;

    @Override
    public ResponseEntity<UserResponse> createUser(CreateUserRequest createUserRequest) {

        var user = createUserRequest.getUser();
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(user, "newUser");
        userValidator.validate(user, errors);
        if (errors.hasErrors()) {
            throw new RequestValidationException(errors);
        }

        UserResult result = registrationService.registerUser(user);
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
