package com.career.guide.backend.dto.roadmap;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeeklyPlanResponse {
	private Long id;
	private Integer weekNumber;
	private String title;
	private String description;
	private String learningGoals;
	private String dailyTasks;
	private String resources;
	private Double completionPercentage;
	private Integer videosWatched;
	private Integer totalVideos;
	private Integer tasksCompleted;
	private Integer totalTasks;
	private String startDate;
	private String endDate;
	private String status;
}


