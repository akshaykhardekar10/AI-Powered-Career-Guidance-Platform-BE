package com.career.guide.backend.dto.onboarding;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OnboardingResponse {
	private Long id;
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
	private LocalDateTime completedAt;
	private Boolean isCompleted;
}
