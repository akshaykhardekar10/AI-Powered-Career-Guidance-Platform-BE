package com.career.guide.backend.dto.quiz;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class QuizSubmissionRequest {
	@NotNull
	private List<QuizSubmissionRequest.Answer> answers;
	@NotNull
	private Integer timeTaken;

	@Data
	public static class Answer {
		private Long questionId;
		private String selectedOption;
	}
}


