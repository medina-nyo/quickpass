package com.quickpass.signup.domain.service.impl;

import com.quickpass.signup.domain.boulanger.constants.SignupStepBoulanger;
import com.quickpass.signup.domain.constants.SignupType;
import com.quickpass.signup.domain.model.SignupSession;
import com.quickpass.signup.domain.repo.SignupSessionRepository;
import com.quickpass.signup.domain.service.SignupService;
import com.quickpass.signup.domain.validation.StepOrderChecker;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * Service gérant les sessions d'inscription et la progression du parcours utilisateur.
 *
 * <p>Cette implémentation orchestre la création, la récupération et la mise à jour
 * des sessions d'inscription, tout en garantissant la cohérence des transitions entre étapes.</p>
 */
@Service
@RequiredArgsConstructor
@Transactional
public class SignupServiceImpl implements SignupService {

    private final SignupSessionRepository signupSessionRepository;

    @Override
    public SignupSession start(String email, SignupType signupType) {
        SignupSession session = SignupSession.builder()
                .signupType(signupType)
                .currentStep(SignupStepBoulanger.EMAIL)
                .expiresAt(LocalDateTime.now().plusHours(24))
                .completed(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return signupSessionRepository.save(session);
    }

    @Override
    public void advanceStep(Long sessionId, SignupStepBoulanger nextStep) {
        SignupSession session = signupSessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Session d'inscription introuvable."));

        if (session.getExpiresAt() != null && session.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Session expirée");
        }

        StepOrderChecker.validateOrder(
                StepOrderChecker.FlowDefinition.BOULANGER,
                session.getCurrentStep(),
                nextStep.name()
        );

        session.setCurrentStep(nextStep);
        session.setUpdatedAt(LocalDateTime.now());
        signupSessionRepository.save(session);
    }

    @Override
    public void complete(Long sessionId) {
        SignupSession session = signupSessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Session d'inscription introuvable."));

        session.setCompleted(true);
        session.setUpdatedAt(LocalDateTime.now());
        signupSessionRepository.save(session);
    }

    @Override
    public SignupSession getById(Long sessionId) {
        return signupSessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Session d'inscription introuvable."));
    }
}
