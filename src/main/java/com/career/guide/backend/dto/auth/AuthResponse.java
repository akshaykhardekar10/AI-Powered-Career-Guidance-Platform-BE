package com.career.guide.backend.dto.auth;

import com.career.guide.backend.dto.user.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
	private String token;
	private String refreshToken;
	private UserResponse user;
}


