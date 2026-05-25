package com.conduit.service;

import com.conduit.mapper.UserMapper;
import com.conduit.openapi.model.UpdateUser;
import com.conduit.openapi.model.User;
import com.conduit.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User getCurrentUser() {
        //Get something from Security Context
        com.conduit.entity.User user = (com.conduit.entity.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var userDTO = userMapper.toUserDTO(user);
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader("Authorization").substring(6);
        userDTO.setToken(token);
        return userDTO;
    }

    public User updateUser(UpdateUser updateUser) {
        com.conduit.entity.User user = (com.conduit.entity.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (updateUser.getUsername() != null) user.setHandle(updateUser.getUsername());
        if (updateUser.getPassword() != null) user.setHashPassword(passwordEncoder.encode(updateUser.getPassword()));
        if (updateUser.getEmail() != null) user.setEmail(updateUser.getEmail());
        if (updateUser.getBio().isPresent()) user.setBio(updateUser.getBio().get());
        if (updateUser.getImage().isPresent()) user.setImageUrl(updateUser.getImage().get());

        var savedUser = userRepository.save(user);
        var userDTO = userMapper.toUserDTO(savedUser);
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader("Authorization").substring(6);
        userDTO.setToken(token);
        return userDTO;
    }

}
