package com.jaydee.School.config.security;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthenticationResponse {
    private String token;
    private String type;
    private Long id;
    private String username;
    private String email;
    private List<String> roles;
    private boolean success;
    private String message;
    private long expiresIn;
    private boolean passwordChangeRequired;
}

