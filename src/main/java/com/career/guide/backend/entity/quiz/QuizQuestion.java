package com.career.guide.backend.entity.quiz;

import com.career.guide.backend.entity.enums.DifficultyLevel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "quiz_questions")
public class QuizQuestion {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String question;

	@Column(columnDefinition = "TEXT")
	private String options;

	@Column(name = "correct_answer")
	private String correctAnswer;

	@Column(name = "skill_category")
	private String skillCategory;

	@Enumerated(EnumType.STRING)
	private DifficultyLevel difficulty;

	private Integer points;

	@Column(name = "explanation", columnDefinition = "TEXT")
	private String explanation;

	@Column(name = "created_at")
	private LocalDateTime createdAt = LocalDateTime.now();

	private Boolean active = true;
}


