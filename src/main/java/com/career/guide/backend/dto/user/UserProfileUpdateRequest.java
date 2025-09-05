package com.career.guide.backend.dto.user;

import lombok.Data;

import java.util.List;

@Data
public class UserProfileUpdateRequest {
	private String bio;
	private String location;
	private String phone;
	private String linkedinUrl;
	private String githubUrl;
	private String portfolioUrl;
	private List<String> skills;
	private List<String> achievements;
}


