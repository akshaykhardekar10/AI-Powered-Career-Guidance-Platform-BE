package com.career.guide.backend.dto.user;

import com.career.guide.backend.entity.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
	private Long id;
	private String email;
	private String firstName;
	private String lastName;
	private String profilePicture;
	private Boolean verified;
	private UserRole role;
	private Boolean onboardingCompleted;
	private Boolean quizCompleted;
}
