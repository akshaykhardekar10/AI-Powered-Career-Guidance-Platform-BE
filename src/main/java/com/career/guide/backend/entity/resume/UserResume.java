package com.career.guide.backend.entity.resume;

import com.career.guide.backend.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "user_resumes")
public class UserResume {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@Column(name = "resume_name")
	private String resumeName;

	@Column(name = "personal_info", columnDefinition = "TEXT")
	private String personalInfo;

	@Column(name = "education", columnDefinition = "TEXT")
	private String education;

	@Column(name = "experience", columnDefinition = "TEXT")
	private String experience;

	@Column(name = "projects", columnDefinition = "TEXT")
	private String projects;

	@Column(name = "skills", columnDefinition = "TEXT")
	private String skills;

	@Column(name = "certifications", columnDefinition = "TEXT")
	private String certifications;

	@Column(name = "achievements", columnDefinition = "TEXT")
	private String achievements;

	@Column(name = "template_id")
	private String templateId;

	@Column(name = "pdf_url")
	private String pdfUrl;

	@Column(name = "is_default")
	private Boolean isDefault = false;

	@Column(name = "created_at")
	private LocalDateTime createdAt = LocalDateTime.now();

	@Column(name = "updated_at")
	private LocalDateTime updatedAt;
}


