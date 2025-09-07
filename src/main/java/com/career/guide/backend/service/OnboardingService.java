package com.career.guide.backend.service;

import com.career.guide.backend.dto.onboarding.OnboardingRequest;
import com.career.guide.backend.dto.onboarding.OnboardingResponse;
import com.career.guide.backend.entity.OnboardingData;
import com.career.guide.backend.entity.User;
import com.career.guide.backend.repository.OnboardingDataRepository;
import com.career.guide.backend.repository.UserRepository;
import com.career.guide.backend.util.DtoMapper;
import java.time.LocalDateTime;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OnboardingService {

    private final UserRepository userRepository;
    private final OnboardingDataRepository onboardingDataRepository;
    private final ModelMapper modelMapper;
    private final DtoMapper dtoMapper;

    public OnboardingService(UserRepository userRepository,
            OnboardingDataRepository onboardingDataRepository,
            ModelMapper modelMapper,
            DtoMapper dtoMapper) {
        this.userRepository = userRepository;
        this.onboardingDataRepository = onboardingDataRepository;
        this.modelMapper = modelMapper;
        this.dtoMapper = dtoMapper;
    }

    public OnboardingData upsertOnboarding(Long userId, OnboardingRequest request) {
        User user = userRepository.findById(userId).orElseThrow();
        OnboardingData data = onboardingDataRepository.findByUser(user).orElseGet(() -> {
            OnboardingData created = new OnboardingData();
            created.setUser(user);
            return created;
        });
        modelMapper.map(request, data);
        // Mark onboarding completed and timestamp it
        data.setIsCompleted(true);
        data.setCompletedAt(LocalDateTime.now());
        // Reflect completion on User entity as well
        user.setOnboardingCompleted(true);
        userRepository.save(user);
        return onboardingDataRepository.save(data);
    }

    public OnboardingData getOnboarding(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        return onboardingDataRepository.findByUser(user).orElse(null);
    }

    public OnboardingResponse getOnboardingResponse(Long userId) {
        OnboardingData data = getOnboarding(userId);
        return dtoMapper.toOnboardingResponse(data);
    }

    public OnboardingResponse upsertOnboardingResponse(Long userId, OnboardingRequest request) {
        OnboardingData data = upsertOnboarding(userId, request);
        return dtoMapper.toOnboardingResponse(data);
    }
}


