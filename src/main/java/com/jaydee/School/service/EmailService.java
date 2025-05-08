package com.jaydee.School.service;

public interface EmailService {
    void sendPasswordResetEmail(String to, String newPassword);
    void sendWelcomeEmail(String to, String firstName);
    void sendEmailVerificationEmail(String to, String token);
} 