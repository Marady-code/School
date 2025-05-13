package com.jaydee.School.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.jaydee.School.entity.Role;
import com.jaydee.School.entity.User;
import com.jaydee.School.repository.UserRepository;

import lombok.RequiredArgsConstructor;

/**
 * Service to handle user security checks for controller authorization
 */
@Service
@RequiredArgsConstructor
public class UserSecurityService {
    
    private final UserRepository userRepository;
    
    /**
     * Check if the authenticated user is the same user being accessed
     */
    public boolean isCurrentUser(Long userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal() == null) {
            return false;
        }
        
        if (authentication.getPrincipal() instanceof User) {
            User currentUser = (User) authentication.getPrincipal();
            return currentUser.getId().equals(userId);
        } else {
            User currentUser = getCurrentAuthenticatedUser();
            return currentUser != null && currentUser.getId().equals(userId);
        }
    }
    
    /**
     * Check if the user with the given ID is not an admin or super admin
     */
    public boolean isNonAdminUser(Long userId) {
        return userRepository.findById(userId)
                .map(user -> !hasAdminRole(user))
                .orElse(false);
    }
      /**
     * Check if a user has either ADMIN or SUPER_ADMIN role
     */
    private boolean hasAdminRole(User user) {
        return user.getRoles().stream()
                .anyMatch(role -> 
                    role.getName() == Role.RoleName.ADMIN || 
                    role.getName() == Role.RoleName.SUPER_ADMIN);
    }
    
    /**
     * Checks if the authenticated user has the SUPER_ADMIN role
     * 
     * @return true if the current user has the SUPER_ADMIN role
     */
    public boolean isSuperAdmin() {
        User currentUser = getCurrentAuthenticatedUser();
        if (currentUser == null) {
            return false;
        }
        
        return currentUser.getRoles().stream()
            .map(Role::getName)
            .anyMatch(roleName -> roleName == Role.RoleName.SUPER_ADMIN);
    }
    
    /**
     * Checks if the authenticated user has admin privileges (SUPER_ADMIN or ADMIN)
     * 
     * @return true if the current user has the SUPER_ADMIN or ADMIN role
     */
    public boolean hasAdminPrivileges() {
        User currentUser = getCurrentAuthenticatedUser();
        if (currentUser == null) {
            return false;
        }
        
        return hasAdminRole(currentUser);
    }
    
    /**
     * Gets the currently authenticated user
     * 
     * @return The authenticated user or null if not authenticated
     */
    public User getCurrentAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        
        if (authentication.getPrincipal() instanceof User) {
            return (User) authentication.getPrincipal();
        }
        
        // Get the username from the authentication
        String email = authentication.getName();
        
        // Retrieve the user by email
        return userRepository.findByEmail(email).orElse(null);
    }
}
