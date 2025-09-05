package com.career.guide.backend.controller;

import com.career.guide.backend.dto.common.ApiResponse;
import com.career.guide.backend.dto.user.UserProfileResponse;
import com.career.guide.backend.dto.user.UserProfileUpdateRequest;
import com.career.guide.backend.dto.user.UserResponse;
import com.career.guide.backend.security.SecurityUtils;
import com.career.guide.backend.service.UserService;
import com.career.guide.backend.util.ResponseHelper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

	private final UserService userService;
	private final SecurityUtils securityUtils;

	public UserController(UserService userService, SecurityUtils securityUtils) {
		this.userService = userService;
		this.securityUtils = securityUtils;
	}

	@GetMapping("/profile")
	public ResponseEntity<ApiResponse<UserProfileResponse>> getProfile() {
		var user = securityUtils.getCurrentUserOrThrow();
		UserProfileResponse profile = userService.getProfile(user.getId());
		return ResponseHelper.success("User profile retrieved successfully", profile);
	}

	@PutMapping("/profile")
	public ResponseEntity<ApiResponse<UserProfileResponse>> updateProfile(@RequestBody UserProfileUpdateRequest request) {
		var user = securityUtils.getCurrentUserOrThrow();
		var updated = userService.updateProfile(user.getId(), request);
		return ResponseHelper.updated("User profile updated successfully", userService.toUserProfileResponse(updated));
	}

	@GetMapping("/me")
	public ResponseEntity<ApiResponse<UserResponse>> getMe() {
		var user = securityUtils.getCurrentUserOrThrow();
		return ResponseHelper.success("Current user information retrieved successfully", userService.toUserResponse(user));
	}
}