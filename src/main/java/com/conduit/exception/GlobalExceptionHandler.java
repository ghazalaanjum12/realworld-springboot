package com.conduit.exception;


import com.conduit.openapi.model.GenericErrorModel;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({JwtException.class, UsernameNotFoundException.class})
    public ResponseEntity<GenericErrorModel> handleAuthException() {
        GenericErrorModel error = new GenericErrorModel(Map.of("body", List.of("Unauthroized")));
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(DuplicateIdException.class)
    public ResponseEntity<GenericErrorModel> handleDuplicateIdException() {
        GenericErrorModel error = new GenericErrorModel(Map.of("email", List.of("Duplicate email exists")));
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

}
