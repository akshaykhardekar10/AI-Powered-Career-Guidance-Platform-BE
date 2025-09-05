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

	@Column(name = "preferred_location", columnDefinition = "TEXT")
	private String preferredLocation;

	@Column(name = "current_skills", columnDefinition = "TEXT")
	private String currentSkills;

	@Column(name = "skill_levels", columnDefinition = "TEXT")
	private String skillLevels;

	@Column(name = "learning_preferences", columnDefinition = "TEXT")
	private String learningPreferences;

	@Column(name = "daily_study_hours")
	private Integer dailyStudyHours;

	@Column(name = "preferred_study_time")
	private String preferredStudyTime;

	@Column(name = "weekend_availability")
	private Boolean weekendAvailability;

	@Column(name = "internship_experience", columnDefinition = "TEXT")
	private String internshipExperience;

	@Column(name = "project_experience", columnDefinition = "TEXT")
	private String projectExperience;

	@Column(name = "certifications", columnDefinition = "TEXT")
	private String certifications;

	@Column(name = "completed_at")
	private LocalDateTime completedAt;

	@Column(name = "is_completed")
	private Boolean isCompleted = false;
}


