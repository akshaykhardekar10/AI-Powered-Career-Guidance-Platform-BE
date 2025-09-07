package com.career.guide.backend.dto.onboarding;

import lombok.Data;

@Data
public class OnboardingRequest {
	private String collegeName;
	private String degree;
	private String branch;
	private Integer currentYear;
	private Double currentCgpa;
	private String careerGoal;
	private String targetCompanies;
	private String preferredRoles;
	private Double targetSalary;
	private String currentSkills;
	private String skillLevels;
	private Integer dailyStudyHours;
	private String primaryProgrammingLanguage;
	private String programmingExpertiseLevel;
}
