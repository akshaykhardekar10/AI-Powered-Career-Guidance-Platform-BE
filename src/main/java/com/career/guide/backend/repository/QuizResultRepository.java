package com.career.guide.backend.repository;

import com.career.guide.backend.entity.User;
import com.career.guide.backend.entity.quiz.QuizResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuizResultRepository extends JpaRepository<QuizResult, Long> {
	List<QuizResult> findByUserOrderByCompletedAtDesc(User user);
	List<QuizResult> findByUserAndQuizType(User user, String quizType);
}


