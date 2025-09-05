package com.career.guide.backend.entity.insights;

import com.career.guide.backend.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "industry_insights")
public class IndustryInsight {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true)
	private String industry;

	@Column(name = "salary_ranges", columnDefinition = "TEXT")
	private String salaryRanges;

	@Column(name = "growth_rate")
	private Double growthRate;

	@Column(name = "demand_level")
	private String demandLevel;

	@Column(name = "top_skills", columnDefinition = "TEXT")
	private String topSkills;

	@Column(name = "market_outlook")
	private String marketOutlook;

	@Column(name = "key_trends", columnDefinition = "TEXT")
	private String keyTrends;

	@Column(name = "recommended_skills", columnDefinition = "TEXT")
	private String recommendedSkills;

	@Column(name = "last_updated")
	private LocalDateTime lastUpdated = LocalDateTime.now();

	@Column(name = "next_update")
	private LocalDateTime nextUpdate;

	@OneToMany(mappedBy = "industry")
	private List<User> users;
}


