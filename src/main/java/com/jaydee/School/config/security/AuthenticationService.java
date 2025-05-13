package com.jaydee.School.config.security;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jaydee.School.DTO.AuthenticationRequest;
import com.jaydee.School.DTO.AuthenticationResponse;
import com.jaydee.School.DTO.RefreshTokenRequest;
import com.jaydee.School.DTO.RegisterRequest;
import com.jaydee.School.Exception.CustomAuthenticationException;
import com.jaydee.School.config.jwt.JwtService;
import com.jaydee.School.entity.Role;
import com.jaydee.School.entity.User;
import com.jaydee.School.repository.RoleRepository;
import com.jaydee.School.repository.UserRepository;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(
        UserRepository userRepository,
        RoleRepository roleRepository,
        PasswordEncoder passwordEncoder,
        JwtService jwtService,
        @Lazy AuthenticationManager authenticationManager
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @Transactional
    public void logout(String token) {
        jwtService.invalidateToken(token);
    }

    @Transactional
    public AuthenticationResponse register(RegisterRequest request) {
        System.out.println("Registration attempt for email: " + request.getEmail());
        
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new CustomAuthenticationException("Email already exists");
        }

        // Convert RegisterRequest.UserRole to Role.RoleName enum
        Role.RoleName roleNameEnum;
        if (request.getRole() != null) {
            // Map the RegisterRequest.UserRole enum to the corresponding Role.RoleName enum
            switch (request.getRole()) {
                case ADMIN:
                    roleNameEnum = Role.RoleName.ADMIN;
                    break;
                case TEACHER:
                    roleNameEnum = Role.RoleName.TEACHER;
                    break;
                case PARENT:
                    roleNameEnum = Role.RoleName.PARENT;
                    break;
                case STUDENT:
                default:
                    roleNameEnum = Role.RoleName.STUDENT;
                    break;
            }
        } else {
            roleNameEnum = Role.RoleName.STUDENT;
        }
        
        System.out.println("Looking for role: " + roleNameEnum);
        
        Role role = roleRepository.findByName(roleNameEnum)
            .orElseThrow(() -> {
                System.out.println("Role not found: " + roleNameEnum);
                return new CustomAuthenticationException("Invalid role: " + roleNameEnum);
            });

        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());        user.setUsername(request.getEmail()); // Ensure username is set to email for consistency
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhoneNumber(request.getPhoneNumber());
        user.setRoles(Set.of(role));
        // Set the role string value for the direct column
        user.setRole(roleNameEnum.name());
        user.setIsActive(true);
        user.setIsEmailVerified(false);
        
        System.out.println("Saving user with email: " + user.getEmail());
        userRepository.save(user);
          String jwt = jwtService.generateToken(user);
        List<String> roles = user.getRoles().stream()
            .map(r -> r.getName().name())
            .collect(Collectors.toList());

        System.out.println("Generated token for user: " + user.getEmail());
        
        return AuthenticationResponse.builder()
            .token(jwt)
            .type("Bearer")
            .id(user.getId())
            .username(user.getUsername()) // Use the username field instead of email
            .email(user.getEmail())
            .roles(roles)
            .success(true)
            .message("User registered successfully")
            .expiresIn(jwtService.getExpirationTime())
            .build();
    }

    @Transactional
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try {
            // Verify credentials but don't store the authentication
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    request.getEmail(),
                    request.getPassword()
                )
            );

            User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new CustomAuthenticationException("User not found"));            String jwt = jwtService.generateToken(user);
            List<String> roles = user.getRoles().stream()
                .map(r -> r.getName().name())
                .collect(Collectors.toList());

            return AuthenticationResponse.builder()
                .token(jwt)
                .type("Bearer")
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .roles(roles)
                .success(true)
                .message("Authentication successful")
                .expiresIn(jwtService.getExpirationTime())
                .build();

        } catch (AuthenticationException e) {
            throw new CustomAuthenticationException("Invalid email or password");
        }
    }

    @Transactional
    public AuthenticationResponse refreshToken(RefreshTokenRequest request) {
        if (!jwtService.validateRefreshToken(request.getRefreshToken())) {
            throw new CustomAuthenticationException("Invalid refresh token");
        }

        String email = jwtService.extractUsername(request.getRefreshToken());
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new CustomAuthenticationException("User not found"));

        String jwt = jwtService.generateToken(user);
        List<String> roles = user.getRoles().stream()
            .map(r -> r.getName().name())
            .collect(Collectors.toList());        return AuthenticationResponse.builder()
            .token(jwt)
            .type("Bearer")
            .id(user.getId())
            .username(user.getUsername())
            .email(user.getEmail())
            .roles(roles)
            .success(true)
            .message("Token refreshed successfully")
            .expiresIn(jwtService.getExpirationTime())
            .build();
    }
}