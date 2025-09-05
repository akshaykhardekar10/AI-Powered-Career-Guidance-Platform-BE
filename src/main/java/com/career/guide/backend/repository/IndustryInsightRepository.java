package com.career.guide.backend.repository;

import com.career.guide.backend.entity.insights.IndustryInsight;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IndustryInsightRepository extends JpaRepository<IndustryInsight, Long> {
	Optional<IndustryInsight> findByIndustry(String industry);
	List<IndustryInsight> findByDemandLevel(String demandLevel);
}


