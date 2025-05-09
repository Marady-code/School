package com.jaydee.School.config.security;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jaydee.School.DTO.PasswordResetRequest;
import com.jaydee.School.DTO.RefreshTokenRequest;
import com.jaydee.School.DTO.RegisterRequest;
import com.jaydee.School.Exception.CustomAuthenticationException;
import com.jaydee.School.Exception.ResourceAlreadyExistsException;
import com.jaydee.School.Exception.ResourceNotFoundException;
import com.jaydee.School.config.jwt.JwtService;
import com.jaydee.School.config.jwt.TokenBlacklistService;
import com.jaydee.School.entity.User;
import com.jaydee.School.repository.RoleRepository;
import com.jaydee.School.repository.UserRepository;
import com.jaydee.School.service.EmailService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    private final TokenBlacklistService tokenBlacklistService;

    /**
     * Registers a new user with the specified details and role
     *
     * @param request The registration request with user details
     * @return Authentication response with JWT token and user info
     */
    @Transactional
    public AuthenticationResponse register(RegisterRequest request) {
        log.info("Starting user registration process for email: {}", request.getEmail());
        
        // Validate if the username already exists
        if (userRepository.existsByUsername(request.getUsername())) {
            log.warn("Username already taken: {}", request.getUsername());
            throw new ResourceAlreadyExistsException("User", "username", request.getUsername());
        }
        
        // Validate if the email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            log.warn("Email already registered: {}", request.getEmail());
            throw new ResourceAlreadyExistsException("User", "email", request.getEmail());
        }
        
        // Validate role based on the user's request
        Role userRole = validateAndGetRole(request.getRole());
        
        // Create the new user
        User user = new User();
        user.setUsername(request.getUsername());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhoneNumber(request.getPhoneNumber());
        user.setIsActive(true);
        user.setIsEmailVerified(false);
        user.setRoles(Collections.singleton(userRole));
        user.setRole(userRole.getName().name()); // Set the role string field
        user.setCreatedAt(LocalDateTime.now());

        // Save the user
        User savedUser = userRepository.save(user);
        log.info("User registered successfully with ID: {}", savedUser.getId());

        // Generate verification token and send email
        String token = generateVerificationToken(savedUser);
        emailService.sendVerificationEmail(savedUser.getEmail(), token);
        log.info("Verification email sent to: {}", savedUser.getEmail());
        
        // Generate JWT tokens
        String jwtToken = jwtService.generateToken(savedUser);
        String refreshToken = jwtService.generateRefreshToken(savedUser);

        return createAuthenticationResponse(savedUser, jwtToken, refreshToken, "User registered successfully");
    }

    /**
     * Authenticates a user with the provided credentials
     *
     * @param request The authentication request with credentials
     * @return Authentication response with JWT token and user info
     */
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        log.info("Attempting authentication for user: {}", request.getUsername());
        
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
            
            SecurityContextHolder.getContext().setAuthentication(authentication);
            User user = (User) authentication.getPrincipal();
            
            // Check if email is verified (optional - can be removed if email verification is not required)
            if (!user.getIsEmailVerified()) {
                log.warn("User {} attempted login with unverified email", user.getUsername());
                // Uncomment if you want to require email verification before login
                // throw new CustomAuthenticationException("Please verify your email before logging in");
            }
            
            // Update last login time
            user.setLastLogin(LocalDateTime.now());
            userRepository.save(user);
            
            String jwtToken = jwtService.generateToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);
            
            log.info("User {} authenticated successfully", user.getUsername());
            return createAuthenticationResponse(user, jwtToken, refreshToken, "Login successful");
            
        } catch (org.springframework.security.core.AuthenticationException e) {
            log.warn("Authentication failed for user {}: {}", request.getUsername(), e.getMessage());
            throw new CustomAuthenticationException("Invalid username/email or password");
        }
    }

    /**
     * Refreshes an expired JWT token using a valid refresh token
     *
     * @param request The refresh token request
     * @return Authentication response with new JWT token
     */
    public AuthenticationResponse refreshToken(RefreshTokenRequest request) {
        try {
            String refreshToken = request.getRefreshToken();
            String username = jwtService.extractUsername(refreshToken);
            
            if (username != null) {
                User user = userRepository.findByUsernameOrEmail(username, username)
                        .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
                
                if (jwtService.isTokenValid(refreshToken, user)) {
                    String newToken = jwtService.generateToken(user);
                    log.info("Token refreshed for user: {}", username);
                    return createAuthenticationResponse(user, newToken, refreshToken, "Token refreshed successfully");
                }
            }
            
            log.warn("Invalid refresh token");
            throw new CustomAuthenticationException("Invalid refresh token");
        } catch (Exception e) {
            log.error("Error refreshing token: {}", e.getMessage());
            throw new CustomAuthenticationException("Error refreshing token: " + e.getMessage());
        }
    }

    /**
     * Logs out a user by invalidating their JWT token
     *
     * @param token The JWT token to blacklist
     */
    public void logout(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            String jwt = token.substring(7);
            tokenBlacklistService.blacklistToken(jwt);
            log.info("User logged out, token blacklisted");
        }
    }

    /**
     * Verifies a user's email using the verification token
     *
     * @param token The verification token
     * @return true if verification is successful, false otherwise
     */
    @Transactional
    public boolean verifyEmail(String token) {
        try {
            // In a real implementation, you would store tokens in a database with the user ID
            // Here we're using a simple JWT-based verification
            String username = jwtService.extractUsername(token);
            
            if (username != null) {
                User user = userRepository.findByUsernameOrEmail(username, username)
                        .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
                
                if (!user.getIsEmailVerified()) {
                    user.setIsEmailVerified(true);
                    userRepository.save(user);
                    log.info("Email verified for user: {}", username);
                    return true;
                }
            }
            
            return false;
        } catch (Exception e) {
            log.error("Error verifying email: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Initiates password reset by sending a reset link to the user's email
     *
     * @param email The user's email address
     */
    public void requestPasswordReset(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        
        // Always return success even if user not found (security best practice)
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            String token = generatePasswordResetToken(user);
            emailService.sendPasswordResetEmail(email, token);
            log.info("Password reset email sent to: {}", email);
        } else {
            log.info("Password reset requested for non-existent email: {}", email);
        }
    }

    /**
     * Resets a user's password using a valid reset token
     *
     * @param token The password reset token
     * @param request The password reset request with new password
     */
    @Transactional
    public void resetPassword(String token, PasswordResetRequest request) {
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new CustomAuthenticationException("Passwords do not match");
        }
        
        try {
            String username = jwtService.extractUsername(token);
            
            if (username != null) {
                User user = userRepository.findByUsernameOrEmail(username, username)
                        .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
                
                user.setPassword(passwordEncoder.encode(request.getNewPassword()));
                userRepository.save(user);
                log.info("Password reset successful for user: {}", username);
            } else {
                throw new CustomAuthenticationException("Invalid or expired token");
            }
        } catch (Exception e) {
            log.error("Error resetting password: {}", e.getMessage());
            throw new CustomAuthenticationException("Error resetting password: " + e.getMessage());
        }
    }

    /**
     * Resends the verification email to the user
     *
     * @param email The user's email address
     */
    public void resendVerificationEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
        
        if (user.getIsEmailVerified()) {
            log.warn("Attempted to resend verification email to already verified user: {}", email);
            throw new CustomAuthenticationException("Email is already verified");
        }
        
        String token = generateVerificationToken(user);
        emailService.sendVerificationEmail(email, token);
        log.info("Verification email resent to: {}", email);
    }

    /**
     * Validates the role based on the current user's authorities and the requested role
     * Implements the business rules for role assignment
     *
     * @param requestedRole The requested role name
     * @return The Role entity for the validated role
     */
    private Role validateAndGetRole(String requestedRole) {
        // Get the current authenticated user (if any)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = null;
        
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            currentUser = (User) authentication.getPrincipal();
        }
        
        try {
            Role.RoleName roleName = Role.RoleName.valueOf(requestedRole.toUpperCase());
            
            // Apply business rules for role assignment
            if (roleName == Role.RoleName.SUPER_ADMIN) {
                // Only existing super admins can create new super admins
                if (currentUser == null || !currentUser.hasRole(Role.RoleName.SUPER_ADMIN)) {
                    log.warn("Unauthorized attempt to create SUPER_ADMIN account");
                    throw new CustomAuthenticationException("Only Super Admins can create other Super Admin accounts");
                }
            } else if (roleName == Role.RoleName.ADMIN) {
                // Only super admins can create admins
                if (currentUser == null || !currentUser.hasRole(Role.RoleName.SUPER_ADMIN)) {
                    log.warn("Unauthorized attempt to create ADMIN account");
                    throw new CustomAuthenticationException("Only Super Admins can create Admin accounts");
                }
            } else if (roleName == Role.RoleName.TEACHER || 
                      roleName == Role.RoleName.STUDENT || 
                      roleName == Role.RoleName.PARENT) {
                // Admins and super admins can create teachers, students, and parents
                if (currentUser == null || 
                    (!currentUser.hasRole(Role.RoleName.ADMIN) && 
                     !currentUser.hasRole(Role.RoleName.SUPER_ADMIN))) {
                    log.warn("Unauthorized attempt to create {} account", roleName);
                    throw new CustomAuthenticationException("Only Admins or Super Admins can create this type of account");
                }
            }
            
            // Get the role from the database
            return roleRepository.findByName(roleName)
                    .orElseThrow(() -> new ResourceNotFoundException("Role", "name", roleName.name()));
                    
        } catch (IllegalArgumentException e) {
            log.warn("Invalid role requested: {}", requestedRole);
            throw new CustomAuthenticationException("Invalid role: " + requestedRole);
        }
    }

    /**
     * Generates a verification token for email verification
     *
     * @param user The user to generate a token for
     * @return The verification token
     */
    private String generateVerificationToken(User user) {
        // In a production system, you might want to store these tokens in a database
        // Here we're using JWT for simplicity
        return jwtService.generateToken(
                Collections.singletonMap("tokenType", "emailVerification"),
                user,
                86400000 // 24 hours validity
        );
    }

    /**
     * Generates a password reset token
     *
     * @param user The user to generate a token for
     * @return The password reset token
     */
    private String generatePasswordResetToken(User user) {
        // In a production system, you might want to store these tokens in a database
        // Here we're using JWT for simplicity
        return jwtService.generateToken(
                Collections.singletonMap("tokenType", "passwordReset"),
                user,
                3600000 // 1 hour validity for security
        );
    }

    /**
     * Creates an authentication response with user details and tokens
     *
     * @param user The authenticated user
     * @param jwtToken The JWT access token
     * @param refreshToken The refresh token
     * @param message A message to include in the response
     * @return The complete authentication response
     */
    private AuthenticationResponse createAuthenticationResponse(User user, String jwtToken, String refreshToken, String message) {
        List<String> roles = user.getRoles().stream()
                .map(role -> "ROLE_" + role.getName().name())
                .collect(Collectors.toList());
        
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .refreshToken(refreshToken)
                .type("Bearer")
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .roles(roles)
                .isEmailVerified(user.getIsEmailVerified())
                .success(true)
                .message(message)
                .expiresIn(86400000) // 24 hours in milliseconds
                .build();
    }
}