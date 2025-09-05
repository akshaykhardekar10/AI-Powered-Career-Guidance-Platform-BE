package com.career.guide.backend.repository;

import com.career.guide.backend.entity.User;
import com.career.guide.backend.entity.resume.UserResume;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserResumeRepository extends JpaRepository<UserResume, Long> {
	List<UserResume> findByUser(User user);
	Optional<UserResume> findByUserAndIsDefault(User user, Boolean isDefault);
}


