package com.quickpass.signup.api.dto.boulanger.step3;

import com.quickpass.common.constants.ModeVente;
import com.quickpass.common.validation.RegexPatterns;
import jakarta.validation.constraints.*;

/**
 * Étape 3 – Informations personnelles et professionnelles du boulanger.
 * Contient :
 * - Prénom, nom
 * - Téléphone personnel
 * - Raison sociale, SIRET, téléphone du commerce
 * - Mode de vente
 */
public record SignupProfileBoulangerDTO(
        @NotBlank(message = "prenom.obligatoire")
        @Size(max = 100, message = "prenom.trop_long")
        String prenom,

        @NotBlank(message = "nom.obligatoire")
        @Size(max = 100, message = "nom.trop_long")
        String nom,

        @NotBlank(message = "telephone_personnel.obligatoire")
        @Pattern(regexp = RegexPatterns.MOBILE_PERSONNEL_FR, message = "telephone_personnel.invalide")
        String telephonePersonnel,

        @NotBlank(message = "raison_sociale.obligatoire")
        @Size(max = 150, message = "raison_sociale.trop_long")
        String raisonSociale,

        @NotBlank(message = "siret.obligatoire")
        @Pattern(regexp = RegexPatterns.SIRET, message = "siret.invalide")
        String siret,

        @Pattern(regexp = RegexPatterns.TELEPHONE_COMMERCE_FR, message = "telephone_commerce.invalide")
        String telephoneCommerce,

        @NotNull(message = "modevente.invalide")
        ModeVente modeVente
) {}
