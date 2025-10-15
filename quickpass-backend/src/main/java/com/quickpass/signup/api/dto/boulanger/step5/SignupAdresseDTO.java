package com.quickpass.signup.api.dto.boulanger.step5;

import com.quickpass.common.constants.ModeVente;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Étape 5 – Saisie de l’adresse du commerce et du mode de vente.
 */
public record SignupAdresseDTO(
        @NotBlank(message = "adresse.obligatoire")
        String adresse,

        @NotBlank(message = "ville.obligatoire")
        String ville,

        @NotBlank(message = "code_postal.obligatoire")
        String codePostal,

        @NotNull(message = "modevente.invalide")
        ModeVente modeVente
) {}
