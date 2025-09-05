package com.career.guide.backend.dto.progress;

import lombok.Data;

@Data
public class ActivityLogRequest {
	private String activityType;
	private String details;
	private Integer timeSpent;
}


