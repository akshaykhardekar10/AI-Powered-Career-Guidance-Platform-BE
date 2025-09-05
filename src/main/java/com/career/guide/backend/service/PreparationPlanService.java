package com.career.guide.backend.service;

import com.career.guide.backend.dto.roadmap.WeeklyPreparationWeek;
import com.career.guide.backend.entity.User;
import com.career.guide.backend.entity.roadmap.PersonalizedRoadmap;
import com.career.guide.backend.entity.roadmap.PreparationPlan;
import com.career.guide.backend.repository.PersonalizedRoadmapRepository;
import com.career.guide.backend.repository.PreparationPlanRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@Transactional
public class PreparationPlanService {

    private final PreparationPlanRepository preparationPlanRepository;
    private final PersonalizedRoadmapRepository roadmapRepository;
    private final GeminiApiService geminiApiService;
    private final RoadmapService roadmapService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public PreparationPlanService(PreparationPlanRepository preparationPlanRepository,
                                  PersonalizedRoadmapRepository roadmapRepository,
                                  GeminiApiService geminiApiService,
                                  RoadmapService roadmapService) {
        this.preparationPlanRepository = preparationPlanRepository;
        this.roadmapRepository = roadmapRepository;
        this.geminiApiService = geminiApiService;
        this.roadmapService = roadmapService;
    }

    public PreparationPlan getOrGenerate(User user) {
        // Return existing plan if present (single plan per user)
        PreparationPlan existing = preparationPlanRepository.findByUser(user).orElse(null);
        if (existing != null) return existing;

        // Find first created roadmap for the user
        PersonalizedRoadmap roadmap = roadmapRepository.findFirstByUserOrderByCreatedAtAsc(user).orElse(null);
        // If none exists, auto-generate a roadmap now
        if (roadmap == null) {
            roadmapService.generateRoadmap(user);
            roadmap = roadmapRepository.findFirstByUserOrderByCreatedAtAsc(user).orElse(null);
        }
        if (roadmap == null) return null;

        // Prefer persisted milestonesArray; fall back to milestones string
        String roadmapJson;
        try {
            if (roadmap.getMilestonesArray() != null && !roadmap.getMilestonesArray().isEmpty()) {
                roadmapJson = objectMapper.writeValueAsString(roadmap.getMilestonesArray());
            } else {
                roadmapJson = roadmap.getMilestones();
            }
        } catch (Exception e) {
            roadmapJson = roadmap.getMilestones();
        }

        // Generate preparation plan from roadmap
        String raw = geminiApiService.generatePreparationPlanFromRoadmap(roadmapJson);
        List<WeeklyPreparationWeek> weeks = parseWeeksArray(raw);

        // Persist single plan for user
        PreparationPlan plan = new PreparationPlan();
        plan.setUser(user);
        plan.setRoadmap(roadmap);
        plan.setContent(raw);
        plan.setWeeksArray(weeks);
        return preparationPlanRepository.save(plan);
    }

    public PreparationPlan get(User user) {
        return preparationPlanRepository.findByUser(user).orElse(null);
    }

    private List<WeeklyPreparationWeek> parseWeeksArray(String text) {
        if (text == null || text.isBlank()) return Collections.emptyList();
        try {
            int start = text.indexOf('[');
            int end = text.lastIndexOf(']');
            String json = (start >= 0 && end > start) ? text.substring(start, end + 1) : text;
            return objectMapper.readValue(json, new TypeReference<List<WeeklyPreparationWeek>>() {});
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
}
