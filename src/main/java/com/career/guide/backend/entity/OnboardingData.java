package com.career.guide.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "onboarding_data")
public class OnboardingData {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne
	@JoinColumn(name = "user_id")
	private User user;

	@Column(name = "college_name")
	private String collegeName;

	@Column(name = "degree")
	private String degree;

	@Column(name = "branch")
	private String branch;

	@Column(name = "current_year")
	private Integer currentYear;

	@Column(name = "current_cgpa")
	private Double currentCgpa;

	@Column(name = "career_goal")
	private String careerGoal;

	@Column(name = "target_companies", columnDefinition = "TEXT")
	private String targetCompanies;

	@Column(name = "preferred_roles", columnDefinition = "TEXT")
	private String preferredRoles;

	@Column(name = "target_salary")
	private Double targetSalary;

	@Column(name = "current_skills", columnDefinition = "TEXT")
	private String currentSkills;

	@Column(name = "skill_levels", columnDefinition = "TEXT")
	private String skillLevels;

	@Column(name = "daily_study_hours")
	private Integer dailyStudyHours;

	@Column(name = "primary_programming_language")
	private String primaryProgrammingLanguage;

	@Column(name = "programming_expertise_level")
	private String programmingExpertiseLevel;

	@Column(name = "completed_at")
	private LocalDateTime completedAt;

	@Column(name = "is_completed")
	private Boolean isCompleted = false;
}


