package com.jaydee.School.config.security;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthenticationResponseNoToken {
    private String type;
    private Long id;
    private String username;
    private String email;
    private List<String> roles;
    private boolean success;
    private String message;
    private long expiresIn;
    private boolean passwordChangeRequired;
    
    // Utility method to create from an AuthenticationResponse
    public static AuthenticationResponseNoToken fromAuthenticationResponse(AuthenticationResponse response) {
        return AuthenticationResponseNoToken.builder()
                .type(response.getType())
                .id(response.getId())
                .username(response.getUsername())
                .email(response.getEmail())
                .roles(response.getRoles())
                .success(response.isSuccess())
                .message(response.getMessage())
                .expiresIn(response.getExpiresIn())
                .passwordChangeRequired(response.isPasswordChangeRequired())
                .build();
    }
}



