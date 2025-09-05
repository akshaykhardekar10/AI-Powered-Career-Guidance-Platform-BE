package com.career.guide.backend.service;

import com.career.guide.backend.dto.roadmap.WeeklyPlanResponse;
import com.career.guide.backend.entity.User;
import com.career.guide.backend.entity.roadmap.PersonalizedRoadmap;
import com.career.guide.backend.entity.roadmap.WeeklyPlan;
import com.career.guide.backend.repository.PersonalizedRoadmapRepository;
import com.career.guide.backend.repository.WeeklyPlanRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class WeeklyPlanService {

	private final WeeklyPlanRepository weeklyPlanRepository;
	private final PersonalizedRoadmapRepository roadmapRepository;
	private final GeminiApiService geminiApiService;
	private final ModelMapper modelMapper;

	public WeeklyPlanService(WeeklyPlanRepository weeklyPlanRepository,
			PersonalizedRoadmapRepository roadmapRepository,
			GeminiApiService geminiApiService,
			ModelMapper modelMapper) {
		this.weeklyPlanRepository = weeklyPlanRepository;
		this.roadmapRepository = roadmapRepository;
		this.geminiApiService = geminiApiService;
		this.modelMapper = modelMapper;
	}

	public WeeklyPlan generateWeeklyPlan(User user, Long roadmapId, int weekNumber) {
		PersonalizedRoadmap roadmap = roadmapRepository.findById(roadmapId).orElse(null);
		if (roadmap == null) {
			return null;
		}

		String weeklyPlanData = geminiApiService.generateWeeklyPlan(roadmap.getRoadmapData(), weekNumber);
		
		WeeklyPlan plan = new WeeklyPlan();
		plan.setUser(user);
		plan.setRoadmap(roadmap);
		plan.setWeekNumber(weekNumber);
		plan.setTitle("Week " + weekNumber + " Plan");
		plan.setDescription("AI-generated weekly plan based on your roadmap");
		plan.setLearningGoals(extractLearningGoals(weeklyPlanData));
		plan.setDailyTasks(extractDailyTasks(weeklyPlanData));
		plan.setResources(extractResources(weeklyPlanData));
		plan.setStartDate(LocalDate.now().plusWeeks(weekNumber - 1));
		plan.setEndDate(LocalDate.now().plusWeeks(weekNumber));
		plan.setStatus(com.career.guide.backend.entity.enums.WeekStatus.NOT_STARTED);
		plan.setCreatedAt(java.time.LocalDateTime.now());
		
		return weeklyPlanRepository.save(plan);
	}

	public List<WeeklyPlan> getUserWeeklyPlans(User user) {
		return weeklyPlanRepository.findByUserOrderByWeekNumber(user);
	}

	public WeeklyPlan getWeeklyPlan(Long planId) {
		return weeklyPlanRepository.findById(planId).orElse(null);
	}

	public WeeklyPlan updateProgress(Long planId, double completionPercentage) {
		WeeklyPlan plan = weeklyPlanRepository.findById(planId).orElse(null);
		if (plan != null) {
			plan.setCompletionPercentage(completionPercentage);
			if (completionPercentage >= 100) {
				plan.setStatus(com.career.guide.backend.entity.enums.WeekStatus.COMPLETED);
			} else if (completionPercentage > 0) {
				plan.setStatus(com.career.guide.backend.entity.enums.WeekStatus.IN_PROGRESS);
			}
			return weeklyPlanRepository.save(plan);
		}
		return null;
	}

	public WeeklyPlan completeTask(Long planId) {
		WeeklyPlan plan = weeklyPlanRepository.findById(planId).orElse(null);
		if (plan != null) {
			plan.setTasksCompleted(plan.getTasksCompleted() + 1);
			double newPercentage = (double) plan.getTasksCompleted() / plan.getTotalTasks() * 100;
			plan.setCompletionPercentage(newPercentage);
			
			if (newPercentage >= 100) {
				plan.setStatus(com.career.guide.backend.entity.enums.WeekStatus.COMPLETED);
			} else if (newPercentage > 0) {
				plan.setStatus(com.career.guide.backend.entity.enums.WeekStatus.IN_PROGRESS);
			}
			
			return weeklyPlanRepository.save(plan);
		}
		return null;
	}

	public WeeklyPlan watchVideo(Long planId) {
		WeeklyPlan plan = weeklyPlanRepository.findById(planId).orElse(null);
		if (plan != null) {
			plan.setVideosWatched(plan.getVideosWatched() + 1);
			return weeklyPlanRepository.save(plan);
		}
		return null;
	}

	public WeeklyPlan getCurrentWeekPlan(User user) {
		return weeklyPlanRepository.findByUserAndStatus(user, com.career.guide.backend.entity.enums.WeekStatus.IN_PROGRESS)
				.stream().findFirst().orElse(null);
	}

	public WeeklyPlanResponse toWeeklyPlanResponse(WeeklyPlan plan) {
		return modelMapper.map(plan, WeeklyPlanResponse.class);
	}

	private String extractLearningGoals(String weeklyPlanData) {
		return "[\"Learn basic programming concepts\", \"Complete coding exercises\", \"Build a simple project\"]";
	}

	private String extractDailyTasks(String weeklyPlanData) {
		return "{\"Monday\": \"Study variables and data types\", \"Tuesday\": \"Practice loops and conditions\", \"Wednesday\": \"Work on functions\"}";
	}

	private String extractResources(String weeklyPlanData) {
		return "[\"https://example.com/video1\", \"https://example.com/article1\", \"https://example.com/practice1\"]";
	}
}
