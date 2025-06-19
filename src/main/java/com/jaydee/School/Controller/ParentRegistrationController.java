package com.jaydee.School.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jaydee.School.DTO.ParentRegistrationDTO;
import com.jaydee.School.DTO.UserResponse;
import com.jaydee.School.service.ParentRegistrationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth/parent")
@RequiredArgsConstructor
public class ParentRegistrationController {

    private final ParentRegistrationService parentRegistrationService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerParent(@Valid @RequestBody ParentRegistrationDTO registrationDTO) {
        UserResponse response = parentRegistrationService.registerParent(registrationDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/verify-student")
    public ResponseEntity<Boolean> verifyStudentPhoneNumber(@RequestParam String phoneNumber) {
        boolean exists = parentRegistrationService.verifyStudentPhoneNumber(phoneNumber);
        return ResponseEntity.ok(exists);
    }
} 