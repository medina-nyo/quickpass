package com.quickpass.unit.signup;

import com.quickpass.account.domain.service.PreferenceService;
import com.quickpass.signup.domain.boulanger.constants.SignupStepBoulanger;
import com.quickpass.signup.domain.constants.SignupType;
import com.quickpass.signup.domain.model.SignupSession;
import com.quickpass.signup.domain.repo.SignupSessionRepository;
import com.quickpass.signup.domain.service.impl.SignupServiceImpl;
import com.quickpass.signup.domain.validation.StepOrderChecker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Test unitaire du service d'inscription (SignupServiceImpl).
 *
 * <p>Vérifie la progression entre étapes, la création de session et
 * la gestion de l’expiration sans charger le contexte Spring Boot.</p>
 */
class SignupServiceTest {

    @Mock
    private SignupSessionRepository signupSessionRepository;

    @Mock
    private PreferenceService preferenceService;

    @InjectMocks
    private SignupServiceImpl signupService;

    private SignupSession session;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        session = SignupSession.builder()
                .id(1L)
                .compteId(1L)
                .signupType(SignupType.BOULANGER)
                .currentStep(SignupStepBoulanger.EMAIL)
                .expiresAt(LocalDateTime.now().plusHours(2))
                .completed(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(signupSessionRepository.findById(1L)).thenReturn(Optional.of(session));
    }

    @Test
    @DisplayName("Doit avancer à l’étape suivante si la session est valide")
    void shouldAdvanceStepWhenSessionValid() {
        try (MockedStatic<StepOrderChecker> mocked = Mockito.mockStatic(StepOrderChecker.class)) {
            mocked.when(() -> StepOrderChecker.validateOrder(any(), any(), any()))
                    .thenAnswer(inv -> null);

            signupService.advanceStep(1L, SignupStepBoulanger.EMAIL_CODE);

            assertThat(session.getCurrentStep()).isEqualTo(SignupStepBoulanger.EMAIL_CODE);
            verify(signupSessionRepository).save(session);
        }
    }

    @Test
    @DisplayName("Ne doit pas avancer si la session est expirée")
    void shouldNotAdvanceIfSessionExpired() {
        session.setExpiresAt(LocalDateTime.now().minusHours(1));

        assertThatThrownBy(() ->
                signupService.advanceStep(1L, SignupStepBoulanger.EMAIL_CODE)
        ).isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Session expirée");

        verify(signupSessionRepository, never()).save(any());
    }

    @Test
    @DisplayName("Doit créer une session lors du start()")
    void shouldCreateSignupSession() {
        when(signupSessionRepository.save(any(SignupSession.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        SignupSession newSession = signupService.start("contact@boulangerie.fr", SignupType.BOULANGER);

        assertThat(newSession).isNotNull();
        assertThat(newSession.getExpiresAt()).isAfter(LocalDateTime.now());
        assertThat(newSession.isCompleted()).isFalse();
        assertThat(newSession.getSignupType()).isEqualTo(SignupType.BOULANGER);

        verify(preferenceService, never()).initDefaults(any());
    }

    @Test
    @DisplayName("Doit marquer la session comme complétée")
    void shouldCompleteSession() {
        signupService.complete(1L);

        assertThat(session.isCompleted()).isTrue();
        verify(signupSessionRepository).save(session);
    }
}
