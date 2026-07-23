package com.conduit.exception;


import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({JwtException.class, UsernameNotFoundException.class})
    public ResponseEntity<Map<String, Object>> handleAuthException() {
        return getErrorResponse(HttpStatus.UNAUTHORIZED, "Unauthorized");
    }

    private ResponseEntity<Map<String, Object>> getErrorResponse(HttpStatus status, String message) {
        Map<String, Object> body = Map.of("errors", Map.of("body", Collections.singletonList(message)));
        return ResponseEntity.status(status).body(body);
    }
}
