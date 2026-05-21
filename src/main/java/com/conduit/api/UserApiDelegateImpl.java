package com.conduit.api;

import com.conduit.openapi.api.UserApiDelegate;
import com.conduit.openapi.model.UserResponse;
import com.conduit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserApiDelegateImpl implements UserApiDelegate {

    private final UserService userService;

    @Override
    public ResponseEntity<UserResponse> getCurrentUser() {
        UserResponse userResponse = new UserResponse();
        var user = userService.getCurrentUser();
        userResponse.setUser(user);
        return ResponseEntity.status(HttpStatus.OK).body(userResponse);
    }

}
