package com.conduit.service;

import com.conduit.openapi.model.UpdateUser;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UpdateUserValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return UpdateUser.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UpdateUser user = (UpdateUser) target;
        if (user.getUsername() == null &&
                user.getEmail() == null &&
                user.getPassword() == null &&
                !user.getBio().isPresent() &&
                !user.getImage().isPresent()) {
            errors.reject("updateUser.atLeastOneField", "At least one field must be present in this object");
        }
    }
}
