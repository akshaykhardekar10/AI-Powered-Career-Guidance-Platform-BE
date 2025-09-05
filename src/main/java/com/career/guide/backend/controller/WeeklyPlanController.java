package com.career.guide.backend.controller;

import com.career.guide.backend.dto.common.ApiResponse;
import com.career.guide.backend.dto.roadmap.WeeklyPreparationWeek;
import com.career.guide.backend.entity.roadmap.WeeklyPlan;
import com.career.guide.backend.security.SecurityUtils;
import com.career.guide.backend.service.WeeklyPlanService;
import com.career.guide.backend.service.PreparationPlanService;
import com.career.guide.backend.util.ResponseHelper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/weekly-plans")
public class WeeklyPlanController {

    private final WeeklyPlanService weeklyPlanService;
    private final PreparationPlanService preparationPlanService;
    private final SecurityUtils securityUtils;

    public WeeklyPlanController(WeeklyPlanService weeklyPlanService, PreparationPlanService preparationPlanService, SecurityUtils securityUtils) {
        this.weeklyPlanService = weeklyPlanService;
        this.preparationPlanService = preparationPlanService;
        this.securityUtils = securityUtils;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<WeeklyPlan>>> getUserWeeklyPlans() {
        var user = securityUtils.getCurrentUserOrThrow();
        return ResponseHelper.success("User weekly plans retrieved successfully", weeklyPlanService.getUserWeeklyPlans(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<WeeklyPlan>> getWeeklyPlan(@PathVariable Long id) {
        WeeklyPlan plan = weeklyPlanService.getWeeklyPlan(id);
        if (plan != null) {
            return ResponseHelper.success("Weekly plan retrieved successfully", plan);
        } else {
            return ResponseHelper.notFound("Weekly plan not found with id: " + id);
        }
    }

    @PutMapping("/{id}/progress")
    public ResponseEntity<ApiResponse<WeeklyPlan>> updateProgress(@PathVariable Long id, @RequestBody ProgressUpdateRequest request) {
        WeeklyPlan plan = weeklyPlanService.updateProgress(id, request.getCompletionPercentage());
        if (plan != null) {
            return ResponseHelper.updated("Weekly plan progress updated successfully", plan);
        } else {
            return ResponseHelper.notFound("Weekly plan not found with id: " + id);
        }
    }

    @PostMapping("/{id}/complete-task")
    public ResponseEntity<ApiResponse<WeeklyPlan>> completeTask(@PathVariable Long id) {
        WeeklyPlan plan = weeklyPlanService.completeTask(id);
        if (plan != null) {
            return ResponseHelper.success("Task completed successfully", plan);
        } else {
            return ResponseHelper.notFound("Weekly plan not found with id: " + id);
        }
    }

    @PostMapping("/{id}/watch-video")
    public ResponseEntity<ApiResponse<WeeklyPlan>> watchVideo(@PathVariable Long id) {
        WeeklyPlan plan = weeklyPlanService.watchVideo(id);
        if (plan != null) {
            return ResponseHelper.success("Video marked as watched successfully", plan);
        } else {
            return ResponseHelper.notFound("Weekly plan not found with id: " + id);
        }
    }

    @GetMapping("/current")
    public ResponseEntity<ApiResponse<WeeklyPlan>> getCurrentWeekPlan() {
        var user = securityUtils.getCurrentUserOrThrow();
        WeeklyPlan plan = weeklyPlanService.getCurrentWeekPlan(user);
        if (plan != null) {
            return ResponseHelper.success("Current week plan retrieved successfully", plan);
        } else {
            return ResponseHelper.notFound("No current week plan found for user");
        }
    }

    @PostMapping("/generate")
    public ResponseEntity<ApiResponse<List<WeeklyPreparationWeek>>> generateWeeklyPreparation() {
        var user = securityUtils.getCurrentUserOrThrow();
        var plan = preparationPlanService.getOrGenerate(user);
        if (plan == null) {
            return ResponseHelper.badRequest("Failed to generate weekly preparation plan. Ensure a roadmap exists for this user.");
        }
        return ResponseHelper.created("Weekly preparation plan generated or fetched successfully", plan.getWeeksArray());
    }

    // DTOs for request bodies
    public static class ProgressUpdateRequest {
        private double completionPercentage;

        public double getCompletionPercentage() {
            return completionPercentage;
        }

        public void setCompletionPercentage(double completionPercentage) {
            this.completionPercentage = completionPercentage;
        }
    }
}
