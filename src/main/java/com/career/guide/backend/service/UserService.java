package com.career.guide.backend.service;

import com.career.guide.backend.dto.user.UserProfileResponse;
import com.career.guide.backend.dto.user.UserProfileUpdateRequest;
import com.career.guide.backend.dto.user.UserResponse;
import com.career.guide.backend.entity.User;
import com.career.guide.backend.entity.UserProfile;
import com.career.guide.backend.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {

	private final UserRepository userRepository;
	private final ModelMapper modelMapper;

	public UserService(UserRepository userRepository, ModelMapper modelMapper) {
		this.userRepository = userRepository;
		this.modelMapper = modelMapper;
	}

	public UserResponse toUserResponse(User user) {
		return modelMapper.map(user, UserResponse.class);
	}

	public UserProfileResponse toUserProfileResponse(UserProfile profile) {
		return modelMapper.map(profile, UserProfileResponse.class);
	}

	public UserProfile updateProfile(Long userId, UserProfileUpdateRequest request) {
		User user = userRepository.findById(userId).orElseThrow();
		UserProfile profile = user.getUserProfile();
		if (profile == null) {
			profile = new UserProfile();
			profile.setUser(user);
			user.setUserProfile(profile);
		}
		profile.setBio(request.getBio());
		profile.setLocation(request.getLocation());
		profile.setPhone(request.getPhone());
		profile.setLinkedinUrl(request.getLinkedinUrl());
		profile.setGithubUrl(request.getGithubUrl());
		profile.setPortfolioUrl(request.getPortfolioUrl());
		profile.setSkills(request.getSkills());
		profile.setAchievements(request.getAchievements());
		return profile;
	}

	public User getUser(Long userId) {
		return userRepository.findById(userId).orElseThrow();
	}

	public UserProfileResponse getProfile(Long userId) {
		User user = userRepository.findById(userId).orElseThrow();
		UserProfile profile = user.getUserProfile();
		return profile == null ? null : toUserProfileResponse(profile);
	}
}


