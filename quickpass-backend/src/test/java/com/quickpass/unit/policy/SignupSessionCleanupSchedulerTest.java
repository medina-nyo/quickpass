package com.quickpass.unit.policy;

import com.quickpass.signup.policy.SignupSessionCleanupScheduler;
import com.quickpass.signup.domain.repo.SignupSessionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.*;

/**
 * Test unitaire du scheduler de nettoyage des sessions d'inscription.
 *
 * <p>Ce test vérifie que le scheduler appelle bien la méthode de suppression
 * du repository et ne lève aucune exception, sans charger le contexte Spring Boot.</p>
 */
class SignupSessionCleanupSchedulerTest {

    @Mock
    private SignupSessionRepository signupSessionRepository;

    @InjectMocks
    private SignupSessionCleanupScheduler scheduler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Le scheduler supprime les sessions expirées sans erreur")
    void shouldCleanExpiredSessionsSafely() {

        when(signupSessionRepository.deleteExpiredSessions(any(LocalDateTime.class))).thenReturn(3);

        assertThatCode(() -> scheduler.cleanExpiredSessions())
                .as("La tâche planifiée doit s'exécuter sans lever d'exception")
                .doesNotThrowAnyException();


        verify(signupSessionRepository, times(1))
                .deleteExpiredSessions(any(LocalDateTime.class));
    }

    @Test
    @DisplayName("Le scheduler ne log rien s’il n’y a aucune session expirée")
    void shouldHandleZeroDeletedSessionsGracefully() {

        when(signupSessionRepository.deleteExpiredSessions(any(LocalDateTime.class))).thenReturn(0);


        assertThatCode(() -> scheduler.cleanExpiredSessions())
                .as("Aucune erreur ne doit être levée même si aucune session n'est supprimée")
                .doesNotThrowAnyException();

        verify(signupSessionRepository, times(1))
                .deleteExpiredSessions(any(LocalDateTime.class));
    }
}
