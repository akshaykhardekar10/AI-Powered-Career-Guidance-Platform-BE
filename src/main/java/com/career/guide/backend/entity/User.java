package com.career.guide.backend.entity;

import com.career.guide.backend.entity.enums.UserRole;
import com.career.guide.backend.entity.insights.IndustryInsight;
import com.career.guide.backend.entity.progress.UserProgress;
import com.career.guide.backend.entity.quiz.QuizResult;
import com.career.guide.backend.entity.roadmap.PersonalizedRoadmap;
import com.career.guide.backend.entity.roadmap.WeeklyPlan;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true, nullable = false)
	private String email;

	private String password;

	@Column(name = "google_id")
	private String googleId;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "profile_picture")
	private String profilePicture;

	private Boolean verified = false;

	@Column(name = "verification_token")
	private String verificationToken;

	@Enumerated(EnumType.STRING)
	private UserRole role = UserRole.STUDENT;

	@Column(name = "created_at")
	private LocalDateTime createdAt = LocalDateTime.now();

	@Column(name = "last_login")
	private LocalDateTime lastLogin;

	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
	private UserProfile userProfile;

	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
	private OnboardingData onboardingData;

	@Column(name = "onboarding_completed")
	private Boolean onboardingCompleted = false;

	@Column(name = "quiz_completed")
	private Boolean quizCompleted = false;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<QuizResult> quizResults;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<PersonalizedRoadmap> roadmaps;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<WeeklyPlan> weeklyPlans;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<UserProgress> progressRecords;

	@ManyToOne
	@JoinColumn(name = "industry_id")
	private IndustryInsight industry;
}


