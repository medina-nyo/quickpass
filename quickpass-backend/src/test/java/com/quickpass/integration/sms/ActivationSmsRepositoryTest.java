package com.quickpass.integration.sms;

import com.quickpass.sms.domain.model.ActivationSms;
import com.quickpass.sms.domain.model.enums.ActivationStatus;
import com.quickpass.sms.domain.repo.ActivationSmsRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DisplayName("Integration - ActivationSmsRepository")
class ActivationSmsRepositoryTest {

    @Autowired
    private ActivationSmsRepository repository;

    @Test
    @DisplayName("✅ doit persister et retrouver un SMS avec statut EN_ATTENTE")
    void shouldPersistAndFindPendingByCompteId() {
        ActivationSms sms = ActivationSms.builder()
                .compteId(1001L)
                .codeHash("hash_123456")
                .expiresAt(LocalDateTime.now().plusMinutes(10))
                .status(ActivationStatus.EN_ATTENTE)
                .attempts(0)
                .maxAttempts(5)
                .build();

        ActivationSms saved = repository.save(sms);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getCompteId()).isEqualTo(1001L);
        assertThat(saved.getStatus()).isEqualTo(ActivationStatus.EN_ATTENTE);

        Optional<ActivationSms> found = repository.findByCompteIdAndStatus(1001L, ActivationStatus.EN_ATTENTE);
        assertThat(found).isPresent();
        assertThat(found.get().canAttempt()).isTrue();
        assertThat(found.get().isExpired()).isFalse();
    }

    @Test
    @DisplayName("🚫 doit retourner vide si aucun SMS trouvé pour le compte")
    void shouldReturnEmptyIfNoPendingSms() {
        Optional<ActivationSms> found = repository.findByCompteIdAndStatus(999L, ActivationStatus.EN_ATTENTE);
        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("⚙️ doit incrémenter le nombre de tentatives et persister")
    void shouldIncrementAttemptsAndPersist() {
        ActivationSms sms = ActivationSms.builder()
                .compteId(2002L)
                .codeHash("hash_654321")
                .expiresAt(LocalDateTime.now().plusMinutes(5))
                .status(ActivationStatus.EN_ATTENTE)
                .attempts(0)
                .maxAttempts(3)
                .build();

        ActivationSms saved = repository.save(sms);
        saved.incrementAttempts();
        repository.save(saved);

        Optional<ActivationSms> reloaded = repository.findByCompteIdAndStatus(2002L, ActivationStatus.EN_ATTENTE);
        assertThat(reloaded).isPresent();
        assertThat(reloaded.get().getAttempts()).isEqualTo(1);
    }
}
