package com.jaydee.School.Controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jaydee.School.DTO.PasswordResetRequest;
import com.jaydee.School.DTO.RefreshTokenRequest;
import com.jaydee.School.DTO.RegisterRequest;
import com.jaydee.School.config.security.AuthenticationRequest;
import com.jaydee.School.config.security.AuthenticationResponse;
import com.jaydee.School.config.security.AuthenticationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(
            @Valid @RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @PostMapping("/register") 
    public ResponseEntity<AuthenticationResponse> register(
            @Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthenticationResponse> refreshToken(
            @Valid @RequestBody RefreshTokenRequest request) {
        return ResponseEntity.ok(authenticationService.refreshToken(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String token) {
        authenticationService.logout(token);
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/verify")
    public ResponseEntity<Map<String, Object>> verifyEmail(@RequestParam String token) {
        boolean verified = authenticationService.verifyEmail(token);
        Map<String, Object> response = new HashMap<>();
        
        if (verified) {
            response.put("success", true);
            response.put("message", "Email verified successfully! You can now login.");
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "Email verification failed.");
            return ResponseEntity.badRequest().body(response);
        }
    }
    
    @PostMapping("/forgot-password")
    public ResponseEntity<Map<String, Object>> forgotPassword(@RequestParam String email) {
        authenticationService.requestPasswordReset(email);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "If the email exists in our system, a password reset link has been sent.");
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, Object>> resetPassword(
            @RequestParam String token,
            @Valid @RequestBody PasswordResetRequest request) {
        authenticationService.resetPassword(token, request);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Password has been reset successfully. You can now login with your new password.");
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/resend-verification")
    public ResponseEntity<Map<String, Object>> resendVerification(@RequestParam String email) {
        authenticationService.resendVerificationEmail(email);
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Verification email has been sent again. Please check your inbox.");
        return ResponseEntity.ok(response);
    }
}