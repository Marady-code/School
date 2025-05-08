package com.jaydee.School.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jaydee.School.DTO.AuthenticationRequest;
import com.jaydee.School.DTO.AuthenticationResponse;
import com.jaydee.School.DTO.RefreshTokenRequest;
import com.jaydee.School.DTO.RegisterRequest;
import com.jaydee.School.config.security.AuthenticationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
//@CrossOrigin(origins = "*", maxAge = 3600)
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
}