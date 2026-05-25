package com.conduit.api;

import com.conduit.openapi.api.UserApiDelegate;
import com.conduit.openapi.model.UpdateCurrentUserRequest;
import com.conduit.openapi.model.UpdateUser;
import com.conduit.openapi.model.UserResponse;
import com.conduit.service.UpdateUserValidator;
import com.conduit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;

@Service
@RequiredArgsConstructor
public class UserApiDelegateImpl implements UserApiDelegate {

    private final UserService userService;
    private final UpdateUserValidator validator;

    @Override
    public ResponseEntity<UserResponse> getCurrentUser() {
        UserResponse userResponse = new UserResponse();
        var user = userService.getCurrentUser();
        userResponse.setUser(user);
        return ResponseEntity.status(HttpStatus.OK).body(userResponse);
    }

    @Override
    public ResponseEntity<UserResponse> updateCurrentUser(UpdateCurrentUserRequest body) {

        //Take the user and validate the input first
        UpdateUser user = body.getUser();
        BeanPropertyBindingResult errors = new BeanPropertyBindingResult(user, "updateUser");
        validator.validate(user, errors);
        if (errors.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        //Send it to the service to save it
        var userDTO = userService.updateUser(user);
        //Get the user response and save it
        UserResponse userResponse = new UserResponse();
        userResponse.setUser(userDTO);
        return ResponseEntity.status(HttpStatus.OK).body(userResponse);

    }

}
