package com.career.guide.backend.dto.resume;

import lombok.Data;

@Data
public class ResumeRequest {
	private String resumeName;
	private String personalInfo;
	private String education;
	private String experience;
	private String projects;
	private String skills;
	private String certifications;
	private String achievements;
	private String templateId;
}


