package com.conduit.exception;

import java.util.List;
import java.util.Map;

public class DuplicateUserException extends RuntimeException {

    private final Map<String, List<String>> fieldErrors;

    public DuplicateUserException(Map<String, List<String>> fieldErrors) {
        this.fieldErrors = fieldErrors;
    }

    public Map<String, List<String>> getFieldErrors() {
        return fieldErrors;
    }
}
