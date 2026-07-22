package com.conduit.service;

import com.conduit.mapper.UserMapper;
import com.conduit.openapi.model.UpdateUser;
import com.conduit.openapi.model.User;
import com.conduit.repository.UserRepository;
import io.micrometer.common.util.StringUtils;
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
    private final JwtService jwtService;

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
        if (updateUser.getBio().isPresent()) {
            String bio = updateUser.getBio().get();
            bio = (bio == null || StringUtils.isBlank(bio)) ? null : bio;
            user.setBio(bio);
        }
        if (updateUser.getImage().isPresent()) {
            String image = updateUser.getImage().get();
            image = (image == null || StringUtils.isBlank(image)) ? null : image;
            user.setImageUrl(image);
        }
        var savedUser = userRepository.save(user);
        var userDTO = userMapper.toUserDTO(savedUser);
        String newToken = jwtService.generateToken(user);
        userDTO.setToken(newToken);
        return userDTO;
    }

}
