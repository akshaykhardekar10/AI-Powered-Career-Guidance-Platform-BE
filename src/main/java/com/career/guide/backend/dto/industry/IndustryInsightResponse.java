package com.career.guide.backend.dto.industry;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IndustryInsightResponse {
    private String industry;
    private List<SalaryRange> salaryRanges;
    private Double growthRate;
    private String demandLevel;
    private List<String> topSkills;
    private String marketOutlook;
    private List<String> keyTrends;
    private List<String> recommendedSkills;
}
