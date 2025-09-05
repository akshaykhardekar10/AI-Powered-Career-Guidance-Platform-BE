package com.career.guide.backend.controller;

import com.career.guide.backend.dto.common.ApiResponse;
import com.career.guide.backend.dto.roadmap.RoadmapResponse;
import com.career.guide.backend.dto.roadmap.PersonalizedRoadmapResponse;
import com.career.guide.backend.dto.roadmap.RoadmapUpdateRequest;
import com.career.guide.backend.security.SecurityUtils;
import com.career.guide.backend.service.RoadmapService;
import com.career.guide.backend.util.ResponseHelper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roadmaps")
public class RoadmapController {

	private final RoadmapService roadmapService;
	private final SecurityUtils securityUtils;

	public RoadmapController(RoadmapService roadmapService, SecurityUtils securityUtils) {
		this.roadmapService = roadmapService;
		this.securityUtils = securityUtils;
	}

	@PostMapping("/generate")
	public ResponseEntity<ApiResponse<RoadmapResponse>> generateRoadmap() {
		var user = securityUtils.getCurrentUserOrThrow();
		return ResponseHelper.created("Personalized roadmap generated successfully", roadmapService.generateRoadmap(user));
	}

	@GetMapping
	public ResponseEntity<ApiResponse<List<RoadmapResponse>>> getUserRoadmaps() {
		var user = securityUtils.getCurrentUserOrThrow();
		return ResponseHelper.success("User roadmaps retrieved successfully", roadmapService.getUserRoadmapsFull(user));
	}

	@GetMapping("/{id}")
	public ResponseEntity<ApiResponse<RoadmapResponse>> getRoadmap(@PathVariable Long id) {
		RoadmapResponse roadmap = roadmapService.getRoadmapFull(id);
		if (roadmap != null) {
			return ResponseHelper.success("Roadmap retrieved successfully", roadmap);
		} else {
			return ResponseHelper.notFound("Roadmap not found with id: " + id);
		}
	}

	@PutMapping("/{id}")
	public ResponseEntity<ApiResponse<PersonalizedRoadmapResponse>> updateRoadmap(@PathVariable Long id, @RequestBody RoadmapUpdateRequest updates) {
		PersonalizedRoadmapResponse roadmap = roadmapService.updateRoadmapResponse(id, updates);
		if (roadmap != null) {
			return ResponseHelper.updated("Roadmap updated successfully", roadmap);
		} else {
			return ResponseHelper.notFound("Roadmap not found with id: " + id);
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse<Object>> deleteRoadmap(@PathVariable Long id) {
		roadmapService.deleteRoadmap(id);
		return ResponseHelper.deleted("Roadmap deleted successfully");
	}

	@GetMapping("/{id}/weekly-plans")
	public ResponseEntity<ApiResponse<List<Object>>> getWeeklyPlans(@PathVariable Long id) {
		// This will be implemented when we create WeeklyPlanService
		return ResponseHelper.success("Weekly plans retrieved successfully", List.of());
	}
}
