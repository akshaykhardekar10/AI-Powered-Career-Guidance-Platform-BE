package com.career.guide.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "user_profiles")
public class UserProfile {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne
	@JoinColumn(name = "user_id")
	private User user;

	private String bio;
	private String location;
	private String phone;
	private String linkedinUrl;
	private String githubUrl;
	private String portfolioUrl;

	private Integer problemsSolved = 0;
	private Integer easyProblems = 0;
	private Integer mediumProblems = 0;
	private Integer hardProblems = 0;
	private Integer contestRating = 0;
	private String rank;
	private Integer streak = 0;
	private Integer maxStreak = 0;

	@ElementCollection
	@CollectionTable(name = "user_skills")
	private List<String> skills;

	@ElementCollection
	@CollectionTable(name = "user_achievements")
	private List<String> achievements;

	@Column(name = "total_score")
	private Double totalScore = 0.0;

	@Column(name = "updated_at")
	private LocalDateTime updatedAt = LocalDateTime.now();
}


