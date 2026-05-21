package com.conduit.service;

import com.conduit.mapper.UserMapper;
import com.conduit.openapi.model.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;

    public User getCurrentUser() {
        //Get something from Security Context
        com.conduit.entity.User user = (com.conduit.entity.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var userDTO =  userMapper.toUserDTO(user);
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader("Authorization").substring(6);
        userDTO.setToken(token);
        return userDTO;
    }

}
