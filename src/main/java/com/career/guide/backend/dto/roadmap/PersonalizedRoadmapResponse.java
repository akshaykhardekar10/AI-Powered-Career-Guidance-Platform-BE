package com.career.guide.backend.dto.roadmap;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonalizedRoadmapResponse {
	private Long id;
	private String title;
	private String description;
	private String targetRole;
	private Integer estimatedDuration;
	private String roadmapData;
	private String technicalSkills;
	private String softSkills;
	private String milestones;
	private String status;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
