package com.career.guide.backend.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class GoogleOAuthRequest {
	@NotBlank
	private String googleId;
	@Email
	@NotBlank
	private String email;
	private String firstName;
	private String lastName;
	private String profilePicture;
}


