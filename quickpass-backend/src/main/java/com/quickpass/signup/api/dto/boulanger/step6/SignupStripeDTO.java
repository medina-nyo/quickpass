package com.quickpass.signup.api.dto.boulanger.step6;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * Données de configuration Stripe lors de l’inscription.
 *
 * <p>Contient l’identifiant du compte Stripe relié au boulanger.</p>
 */
@Getter
@Setter
public class SignupStripeDTO {

        /**
         * Identifiant du compte Stripe connecté.
         * Doit être fourni pour les modes de vente EN_LIGNE ou LES_DEUX.
         */
        @NotBlank(message = "L’identifiant du compte Stripe est obligatoire.")
        private String stripeAccountId;
}
