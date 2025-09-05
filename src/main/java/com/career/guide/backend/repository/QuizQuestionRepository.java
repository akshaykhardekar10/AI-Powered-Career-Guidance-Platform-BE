package com.career.guide.backend.repository;

import com.career.guide.backend.entity.quiz.QuizQuestion;
import com.career.guide.backend.entity.enums.DifficultyLevel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuizQuestionRepository extends JpaRepository<QuizQuestion, Long> {
    List<QuizQuestion> findBySkillCategoryAndDifficultyAndActiveTrue(String skillCategory, DifficultyLevel difficulty);
    List<QuizQuestion> findByActiveTrue();
}
