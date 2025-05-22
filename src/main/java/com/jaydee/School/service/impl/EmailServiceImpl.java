package com.jaydee.School.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.jaydee.School.service.EmailService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

	private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);
	
	private final JavaMailSender mailSender;
	
	@Value("${app.email.enabled:false}")
	private boolean emailEnabled;

	/**
	 * Common method to send emails with error handling and logging
	 */
	private void sendEmail(String to, String subject, String text) {
		if (!emailEnabled) {
			logger.info("Email sending is disabled. Would have sent email to: {}", to);
			logger.info("Subject: {}", subject);
			logger.info("Body: {}", text);
			return;
		}
		
		try {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setTo(to);
			message.setSubject(subject);
			message.setText(text);
			mailSender.send(message);
			logger.info("Email sent successfully to: {}", to);
		} catch (MailException e) {
			logger.error("Failed to send email to {}: {}", to, e.getMessage());
			// Log the error but don't throw - don't break the application flow
		}
	}

	@Override
	public void sendPasswordResetEmail(String to, String newPassword) {
		String subject = "Password Reset";
		String text = "Your new password is: " + newPassword + "\n\nPlease change your password after logging in.";
		sendEmail(to, subject, text);
	}

	@Override
	public void sendWelcomeEmail(String to, String firstName) {
		String subject = "Welcome to School Management System";
		String text = "Dear " + firstName + ",\n\nWelcome to our School Management System. We're glad to have you on board!";
		sendEmail(to, subject, text);
	}

	@Override
	public void sendEmailVerificationEmail(String to, String token) {
		String subject = "Email Verification";
		String text = "Please click the following link to verify your email: "
				+ "http://localhost:8080/api/auth/verify-email?token=" + token;
		sendEmail(to, subject, text);
	}
}