package com.career.guide.backend.dto.resume;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResumeResponse {
	private Long id;
	private String resumeName;
	private String personalInfo;
	private String education;
	private String experience;
	private String projects;
	private String skills;
	private String certifications;
	private String achievements;
	private String templateId;
	private String pdfUrl;
	private Boolean isDefault;
}


