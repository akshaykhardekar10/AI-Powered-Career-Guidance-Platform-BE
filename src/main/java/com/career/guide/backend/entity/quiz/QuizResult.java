package com.career.guide.backend.entity.quiz;

import com.career.guide.backend.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "quiz_results")
public class QuizResult {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@Column(name = "quiz_type")
	private String quizType;

	@Column(name = "total_questions")
	private Integer totalQuestions;

	@Column(name = "correct_answers")
	private Integer correctAnswers;

	@Column(name = "total_score")
	private Double totalScore;

	@Column(name = "skill_scores", columnDefinition = "TEXT")
	private String skillScores;

	@Column(name = "time_taken")
	private Integer timeTaken;

	@Column(name = "answers_data", columnDefinition = "TEXT")
	private String answersData;

	@Column(name = "category")
	private String category;

	@Column(name = "improvement_tip", columnDefinition = "TEXT")
	private String improvementTip;

	@Column(name = "completed_at")
	private LocalDateTime completedAt = LocalDateTime.now();
}



