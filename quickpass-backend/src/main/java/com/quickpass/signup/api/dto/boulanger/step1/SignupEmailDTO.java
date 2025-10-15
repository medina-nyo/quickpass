package com.quickpass.signup.api.dto.boulanger.step1;

import com.quickpass.common.validation.RegexPatterns;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * Étape 1 – Saisie de l’adresse e-mail du boulanger.
 * Valide :
 * - L’adresse e-mail doit être au bon format.
 * - L’adresse est obligatoire et unique.
 */
public record SignupEmailDTO(
        @NotBlank(message = "email.obligatoire")
        @Pattern(regexp = RegexPatterns.EMAIL, message = "email.invalide")
        String email
) {}