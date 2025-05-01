//package com.jaydee.School.Controller;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.jaydee.School.DTO.AuthenticationRequest;
//import com.jaydee.School.DTO.AuthenticationResponse;
//import com.jaydee.School.DTO.RegisterRequest;
//import com.jaydee.School.security.AuthenticationService;
//
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import lombok.RequiredArgsConstructor;
//
//@RestController
//@RequestMapping("/api/auth")
//@RequiredArgsConstructor
//@Tag(name = "Authentication", description = "Authentication management APIs")
//public class AuthenticationController {
//
//    private final AuthenticationService authenticationService;
//
//    @PostMapping("/register")
//    @Operation(summary = "Register a new user", description = "Creates a new user account and returns a JWT token")
//    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
//        return ResponseEntity.ok(authenticationService.register(request));
//    }
//
//    @PostMapping("/login")
//    @Operation(summary = "Authenticate a user", description = "Authenticates a user and returns a JWT token")
//    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
//        return ResponseEntity.ok(authenticationService.authenticate(request));
//    }
//} 