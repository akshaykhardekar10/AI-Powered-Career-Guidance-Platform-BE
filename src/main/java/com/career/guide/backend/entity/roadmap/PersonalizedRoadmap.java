package com.career.guide.backend.entity.roadmap;

import com.career.guide.backend.entity.User;
import com.career.guide.backend.entity.enums.RoadmapStatus;
import com.career.guide.backend.dto.roadmap.RoadmapMilestone;
import com.career.guide.backend.entity.converter.RoadmapMilestoneListConverter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "personalized_roadmaps")
public class PersonalizedRoadmap {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	private String title;

	@Column(columnDefinition = "TEXT")
	private String description;

	@Column(name = "target_role")
	private String targetRole;

	@Column(name = "estimated_duration")
	private Integer estimatedDuration;

	@Column(name = "roadmap_data", columnDefinition = "TEXT")
	private String roadmapData;

	@Column(name = "technical_skills", columnDefinition = "TEXT")
	private String technicalSkills;

	@Column(name = "soft_skills", columnDefinition = "TEXT")
	private String softSkills;

	@Column(name = "milestones", columnDefinition = "TEXT")
	private String milestones;

	// Store parsed milestones array to avoid recomputation on reads
	@Column(name = "milestones_array", columnDefinition = "TEXT")
	@Convert(converter = RoadmapMilestoneListConverter.class)
	private List<RoadmapMilestone> milestonesArray;

	@Enumerated(EnumType.STRING)
	private RoadmapStatus status = RoadmapStatus.ACTIVE;

	@Column(name = "created_at")
	private LocalDateTime createdAt = LocalDateTime.now();

	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	@OneToMany(mappedBy = "roadmap", cascade = CascadeType.ALL)
	private List<WeeklyPlan> weeklyPlans;
}
