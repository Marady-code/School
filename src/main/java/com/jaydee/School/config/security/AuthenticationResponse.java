package com.jaydee.School.config.security;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    private String token;
    @Builder.Default
    private String type = "Bearer";
    private Long id;
    private String username;
    private String email;
    private List<String> roles;
    private String message;
    private boolean success;
    private Long expiresIn;
}