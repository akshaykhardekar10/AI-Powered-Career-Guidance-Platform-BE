package com.career.guide.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

	private final JavaMailSender mailSender;
	private final String fromEmail;

	public EmailService(JavaMailSender mailSender, @Value("${app.mail.from}") String fromEmail) {
		this.mailSender = mailSender;
		this.fromEmail = fromEmail;
	}

	public void sendVerificationEmail(String email, String token) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(email);
		message.setFrom(fromEmail);
		message.setSubject("Verify Your Email - Career Guidance Platform");
		message.setText("Please click the link to verify your email: http://localhost:3000/verify-email?token=" + token);
		mailSender.send(message);
	}

	public void sendPasswordResetEmail(String email, String token) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(email);
		message.setFrom(fromEmail);
		message.setSubject("Reset Your Password - Career Guidance Platform");
		message.setText("Please click the link to reset your password: http://localhost:3000/reset-password?token=" + token);
		mailSender.send(message);
	}
}


