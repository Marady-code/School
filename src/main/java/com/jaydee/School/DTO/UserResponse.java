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
    private Set<Role> roles;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // For password reset operations - only used transiently, not serialized to client
    private transient String plainPassword;
    
    /**
     * Convert this DTO to a User entity
     * Note: This doesn't copy sensitive fields like password
     */
    public com.jaydee.School.entity.User toEntity() {
        com.jaydee.School.entity.User user = new com.jaydee.School.entity.User();
        user.setId(id);
        user.setUsername(username);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPhoneNumber(phoneNumber);
        user.setIsActive(isActive);
        user.setRoles(roles);
        // Don't set password or other sensitive fields
        return user;
    }
}