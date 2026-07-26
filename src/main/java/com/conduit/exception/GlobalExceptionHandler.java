package com.conduit.exception;


import com.conduit.openapi.model.GenericErrorModel;
import io.jsonwebtoken.JwtException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
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

    @ExceptionHandler
    public ResponseEntity<GenericErrorModel> handleDuplicateIdException(DuplicateUserException exception) {
        GenericErrorModel error = new GenericErrorModel(exception.getFieldErrors());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<GenericErrorModel> handleBadCredentials() {
        GenericErrorModel error = new GenericErrorModel(Map.of("credentials", List.of("invalid")));
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler
    public ResponseEntity<GenericErrorModel> handleRequestValidationException(RequestValidationException exception) {
        GenericErrorModel error = new GenericErrorModel(exception.getFieldErrors());
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(error);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<GenericErrorModel> handleRequestValidationException() {
        GenericErrorModel error = new GenericErrorModel(Map.of("body", List.of("Unknown error")));
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(error);
    }

}
