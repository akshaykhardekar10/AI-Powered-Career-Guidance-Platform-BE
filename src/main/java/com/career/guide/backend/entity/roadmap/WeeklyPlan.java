package com.career.guide.backend.entity.roadmap;

import com.career.guide.backend.entity.User;
import com.career.guide.backend.entity.enums.WeekStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "weekly_plans")
public class WeeklyPlan {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne
	@JoinColumn(name = "roadmap_id")
	private PersonalizedRoadmap roadmap;

	@Column(name = "week_number")
	private Integer weekNumber;

	private String title;

	@Column(columnDefinition = "TEXT")
	private String description;

	@Column(name = "learning_goals", columnDefinition = "TEXT")
	private String learningGoals;

	@Column(name = "daily_tasks", columnDefinition = "TEXT")
	private String dailyTasks;

	@Column(name = "resources", columnDefinition = "TEXT")
	private String resources;

	@Column(name = "completion_percentage")
	private Double completionPercentage = 0.0;

	@Column(name = "videos_watched")
	private Integer videosWatched = 0;

	@Column(name = "total_videos")
	private Integer totalVideos = 0;

	@Column(name = "tasks_completed")
	private Integer tasksCompleted = 0;

	@Column(name = "total_tasks")
	private Integer totalTasks = 0;

	@Column(name = "start_date")
	private LocalDate startDate;

	@Column(name = "end_date")
	private LocalDate endDate;

	@Enumerated(EnumType.STRING)
	private WeekStatus status = WeekStatus.NOT_STARTED;

	@Column(name = "created_at")
	private LocalDateTime createdAt = LocalDateTime.now();
}


