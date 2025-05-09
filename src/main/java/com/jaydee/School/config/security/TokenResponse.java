package com.jaydee.School.config.security;

import com.jaydee.School.config.security.RoleEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenResponse {
    private String accessToken;
    private Long userId;
    private String email;
    private String fullName;
    private RoleEnum role;
}