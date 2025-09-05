package com.career.guide.backend.dto.roadmap;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoadmapResponse {
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
	private List<RoadmapMilestone> milestonesArray;
}
