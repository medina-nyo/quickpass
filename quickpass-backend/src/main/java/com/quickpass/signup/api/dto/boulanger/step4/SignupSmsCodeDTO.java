package com.quickpass.signup.api.dto.boulanger.step4;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * Étape 4 – Vérification du code SMS envoyé au téléphone personnel.
 * Valide :
 * - Le code numérique envoyé par SMS.
 */
public record SignupSmsCodeDTO(
        @NotBlank(message = "sms.code.obligatoire")
        @Pattern(regexp = "^[0-9]{6}$", message = "sms.code.invalide")
        String smsCode
) {}
