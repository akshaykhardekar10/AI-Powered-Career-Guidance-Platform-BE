package com.career.guide.backend.entity.progress;

import com.career.guide.backend.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "user_progress")
public class UserProgress {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@Column(name = "date")
	private LocalDate date;

	@Column(name = "activity_type")
	private String activityType;

	@Column(name = "activity_details", columnDefinition = "TEXT")
	private String activityDetails;

	@Column(name = "time_spent")
	private Integer timeSpent;

	@Column(name = "streak_day")
	private Integer streakDay;

	@Column(name = "points_earned")
	private Integer pointsEarned;

	@Column(name = "skill_category")
	private String skillCategory;

	@Column(name = "created_at")
	private LocalDateTime createdAt = LocalDateTime.now();
}


