package com.career.guide.backend.controller;

import com.career.guide.backend.dto.common.ApiResponse;
import com.career.guide.backend.dto.quiz.QuizResponse;
import com.career.guide.backend.dto.quiz.QuizResultResponse;
import com.career.guide.backend.dto.quiz.QuizSubmissionRequest;
import com.career.guide.backend.security.SecurityUtils;
import com.career.guide.backend.service.QuizService;
import com.career.guide.backend.util.ResponseHelper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quiz")
public class QuizController {

	private final QuizService quizService;
	private final SecurityUtils securityUtils;

	public QuizController(QuizService quizService, SecurityUtils securityUtils) {
		this.quizService = quizService;
		this.securityUtils = securityUtils;
	}

	@GetMapping("/questions")
	public ResponseEntity<ApiResponse<QuizResponse>> getQuestions() {
		var user = securityUtils.getCurrentUserOrThrow();
		return ResponseHelper.success("Quiz questions generated successfully", quizService.generateQuestions(user));
	}

	@PostMapping("/submit")
	public ResponseEntity<ApiResponse<QuizResultResponse>> submit(@RequestBody QuizSubmissionRequest request) {
		var user = securityUtils.getCurrentUserOrThrow();
		return ResponseHelper.success("Quiz submitted successfully", quizService.submitAnswers(user, request));
	}

	@GetMapping("/history")
	public ResponseEntity<ApiResponse<List<QuizResultResponse>>> history() {
		var user = securityUtils.getCurrentUserOrThrow();
		return ResponseHelper.success("Quiz history retrieved successfully", quizService.history(user));
	}

	@GetMapping("/results")
	public ResponseEntity<ApiResponse<QuizResultResponse>> getLatestResult() {
		var user = securityUtils.getCurrentUserOrThrow();
		QuizResultResponse result = quizService.getLatestResult(user);
		return ResponseHelper.success("Latest quiz result retrieved successfully", result);
	}

	@PostMapping("/retake")
	public ResponseEntity<ApiResponse<QuizResponse>> retake() {
		var user = securityUtils.getCurrentUserOrThrow();
		return ResponseHelper.success("Quiz retake questions generated successfully", quizService.generateQuestions(user));
	}
}


