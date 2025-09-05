package com.career.guide.backend.dto.roadmap;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoadmapUpdateRequest {
	private String title;
	private String description;
	private String targetRole;
	private Integer estimatedDuration;
	private String roadmapData;
	private String technicalSkills;
	private String softSkills;
	private String milestones;
	private String status;
}
