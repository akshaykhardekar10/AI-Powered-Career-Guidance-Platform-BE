package com.career.guide.backend.repository;

import com.career.guide.backend.entity.User;
import com.career.guide.backend.entity.enums.RoadmapStatus;
import com.career.guide.backend.entity.roadmap.PersonalizedRoadmap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PersonalizedRoadmapRepository extends JpaRepository<PersonalizedRoadmap, Long> {
	List<PersonalizedRoadmap> findByUserAndStatus(User user, RoadmapStatus status);
	Optional<PersonalizedRoadmap> findByUserAndTargetRole(User user, String targetRole);
	Optional<PersonalizedRoadmap> findFirstByUserOrderByCreatedAtAsc(User user);
}
