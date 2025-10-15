package com.quickpass.unit.sms;

import com.quickpass.error.exception.BusinessException;
import com.quickpass.sms.domain.model.ActivationSms;
import com.quickpass.sms.domain.model.enums.ActivationStatus;
import com.quickpass.sms.domain.repo.ActivationSmsRepository;
import com.quickpass.sms.domain.service.impl.ActivationServiceImpl;
import com.quickpass.sms.provider.MockSmsProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class ActivationServiceTest {

    @Mock
    ActivationSmsRepository repository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    MockSmsProvider smsProvider;

    @InjectMocks
    ActivationServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private ActivationSms enAttente(Long compteId) {
        return ActivationSms.builder()
                .compteId(compteId)
                .codeHash("hashed-code")
                .expiresAt(LocalDateTime.now().plusMinutes(10))
                .attempts(0)
                .maxAttempts(5)
                .status(ActivationStatus.EN_ATTENTE)
                .build();
    }

    private ActivationSms expire(Long compteId) {
        return ActivationSms.builder()
                .compteId(compteId)
                .codeHash("hashed-code")
                .expiresAt(LocalDateTime.now().minusMinutes(1))
                .attempts(0)
                .maxAttempts(5)
                .status(ActivationStatus.EN_ATTENTE)
                .build();
    }

    @Test
    @DisplayName("sendSms génère un code, persiste et envoie le SMS")
    void sendSms_ok() {
        when(passwordEncoder.encode(anyString())).thenReturn("hashed-code");
        when(repository.save(any(ActivationSms.class))).thenAnswer(i -> i.getArgument(0));

        assertThatCode(() -> service.sendSms(1L, "+33612345678")).doesNotThrowAnyException();

        ArgumentCaptor<ActivationSms> captor = ArgumentCaptor.forClass(ActivationSms.class);
        verify(repository).save(captor.capture());
        ActivationSms saved = captor.getValue();
        assertThat(saved.getCompteId()).isEqualTo(1L);
        assertThat(saved.getStatus()).isEqualTo(ActivationStatus.EN_ATTENTE);
        assertThat(saved.getExpiresAt()).isAfter(LocalDateTime.now());
        verify(smsProvider, times(1)).send(eq("+33612345678"), anyString());
    }

    @Test
    @DisplayName("sendSms applique le rate-limit 3/h")
    void sendSms_rateLimited() {
        when(passwordEncoder.encode(anyString())).thenReturn("hashed-code");
        when(repository.countRecentSends(anyLong(), any())).thenReturn(0L, 1L, 2L, 3L);
        when(repository.save(any(ActivationSms.class))).thenAnswer(i -> i.getArgument(0));

        assertThatCode(() -> service.sendSms(1L, "+33612345678")).doesNotThrowAnyException();
        assertThatCode(() -> service.sendSms(1L, "+33612345678")).doesNotThrowAnyException();
        assertThatCode(() -> service.sendSms(1L, "+33612345678")).doesNotThrowAnyException();
        assertThatThrownBy(() -> service.sendSms(1L, "+33612345678"))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("SMS_RATE_LIMIT");
    }

    @Test
    @DisplayName("verifySms accepte un code valide et active le compte")
    void verifySms_ok() {
        ActivationSms entity = enAttente(1L);
        when(repository.findByCompteIdAndStatus(1L, ActivationStatus.EN_ATTENTE)).thenReturn(Optional.of(entity));
        when(passwordEncoder.matches("123456", "hashed-code")).thenReturn(true);
        when(repository.save(any(ActivationSms.class))).thenAnswer(i -> i.getArgument(0));

        assertThatCode(() -> service.verifySms(1L, "123456")).doesNotThrowAnyException();

        assertThat(entity.getStatus()).isEqualTo(ActivationStatus.VERIFIE);
        verify(repository, times(1)).save(entity);
    }

    @Test
    @DisplayName("verifySms refuse un code invalide et incrémente attempts")
    void verifySms_wrongCode_incrementsAttempts() {
        ActivationSms entity = enAttente(1L);
        when(repository.findByCompteIdAndStatus(1L, ActivationStatus.EN_ATTENTE)).thenReturn(Optional.of(entity));
        when(passwordEncoder.matches("000000", "hashed-code")).thenReturn(false);
        when(repository.save(any(ActivationSms.class))).thenAnswer(i -> i.getArgument(0));

        assertThatThrownBy(() -> service.verifySms(1L, "000000"))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("SMS_INVALID_CODE");

        assertThat(entity.getAttempts()).isEqualTo(1);
        verify(repository, times(1)).save(entity);
    }

    @Test
    @DisplayName("verifySms refuse un code expiré")
    void verifySms_expired() {
        ActivationSms entity = expire(1L);
        when(repository.findByCompteIdAndStatus(1L, ActivationStatus.EN_ATTENTE)).thenReturn(Optional.of(entity));

        assertThatThrownBy(() -> service.verifySms(1L, "123456"))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("SMS_EXPIRED");
    }

    @Test
    @DisplayName("verifySms bloque après 5 tentatives")
    void verifySms_maxAttempts() {
        ActivationSms entity = enAttente(1L);
        entity.setAttempts(5);
        when(repository.findByCompteIdAndStatus(1L, ActivationStatus.EN_ATTENTE)).thenReturn(Optional.of(entity));

        assertThatThrownBy(() -> service.verifySms(1L, "000000"))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("SMS_MAX_ATTEMPTS");
    }

    @RepeatedTest(3)
    @DisplayName("sendSms génère un OTP 6 chiffres")
    void sendSms_codeFormat() {
        ArgumentCaptor<String> msgCaptor = ArgumentCaptor.forClass(String.class);
        when(passwordEncoder.encode(anyString())).thenReturn("hashed-code");
        when(repository.save(any(ActivationSms.class))).thenAnswer(i -> i.getArgument(0));

        service.sendSms(42L, "+33600000000");

        verify(smsProvider).send(eq("+33600000000"), msgCaptor.capture());
        String body = msgCaptor.getValue();
        assertThat(body).matches(".*\\b\\d{6}\\b.*");
    }

    @Test
    @DisplayName("verifySms nécessite une demande EN_ATTENTE existante")
    void verifySms_missingPending() {
        when(repository.findByCompteIdAndStatus(1L, ActivationStatus.EN_ATTENTE)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.verifySms(1L, "123456"))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("SMS_NO_PENDING");
    }
}
