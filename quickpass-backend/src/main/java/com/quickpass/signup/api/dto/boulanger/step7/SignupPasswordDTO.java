package com.quickpass.signup.api.dto.boulanger.step7;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Étape 7 – Création du mot de passe du compte.
 * Valide :
 * - Minimum 8 caractères
 * - Doit contenir majuscules, chiffres, caractères spéciaux
 */
public record SignupPasswordDTO(
        @NotBlank(message = "motdepasse.obligatoire")
        @Size(min = 8, message = "motdepasse.trop_court")
        @Pattern(
                regexp = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*]).+$",
                message = "motdepasse.faible"
        )
        String motDePasse
) {}
