package com.jaydee.School.DTO;

import java.time.LocalDateTime;
import java.util.Set;

import com.jaydee.School.entity.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private Boolean isActive;
    private Boolean isEmailVerified;
    private LocalDateTime lastLogin;
    private Set<Role> roles;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}