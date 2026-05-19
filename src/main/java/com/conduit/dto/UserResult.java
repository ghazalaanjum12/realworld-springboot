package com.conduit.dto;

import com.conduit.entity.User;

public record UserResult(User user, String token) {}
