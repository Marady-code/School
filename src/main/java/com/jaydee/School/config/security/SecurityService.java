//package com.jaydee.School.config.security;
//
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Service;
//
//@Service
//public class SecurityService {
//
//    /**
//     * Checks if the currently authenticated user is the user with the given ID
//     * @param userId The user ID to check against
//     * @return true if the current user matches the given ID, false otherwise
//     */
////    public boolean isCurrentUser(Long userId) {
////        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
////        if (authentication == null || !authentication.isAuthenticated()) {
////            return false;
////        }
////
////        Object principal = authentication.getPrincipal();
////        if (principal instanceof AuthUser) {
////            return ((AuthUser) principal).getId().equals(userId);
////        }
////        return false;
////    }
//
//    /**
//     * Checks if the current user has a specific role
//     * @param role The role to check for (without the "ROLE_" prefix)
//     * @return true if the user has the role, false otherwise
//     */
//    public boolean hasRole(String role) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        return authentication != null && authentication.getAuthorities().stream()
//                .anyMatch(a -> a.getAuthority().equals("ROLE_" + role));
//    }
//    
//    /**
//     * Checks if the current user has a specific permission
//     * @param permission The permission to check for (e.g., "student:read")
//     * @return true if the user has the permission, false otherwise
//     */
//    public boolean hasPermission(String permission) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        return authentication != null && authentication.getAuthorities().stream()
//                .anyMatch(a -> a.getAuthority().equals(permission));
//    }
//    
//    /**
//     * Gets the role of the currently authenticated user
//     * @return The role enum of the current user, or null if not available
//     */
////    public RoleEnum getCurrentUserRole() {
////        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
////        if (authentication != null && authentication.getPrincipal() instanceof AuthUser) {
////            return ((AuthUser) authentication.getPrincipal()).getRole();
////        }
////        return null;
////    }
//    
//    /**
//     * Gets the ID of the currently authenticated user
//     * @return The user ID, or null if not available
//     */
////    public Long getCurrentUserId() {
////        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
////        if (authentication != null && authentication.getPrincipal() instanceof AuthUser) {
////            return ((AuthUser) authentication.getPrincipal()).getId();
////        }
////        return null;
////    }
//}
//
//
