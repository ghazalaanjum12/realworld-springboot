package com.conduit.exception;

import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RequestValidationException extends RuntimeException {
    private final Map<String, List<String>> fieldErrors;

    public RequestValidationException(Errors errors) {
        super("Validation failed for object: " + errors.getObjectName());
        if (errors.getFieldErrors() == null) {
            this.fieldErrors = Map.of("all", List.of("there should be alteast one value present"));
            return;
        }
        this.fieldErrors = errors.getFieldErrors().stream().collect(Collectors.toMap(FieldError::getField, fe -> List.of(fe.getDefaultMessage())));
    }

    public Map<String, List<String>> getFieldErrors() {
        return fieldErrors;
    }
}
