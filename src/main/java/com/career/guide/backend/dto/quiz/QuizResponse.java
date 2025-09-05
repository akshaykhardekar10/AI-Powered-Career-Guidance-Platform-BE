package com.career.guide.backend.dto.quiz;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuizResponse {
	private List<Question> questions;
	private Integer timeLimit;

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Question {
		private Long id;
		private String question;
		private List<String> options;
		private String skillCategory;
		private String difficulty;
		private Integer points;
	}
}


