package com.conduit.validation;

import com.conduit.openapi.model.UpdateUser;
import org.apache.commons.lang3.StringUtils;
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
        if (user.getEmail() == null && user.getPassword() == null && user.getUsername() == null && !user.getBio().isPresent() && !user.getImage().isPresent()) {
            errors.reject("field.required", "At least one field must be provided");
        }
        if (user.getEmail() != null && StringUtils.isBlank(user.getEmail())) {
            errors.rejectValue("email", "field.required", "can't be blank");
        }
        if (user.getUsername() != null && StringUtils.isBlank(user.getUsername())) {
            errors.rejectValue("username", "field.required", "can't be blank");
        }
        if (user.getPassword() != null && StringUtils.isBlank(user.getPassword())) {
            errors.rejectValue("password", "field.required", "can't be blank");
        }
        if(StringUtils.isNotBlank(user.getPassword()) && user.getPassword().length()<8 ){
            errors.rejectValue("password","field.required","should be greater than 8 characters");
        }
    }
}
