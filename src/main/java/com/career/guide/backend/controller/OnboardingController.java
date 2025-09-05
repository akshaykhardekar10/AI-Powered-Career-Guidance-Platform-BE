package com.career.guide.backend.controller;

import com.career.guide.backend.dto.common.ApiResponse;
import com.career.guide.backend.dto.onboarding.OnboardingRequest;
import com.career.guide.backend.dto.onboarding.OnboardingResponse;
import com.career.guide.backend.security.SecurityUtils;
import com.career.guide.backend.service.OnboardingService;
import com.career.guide.backend.util.ResponseHelper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/onboarding")
public class OnboardingController {

	private final OnboardingService onboardingService;
	private final SecurityUtils securityUtils;

	public OnboardingController(OnboardingService onboardingService, SecurityUtils securityUtils) {
		this.onboardingService = onboardingService;
		this.securityUtils = securityUtils;
	}

	@GetMapping
	public ResponseEntity<ApiResponse<OnboardingResponse>> get() {
		var user = securityUtils.getCurrentUserOrThrow();
		var data = onboardingService.getOnboardingResponse(user.getId());
		return ResponseHelper.success("Onboarding data retrieved successfully", data);
	}

	@PostMapping
	public ResponseEntity<ApiResponse<OnboardingResponse>> upsert(@RequestBody OnboardingRequest request) {
		var user = securityUtils.getCurrentUserOrThrow();
		var data = onboardingService.upsertOnboardingResponse(user.getId(), request);
		return ResponseHelper.success("Onboarding data saved successfully", data);
	}
}