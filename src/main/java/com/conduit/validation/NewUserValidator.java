package com.conduit.validation;

import com.conduit.openapi.model.NewUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class NewUserValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return NewUser.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        NewUser newUser = (NewUser) target;
        if (StringUtils.isBlank(newUser.getEmail())) {
            errors.rejectValue("email", "field.required", "can't be blank");
        }
        if (StringUtils.isBlank(newUser.getUsername())) {
            errors.rejectValue("username", "field.required", "can't be blank");
        }
        if (StringUtils.isBlank(newUser.getPassword())) {
            errors.rejectValue("password", "field.required", "can't be blank");
        }
        if(StringUtils.isNotBlank(newUser.getPassword()) && newUser.getPassword().length()<8){
            errors.rejectValue("password","field.required", "should be greater than 8 characters");
        }

    }
}
