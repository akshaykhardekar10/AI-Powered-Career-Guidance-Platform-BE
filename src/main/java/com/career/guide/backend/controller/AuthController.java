package com.career.guide.backend.controller;

import com.career.guide.backend.dto.auth.*;
import com.career.guide.backend.dto.common.ApiResponse;
import com.career.guide.backend.service.AuthService;
import com.career.guide.backend.util.ResponseHelper;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	private final AuthService authService;

	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	@PostMapping("/register")
	public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest request) {
		return ResponseHelper.created("User registered successfully", authService.register(request));
	}

	@PostMapping("/login")
	public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
		return ResponseHelper.success("Login successful", authService.login(request));
	}

	@PostMapping("/oauth2/google")
	public ResponseEntity<ApiResponse<AuthResponse>> google(@RequestBody Map<String, String> request) {
		String idToken = request.get("idToken");
		if (idToken == null) {
			throw new IllegalArgumentException("idToken is required");
		}
		return ResponseHelper.success("Google login successful", authService.googleLogin(idToken));
	}

	@PostMapping("/verify-email")
	public ResponseEntity<ApiResponse<Object>> verify(@Valid @RequestBody VerifyEmailRequest request) {
		authService.verifyEmail(request);
		return ResponseHelper.success("Email verified successfully");
	}

	@PostMapping("/refresh-token")
	public ResponseEntity<ApiResponse<AuthResponse>> refresh(@RequestBody Map<String, String> request) {
		String refreshToken = request.get("refreshToken");
		if (refreshToken == null) {
			throw new IllegalArgumentException("refreshToken is required");
		}
		return ResponseHelper.success("Token refreshed successfully", authService.refresh(refreshToken));
	}

	@PostMapping("/forgot-password")
	public ResponseEntity<ApiResponse<Object>> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
		authService.forgotPassword(request);
		return ResponseHelper.success("Password reset email sent successfully");
	}

	@PostMapping("/reset-password")
	public ResponseEntity<ApiResponse<Object>> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
		authService.resetPassword(request);
		return ResponseHelper.success("Password reset successfully");
	}

	@PostMapping("/logout")
	public ResponseEntity<ApiResponse<Object>> logout(@RequestBody Map<String, String> request) {
		String refreshToken = request.get("refreshToken");
		if (refreshToken == null) {
			throw new IllegalArgumentException("refreshToken is required");
		}
		authService.logout(refreshToken);
		return ResponseHelper.success("Logout successful");
	}
}


