package com.quickpass.sms.domain.service.impl;

import com.quickpass.error.ErrorCatalog;
import com.quickpass.error.exception.BusinessException;
import com.quickpass.sms.domain.model.ActivationSms;
import com.quickpass.sms.domain.model.enums.ActivationStatus;
import com.quickpass.sms.domain.repo.ActivationSmsRepository;
import com.quickpass.sms.domain.service.ActivationService;
import com.quickpass.sms.provider.MockSmsProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;

/**
 * Service métier responsable de la gestion du flux d’activation SMS :
 * génération, envoi et vérification des codes.
 *
 * Conforme aux standards d’entreprise :
 * - Rate-limit (3 SMS/h)
 * - Hashage sécurisé OTP
 * - Gestion d’expiration et de tentative avec cooldown
 */
@Service
@RequiredArgsConstructor
@Transactional
public class ActivationServiceImpl implements ActivationService {

    private static final int CODE_LENGTH = 6;
    private static final int CODE_TTL_MINUTES = 10;
    private static final int MAX_SMS_PER_HOUR = 3;
    private static final int MAX_ATTEMPTS = 5;
    private static final int LOCK_COOLDOWN_MINUTES = 15; // Ajout pour Retry-After

    private final ActivationSmsRepository activationSmsRepository;
    private final MockSmsProvider mockSmsProvider;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void sendSms(Long compteId, String phoneNumber) {
        LocalDateTime since = LocalDateTime.now().minusHours(1);
        long recentCount = activationSmsRepository.countRecentSends(compteId, since);

        if (recentCount >= MAX_SMS_PER_HOUR) {
            throw new BusinessException(ErrorCatalog.SMS_RATE_LIMIT,
                    "Trop de SMS envoyés cette heure-ci (limite : 3/h).");
        }

        String code = generateSecureCode();
        String hash = passwordEncoder.encode(code);

        ActivationSms activation = ActivationSms.builder()
                .compteId(compteId)
                .codeHash(hash)
                .expiresAt(LocalDateTime.now().plusMinutes(CODE_TTL_MINUTES))
                .status(ActivationStatus.EN_ATTENTE)
                .attempts(0)
                .maxAttempts(MAX_ATTEMPTS)
                .build();

        activationSmsRepository.save(activation);
        mockSmsProvider.send(phoneNumber, "Votre code QuickPass : " + code);
    }

    @Override
    public boolean verifySms(Long compteId, String code) {
        ActivationSms sms = activationSmsRepository
                .findByCompteIdAndStatus(compteId, ActivationStatus.EN_ATTENTE)
                .orElseThrow(() -> new BusinessException(ErrorCatalog.SMS_NO_PENDING,
                        "Aucune activation SMS en attente pour ce compte."));

        if (sms.isExpired()) {
            sms.setStatus(ActivationStatus.EXPIRE);
            activationSmsRepository.save(sms);
            throw new BusinessException(ErrorCatalog.SMS_EXPIRED, "Le code SMS est expiré.");
        }

        if (!sms.canAttempt()) {
            // 🕒 Ajout : blocage temporaire de 15 minutes
            sms.setExpiresAt(LocalDateTime.now().plusMinutes(LOCK_COOLDOWN_MINUTES));
            activationSmsRepository.save(sms);

            throw new BusinessException(ErrorCatalog.SMS_MAX_ATTEMPTS,
                    "Nombre maximum de tentatives atteint. Réessayez dans 15 minutes.");
        }

        sms.incrementAttempts();

        if (passwordEncoder.matches(code, sms.getCodeHash())) {
            sms.markVerified();
            activationSmsRepository.save(sms);
            return true;
        }

        activationSmsRepository.save(sms);
        throw new BusinessException(ErrorCatalog.SMS_INVALID_CODE, "Le code SMS saisi est invalide.");
    }

    private String generateSecureCode() {
        SecureRandom random = new SecureRandom();
        int min = (int) Math.pow(10, CODE_LENGTH - 1);
        int max = (int) Math.pow(10, CODE_LENGTH) - 1;
        return String.valueOf(random.nextInt(max - min + 1) + min);
    }
}
