package com.career.guide.backend.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileResponse {
	private String bio;
	private String location;
	private String phone;
	private String linkedinUrl;
	private String githubUrl;
	private String portfolioUrl;
	private Integer problemsSolved;
	private Integer easyProblems;
	private Integer mediumProblems;
	private Integer hardProblems;
	private Integer contestRating;
	private String rank;
	private Integer streak;
	private Integer maxStreak;
	private List<String> skills;
	private List<String> achievements;
	private Double totalScore;
}


