package com.career.guide.backend.security;

import com.career.guide.backend.entity.User;
import com.career.guide.backend.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {

	private final UserRepository userRepository;

	public SecurityUtils(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public User getCurrentUserOrThrow() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null || auth.getName() == null) {
			throw new IllegalStateException("Unauthenticated");
		}
		return userRepository.findByEmail(auth.getName()).orElseThrow();
	}
}


