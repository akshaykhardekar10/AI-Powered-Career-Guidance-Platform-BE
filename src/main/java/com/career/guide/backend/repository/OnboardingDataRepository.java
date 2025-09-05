package com.career.guide.backend.repository;

import com.career.guide.backend.entity.OnboardingData;
import com.career.guide.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OnboardingDataRepository extends JpaRepository<OnboardingData, Long> {
	Optional<OnboardingData> findByUser(User user);
	List<OnboardingData> findByCareerGoal(String careerGoal);
}


