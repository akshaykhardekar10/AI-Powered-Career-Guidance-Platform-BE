package com.career.guide.backend.repository;

import com.career.guide.backend.entity.User;
import com.career.guide.backend.entity.progress.UserProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface UserProgressRepository extends JpaRepository<UserProgress, Long> {
	List<UserProgress> findByUserAndDateBetween(User user, LocalDate start, LocalDate end);
	List<UserProgress> findByUserOrderByDateDesc(User user);
	@Query("SELECT SUM(p.timeSpent) FROM UserProgress p WHERE p.user = ?1 AND p.date = ?2")
	Integer getTotalTimeSpentByUserAndDate(User user, LocalDate date);
}


