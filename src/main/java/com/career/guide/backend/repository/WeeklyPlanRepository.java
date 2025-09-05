package com.career.guide.backend.repository;

import com.career.guide.backend.entity.User;
import com.career.guide.backend.entity.enums.WeekStatus;
import com.career.guide.backend.entity.roadmap.PersonalizedRoadmap;
import com.career.guide.backend.entity.roadmap.WeeklyPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WeeklyPlanRepository extends JpaRepository<WeeklyPlan, Long> {
	List<WeeklyPlan> findByUserOrderByWeekNumber(User user);
	List<WeeklyPlan> findByRoadmapOrderByWeekNumber(PersonalizedRoadmap roadmap);
	Optional<WeeklyPlan> findByUserAndStatus(User user, WeekStatus status);
}


