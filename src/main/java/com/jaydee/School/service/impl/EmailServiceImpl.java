package com.jaydee.School.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.jaydee.School.service.EmailService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    
    @Value("${app.url:http://localhost:8080}")
    private String baseUrl;
    
    @Value("${spring.mail.username}")
    private String fromEmail;
    
    @Async
    @Override
    public void sendVerificationEmail(String to, String token) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject("School Management System - Email Verification");
            
            Context context = new Context();
            context.setVariable("verificationLink", baseUrl + "/api/auth/verify?token=" + token);
            context.setVariable("userName", to.split("@")[0]);
            
            String htmlContent = templateEngine.process("verification-email", context);
            helper.setText(htmlContent, true);
            
            mailSender.send(message);
            log.info("Verification email sent to: {}", to);
        } catch (MessagingException e) {
            log.error("Failed to send verification email to: {}", to, e);
        }
    }
    
    @Async
    @Override
    public void sendPasswordResetEmail(String to, String token) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject("School Management System - Password Reset Request");
            
            Context context = new Context();
            context.setVariable("resetLink", baseUrl + "/api/auth/reset-password?token=" + token);
            context.setVariable("userName", to.split("@")[0]);
            
            String htmlContent = templateEngine.process("password-reset-email", context);
            helper.setText(htmlContent, true);
            
            mailSender.send(message);
            log.info("Password reset email sent to: {}", to);
        } catch (MessagingException e) {
            log.error("Failed to send password reset email to: {}", to, e);
        }
    }
}