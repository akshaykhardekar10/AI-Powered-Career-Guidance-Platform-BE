package com.career.guide.backend.util;

import com.career.guide.backend.dto.onboarding.OnboardingResponse;
import com.career.guide.backend.dto.roadmap.PersonalizedRoadmapResponse;
import com.career.guide.backend.entity.OnboardingData;
import com.career.guide.backend.entity.roadmap.PersonalizedRoadmap;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DtoMapper {

	public OnboardingResponse toOnboardingResponse(OnboardingData entity) {
		if (entity == null) {
			return null;
		}
		
		return OnboardingResponse.builder()
				.id(entity.getId())
				.collegeName(entity.getCollegeName())
				.degree(entity.getDegree())
				.branch(entity.getBranch())
				.currentYear(entity.getCurrentYear())
				.currentCgpa(entity.getCurrentCgpa())
				.careerGoal(entity.getCareerGoal())
				.targetCompanies(entity.getTargetCompanies())
				.preferredRoles(entity.getPreferredRoles())
				.targetSalary(entity.getTargetSalary())
				.preferredLocation(entity.getPreferredLocation())
				.currentSkills(entity.getCurrentSkills())
				.skillLevels(entity.getSkillLevels())
				.learningPreferences(entity.getLearningPreferences())
				.dailyStudyHours(entity.getDailyStudyHours())
				.preferredStudyTime(entity.getPreferredStudyTime())
				.weekendAvailability(entity.getWeekendAvailability())
				.internshipExperience(entity.getInternshipExperience())
				.projectExperience(entity.getProjectExperience())
				.certifications(entity.getCertifications())
				.completedAt(entity.getCompletedAt())
				.isCompleted(entity.getIsCompleted())
				.build();
	}

	public PersonalizedRoadmapResponse toPersonalizedRoadmapResponse(PersonalizedRoadmap entity) {
		if (entity == null) {
			return null;
		}
		
		return PersonalizedRoadmapResponse.builder()
				.id(entity.getId())
				.title(entity.getTitle())
				.description(entity.getDescription())
				.targetRole(entity.getTargetRole())
				.estimatedDuration(entity.getEstimatedDuration())
				.roadmapData(entity.getRoadmapData())
				.technicalSkills(entity.getTechnicalSkills())
				.softSkills(entity.getSoftSkills())
				.milestones(entity.getMilestones())
				.status(entity.getStatus() != null ? entity.getStatus().toString() : null)
				.createdAt(entity.getCreatedAt())
				.updatedAt(entity.getUpdatedAt())
				.build();
	}

	public List<PersonalizedRoadmapResponse> toPersonalizedRoadmapResponseList(List<PersonalizedRoadmap> entities) {
		if (entities == null) {
			return null;
		}
		
		return entities.stream()
				.map(this::toPersonalizedRoadmapResponse)
				.collect(Collectors.toList());
	}
}
