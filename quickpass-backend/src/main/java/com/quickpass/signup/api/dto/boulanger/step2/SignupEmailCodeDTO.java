package com.quickpass.signup.api.dto.boulanger.step2;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * Étape 2 – Vérification du code envoyé par e-mail.
 * Valide :
 * - Le code à 6 chiffres reçu par e-mail.
 */
public record SignupEmailCodeDTO(
        @NotBlank(message = "email.code.obligatoire")
        @Pattern(regexp = "^[0-9]{6}$", message = "email.code.invalide")
        String emailCode
) {}
