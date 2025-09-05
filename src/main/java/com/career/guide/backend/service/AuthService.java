package com.career.guide.backend.service;

import com.career.guide.backend.dto.auth.*;
import com.career.guide.backend.dto.user.UserResponse;
import com.career.guide.backend.entity.RefreshToken;
import com.career.guide.backend.entity.User;
import com.career.guide.backend.entity.enums.UserRole;
import com.career.guide.backend.repository.RefreshTokenRepository;
import com.career.guide.backend.repository.UserRepository;
import com.career.guide.backend.security.JwtService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Service
@Transactional
public class AuthService {

	private final UserRepository userRepository;
	private final RefreshTokenRepository refreshTokenRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	private final EmailService emailService;
	private final GoogleTokenVerificationService googleTokenVerificationService;
	private final ModelMapper modelMapper;

	public AuthService(UserRepository userRepository, RefreshTokenRepository refreshTokenRepository,
			PasswordEncoder passwordEncoder, JwtService jwtService, EmailService emailService,
			GoogleTokenVerificationService googleTokenVerificationService, ModelMapper modelMapper) {
		this.userRepository = userRepository;
		this.refreshTokenRepository = refreshTokenRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtService = jwtService;
		this.emailService = emailService;
		this.googleTokenVerificationService = googleTokenVerificationService;
		this.modelMapper = modelMapper;
	}

	public AuthResponse register(RegisterRequest request) {
		userRepository.findByEmail(request.getEmail()).ifPresent(u -> {
			throw new IllegalStateException("Email already registered");
		});
		User user = new User();
		user.setEmail(request.getEmail());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setFirstName(request.getFirstName());
		user.setLastName(request.getLastName());
		user.setRole(UserRole.STUDENT);
		user.setVerified(false);
		user.setVerificationToken(UUID.randomUUID().toString());
		userRepository.save(user);
		emailService.sendVerificationEmail(user.getEmail(), user.getVerificationToken());
		return buildAuthResponse(user);
	}

	public AuthResponse login(LoginRequest request) {
		User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));
		if (user.getPassword() == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
			throw new IllegalArgumentException("Invalid credentials");
		}
		return buildAuthResponse(user);
	}

	public AuthResponse googleLogin(String idToken) {
		try {
			GoogleIdToken.Payload payload = googleTokenVerificationService.verifyToken(idToken);
			String email = payload.getEmail();
			String googleId = payload.getSubject();
			String firstName = (String) payload.get("given_name");
			String lastName = (String) payload.get("family_name");
			String profilePicture = (String) payload.get("picture");

			User user = userRepository.findByEmail(email).orElseGet(() -> {
				User u = new User();
				u.setEmail(email);
				u.setGoogleId(googleId);
				u.setFirstName(firstName);
				u.setLastName(lastName);
				u.setProfilePicture(profilePicture);
				u.setVerified(true);
				u.setRole(UserRole.STUDENT);
				return userRepository.save(u);
			});
			if (user.getGoogleId() == null) {
				user.setGoogleId(googleId);
				user.setFirstName(firstName);
				user.setLastName(lastName);
				user.setProfilePicture(profilePicture);
			}
			return buildAuthResponse(user);
		} catch (Exception e) {
			throw new IllegalArgumentException("Invalid Google token");
		}
	}

	public void verifyEmail(VerifyEmailRequest request) {
		User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new IllegalArgumentException("Invalid email"));
		if (user.getVerificationToken() != null && user.getVerificationToken().equals(request.getVerificationToken())) {
			user.setVerified(true);
			user.setVerificationToken(null);
		} else {
			throw new IllegalArgumentException("Invalid token");
		}
	}

	public AuthResponse refresh(String refreshToken) {
		RefreshToken token = refreshTokenRepository.findByTokenAndIsRevokedFalse(refreshToken)
				.orElseThrow(() -> new IllegalArgumentException("Invalid refresh token"));
		if (token.getExpiresAt().isBefore(LocalDateTime.now())) {
			refreshTokenRepository.delete(token);
			throw new IllegalArgumentException("Refresh token expired");
		}
		User user = token.getUser();
		// Rotate refresh token
		refreshTokenRepository.delete(token);
		return buildAuthResponse(user);
	}

	public void forgotPassword(ForgotPasswordRequest request) {
		User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new IllegalArgumentException("Email not found"));
		String resetToken = UUID.randomUUID().toString();
		user.setVerificationToken(resetToken);
		userRepository.save(user);
		emailService.sendPasswordResetEmail(user.getEmail(), resetToken);
	}

	public void resetPassword(ResetPasswordRequest request) {
		User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new IllegalArgumentException("Email not found"));
		if (user.getVerificationToken() == null || !user.getVerificationToken().equals(request.getToken())) {
			throw new IllegalArgumentException("Invalid reset token");
		}
		user.setPassword(passwordEncoder.encode(request.getNewPassword()));
		user.setVerificationToken(null);
		userRepository.save(user);
	}

	public void logout(String refreshToken) {
		refreshTokenRepository.findByTokenAndIsRevokedFalse(refreshToken)
				.ifPresent(token -> {
					token.setIsRevoked(true);
					refreshTokenRepository.save(token);
				});
	}

	private AuthResponse buildAuthResponse(User user) {
		String token = jwtService.generateToken(user.getEmail(), Map.of("role", user.getRole().name()));
		String refreshToken = UUID.randomUUID().toString();
		
		// Create refresh token entity
		RefreshToken refreshTokenEntity = new RefreshToken();
		refreshTokenEntity.setUser(user);
		refreshTokenEntity.setToken(refreshToken);
		refreshTokenEntity.setExpiresAt(LocalDateTime.now().plusDays(7)); // 7 days
		refreshTokenRepository.save(refreshTokenEntity);
		
		UserResponse userResponse = modelMapper.map(user, UserResponse.class);
		return new AuthResponse(token, refreshToken, userResponse);
	}
}


