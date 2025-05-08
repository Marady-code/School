package com.jaydee.School.service.impl;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.jaydee.School.service.EmailService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

	private final JavaMailSender mailSender;

	@Override
	public void sendPasswordResetEmail(String to, String newPassword) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(to);
		message.setSubject("Password Reset");
		message.setText("Your new password is: " + newPassword + "\n\nPlease change your password after logging in.");
		mailSender.send(message);
	}

	@Override
	public void sendWelcomeEmail(String to, String firstName) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(to);
		message.setSubject("Welcome to School Management System");
		message.setText(
				"Dear " + firstName + ",\n\nWelcome to our School Management System. We're glad to have you on board!");
		mailSender.send(message);
	}

	@Override
	public void sendEmailVerificationEmail(String to, String token) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(to);
		message.setSubject("Email Verification");
		message.setText("Please click the following link to verify your email: "
				+ "http://localhost:8080/api/auth/verify-email?token=" + token);
		mailSender.send(message);
	}
}