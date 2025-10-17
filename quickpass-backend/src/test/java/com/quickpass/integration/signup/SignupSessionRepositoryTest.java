package com.quickpass.integration.signup;

import com.quickpass.config.TestJpaConfig;
import com.quickpass.signup.domain.boulanger.constants.SignupStepBoulanger;
import com.quickpass.signup.domain.constants.SignupType;
import com.quickpass.signup.domain.model.SignupSession;
import com.quickpass.signup.domain.repo.SignupSessionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test d’intégration pour vérifier la suppression automatique
 * des sessions expirées par le repository.
 */
@DataJpaTest
@Import(TestJpaConfig.class)
@EntityScan(basePackages = "com.quickpass.signup.domain.model")
@Transactional
class SignupSessionRepositoryTest {

    @Autowired
    private SignupSessionRepository signupSessionRepository;

    private SignupSession validSession;
    private SignupSession expiredSession;

    @BeforeEach
    void setUp() {
        validSession = SignupSession.builder()
                .email("valid@quickpass.fr") // CHAMP AJOUTÉ
                .compteId(1L)
                .signupType(SignupType.BOULANGER)
                .currentStep(SignupStepBoulanger.EMAIL)
                .expiresAt(LocalDateTime.now().plusHours(5))
                .completed(false)
                .createdAt(LocalDateTime.now().minusHours(1))
                .updatedAt(LocalDateTime.now().minusMinutes(10))
                .build();

        expiredSession = SignupSession.builder()
                .email("expired@quickpass.fr") // CHAMP AJOUTÉ
                .compteId(2L)
                .signupType(SignupType.BOULANGER)
                .currentStep(SignupStepBoulanger.EMAIL)
                .expiresAt(LocalDateTime.now().minusHours(2))
                .completed(false)
                .createdAt(LocalDateTime.now().minusHours(10))
                .updatedAt(LocalDateTime.now().minusHours(3))
                .build();

        signupSessionRepository.save(validSession);
        signupSessionRepository.save(expiredSession);
    }

    @Test
    @DisplayName("Doit supprimer uniquement les sessions expirées")
    void shouldDeleteOnlyExpiredSessions() {
        int deletedCount = signupSessionRepository.deleteExpiredSessions(LocalDateTime.now());
        assertThat(deletedCount).isEqualTo(1);

        Iterable<SignupSession> remaining = signupSessionRepository.findAll();
        SignupSession remainingSession = remaining.iterator().next();

        assertThat(remainingSession).isNotNull();
        assertThat(remainingSession.getCompteId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Ne doit rien supprimer si aucune session n’est expirée")
    void shouldNotDeleteIfNoExpiredSessions() {
        signupSessionRepository.deleteAll();

        SignupSession freshSession = SignupSession.builder()
                .email("fresh@quickpass.fr") // CHAMP AJOUTÉ
                .compteId(3L)
                .signupType(SignupType.BOULANGER)
                .currentStep(SignupStepBoulanger.EMAIL)
                .expiresAt(LocalDateTime.now().plusHours(12))
                .completed(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        signupSessionRepository.save(freshSession);

        int deletedCount = signupSessionRepository.deleteExpiredSessions(LocalDateTime.now());
        assertThat(deletedCount).isEqualTo(0);
        assertThat(signupSessionRepository.count()).isEqualTo(1);
    }
}