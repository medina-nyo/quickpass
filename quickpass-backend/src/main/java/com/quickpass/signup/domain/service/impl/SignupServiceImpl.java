package com.quickpass.signup.domain.service.impl;

import com.quickpass.account.domain.repo.CompteRepository;
import com.quickpass.error.ErrorCatalog;
import com.quickpass.error.exception.BusinessException;
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
    private final CompteRepository compteRepository;

    /**
     * Démarre une nouvelle session d'inscription après avoir vérifié l'unicité de l'e-mail
     * dans les comptes existants et dans les sessions d'inscription actives.
     *
     * @param email l'adresse e-mail de l'utilisateur.
     * @param signupType le type d'inscription (ex: BOULANGER).
     * @return la session d'inscription créée.
     */
    @Override
    public SignupSession start(String email, SignupType signupType) {
        if (compteRepository.existsByEmail(email)) {
            throw new BusinessException(ErrorCatalog.SIGNUP_EMAIL_EXISTS, "L'e-mail est déjà associé à un compte finalisé.");
        }

        if (signupSessionRepository.existsByEmailAndCompletedIsFalse(email)) {
            throw new BusinessException(ErrorCatalog.SIGNUP_EMAIL_EXISTS, "Une session d'inscription active existe déjà pour cet e-mail.");
        }

        SignupSession session = SignupSession.builder()
                .signupType(signupType)
                .email(email)
                .currentStep(SignupStepBoulanger.EMAIL)
                .expiresAt(LocalDateTime.now().plusHours(24))
                .completed(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return signupSessionRepository.save(session);
    }

    /**
     * Fait progresser la session à l'étape suivante, après vérification de l'ordre.
     *
     * @param sessionId l'identifiant de la session.
     * @param nextStep la prochaine étape attendue.
     */
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

    /**
     * Marque la session comme complétée.
     *
     * @param sessionId l'identifiant de la session.
     */
    @Override
    public void complete(Long sessionId) {
        SignupSession session = signupSessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Session d'inscription introuvable."));

        session.setCompleted(true);
        session.setUpdatedAt(LocalDateTime.now());
        signupSessionRepository.save(session);
    }

    /**
     * Récupère une session par son identifiant.
     *
     * @param sessionId l'identifiant de la session.
     * @return la session d'inscription.
     */
    @Override
    public SignupSession getById(Long sessionId) {
        return signupSessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Session d'inscription introuvable."));
    }
}