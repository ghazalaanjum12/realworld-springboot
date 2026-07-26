package com.conduit.validation;

import com.conduit.openapi.model.LoginUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class LoginUserValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return LoginUser.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        LoginUser user = (LoginUser) target;
        if(StringUtils.isBlank(user.getEmail())){
            errors.rejectValue("email", "field.required", "can't be blank");
        }
        if(StringUtils.isBlank(user.getPassword())){
            errors.rejectValue("password", "field.required", "can't be blank");
        }

    }
}
