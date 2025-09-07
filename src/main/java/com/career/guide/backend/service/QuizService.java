package com.career.guide.backend.service;

import com.career.guide.backend.dto.quiz.QuizResponse;
import com.career.guide.backend.dto.quiz.QuizSubmissionRequest;
import com.career.guide.backend.dto.quiz.QuizResultResponse;
import com.career.guide.backend.entity.OnboardingData;
import com.career.guide.backend.entity.User;
import com.career.guide.backend.entity.quiz.QuizQuestion;
import com.career.guide.backend.entity.quiz.QuizResult;
import com.career.guide.backend.repository.OnboardingDataRepository;
import com.career.guide.backend.repository.QuizQuestionRepository;
import com.career.guide.backend.repository.QuizResultRepository;
import com.career.guide.backend.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class QuizService {

    private final QuizResultRepository quizResultRepository;
    private final QuizQuestionRepository quizQuestionRepository;
    private final OnboardingDataRepository onboardingDataRepository;
    private final UserRepository userRepository;
    private final GeminiApiService geminiApiService;
    private final ObjectMapper objectMapper;

    public QuizService(QuizResultRepository quizResultRepository, 
            QuizQuestionRepository quizQuestionRepository,
            OnboardingDataRepository onboardingDataRepository,
            GeminiApiService geminiApiService,
            UserRepository userRepository) {
        this.quizResultRepository = quizResultRepository;
        this.quizQuestionRepository = quizQuestionRepository;
        this.onboardingDataRepository = onboardingDataRepository;
        this.geminiApiService = geminiApiService;
        this.objectMapper = new ObjectMapper();
        this.userRepository = userRepository;
    }

    public QuizResponse generateQuestions(User user) {
        OnboardingData onboarding = onboardingDataRepository.findByUser(user).orElse(null);
        List<QuizQuestion> questions = geminiApiService.generateQuizQuestions(user, onboarding);
        
        // Save generated questions to database
        questions = quizQuestionRepository.saveAll(questions);
        
        return geminiApiService.toQuizResponse(questions);
    }

    public QuizResultResponse submitAnswers(User user, QuizSubmissionRequest request) {
        // Calculate score and skill breakdown
        Map<String, Double> skillScores = new HashMap<>();
        int correctAnswers = 0;
        int totalQuestions = request.getAnswers().size();
        
        for (QuizSubmissionRequest.Answer answer : request.getAnswers()) {
            QuizQuestion question = quizQuestionRepository.findById(answer.getQuestionId()).orElse(null);
            if (question != null) {
                boolean isCorrect = question.getCorrectAnswer().equals(answer.getSelectedOption());
                if (isCorrect) {
                    correctAnswers++;
                    // Update skill scores
                    String skill = question.getSkillCategory();
                    skillScores.put(skill, skillScores.getOrDefault(skill, 0.0) + question.getPoints());
                }
            }
        }
        
        double totalScore = (double) correctAnswers / totalQuestions * 100;
        
        // Create result
        QuizResult result = new QuizResult();
        result.setUser(user);
        result.setQuizType("personalized");
        result.setTotalScore(totalScore);
        result.setTotalQuestions(totalQuestions);
        result.setCorrectAnswers(correctAnswers);
        result.setTimeTaken(request.getTimeTaken());
        result.setCategory("Technical");
        
        try {
            result.setSkillScores(objectMapper.writeValueAsString(skillScores));
            result.setAnswersData(objectMapper.writeValueAsString(request.getAnswers()));
        } catch (Exception e) {
            result.setSkillScores("{}");
            result.setAnswersData("[]");
        }
        
        // Build improvement tip from wrong answers if any
        List<QuizSubmissionRequest.Answer> wrong = request.getAnswers().stream()
            .filter(a -> {
                QuizQuestion q = quizQuestionRepository.findById(a.getQuestionId()).orElse(null);
                return q != null && !q.getCorrectAnswer().equals(a.getSelectedOption());
            })
            .collect(Collectors.toList());
        if (!wrong.isEmpty()) {
            String wrongQuestionsText = wrong.stream().map(a -> {
                QuizQuestion q = quizQuestionRepository.findById(a.getQuestionId()).orElse(null);
                if (q == null) return "";
                return String.format("Question: \"%s\"\nCorrect Answer: \"%s\"\nUser Answer: \"%s\"",
                    q.getQuestion(), q.getCorrectAnswer(), a.getSelectedOption());
            }).collect(Collectors.joining("\n\n"));
            OnboardingData onboarding = onboardingDataRepository.findByUser(user).orElse(null);
            String industry = onboarding != null && onboarding.getBranch() != null ? onboarding.getBranch() : "tech";
            String tip = geminiApiService.generateImprovementTip(industry, wrongQuestionsText);
            result.setImprovementTip(tip);
        }

        QuizResult saved = quizResultRepository.save(result);
        // Mark initial quiz as completed on the user (one-time)
        if (Boolean.FALSE.equals(user.getQuizCompleted())) {
            user.setQuizCompleted(true);
            userRepository.save(user);
        }
        return toResultResponse(saved);
    }

    public List<QuizResultResponse> history(User user) {
        return quizResultRepository.findByUserOrderByCompletedAtDesc(user)
            .stream().map(this::toResultResponse).toList();
    }

	public QuizResultResponse getLatestResult(User user) {
		List<QuizResult> results = quizResultRepository.findByUserOrderByCompletedAtDesc(user);
		QuizResult latest = results.isEmpty() ? null : results.get(0);
		return latest == null ? null : toResultResponse(latest);
	}

	private QuizResultResponse toResultResponse(QuizResult entity) {
		Map<String, Double> breakdown;
		try {
			breakdown = objectMapper.readValue(entity.getSkillScores(), new TypeReference<Map<String, Double>>(){});
		} catch (Exception e) {
			breakdown = new HashMap<>();
		}
		return QuizResultResponse.builder()
			.score(entity.getTotalScore())
			.skillBreakdown(breakdown)
			.recommendations(entity.getImprovementTip())
			.build();
	}
}