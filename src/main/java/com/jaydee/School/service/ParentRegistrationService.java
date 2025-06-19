package com.jaydee.School.service;

import com.jaydee.School.DTO.ParentRegistrationDTO;
import com.jaydee.School.DTO.UserResponse;

public interface ParentRegistrationService {
    /**
     * Register a new parent account after verifying student relationship
     * @param registrationDTO The parent registration data including student verification
     * @return UserResponse containing the created parent account information
     * @throws CustomAuthenticationException if verification fails or email already exists
     */
    UserResponse registerParent(ParentRegistrationDTO registrationDTO);
    
    /**
     * Verify if a student exists with the given phone number
     * @param studentPhoneNumber The phone number to verify
     * @return true if student exists, false otherwise
     */
    boolean verifyStudentPhoneNumber(String studentPhoneNumber);
} 