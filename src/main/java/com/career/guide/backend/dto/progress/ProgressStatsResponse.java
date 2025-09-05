package com.career.guide.backend.dto.progress;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProgressStatsResponse {
	private Integer totalHours;
	private Integer streaks;
	private Map<String, Double> skillProgress;
	private Map<String, Object> monthlyData;
}


