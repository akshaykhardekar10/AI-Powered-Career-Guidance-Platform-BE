package com.career.guide.backend.repository;

import com.career.guide.backend.entity.User;
import com.career.guide.backend.entity.roadmap.PreparationPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PreparationPlanRepository extends JpaRepository<PreparationPlan, Long> {
    Optional<PreparationPlan> findByUser(User user);
}
