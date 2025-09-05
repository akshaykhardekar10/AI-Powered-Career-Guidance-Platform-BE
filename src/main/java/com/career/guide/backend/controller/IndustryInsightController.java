package com.career.guide.backend.controller;

import com.career.guide.backend.dto.common.ApiResponse;
import com.career.guide.backend.dto.industry.IndustryInsightResponse;
import com.career.guide.backend.security.SecurityUtils;
import com.career.guide.backend.service.IndustryInsightService;
import com.career.guide.backend.util.ResponseHelper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/industry-insights")
public class IndustryInsightController {

    private final IndustryInsightService industryInsightService;
    private final SecurityUtils securityUtils;

    public IndustryInsightController(IndustryInsightService industryInsightService, SecurityUtils securityUtils) {
        this.industryInsightService = industryInsightService;
        this.securityUtils = securityUtils;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<IndustryInsightResponse>> getInsights(@RequestParam(value = "force", defaultValue = "false") boolean force) {
        var user = securityUtils.getCurrentUserOrThrow();
        var resp = industryInsightService.getInsightsForUser(user, force);
        return ResponseHelper.success("Industry insights", resp);
    }
}
