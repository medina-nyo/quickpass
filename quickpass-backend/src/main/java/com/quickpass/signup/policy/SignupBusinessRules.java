package com.quickpass.signup.policy;

import com.quickpass.common.constants.ModeVente;
import com.quickpass.error.ErrorCatalog;
import com.quickpass.error.exception.BusinessException;
import com.quickpass.signup.api.dto.boulanger.step6.SignupStripeDTO;

/**
 * Classe centralisant les règles métier du processus d’inscription.
 *
 * <p>Ces règles garantissent la cohérence des étapes et la conformité
 * légale (par exemple : obligation de Stripe pour la vente en ligne).</p>
 */
public final class SignupBusinessRules {

    private SignupBusinessRules() {}

    /**
     * Vérifie que la configuration Stripe est correctement renseignée
     * lorsque le mode de vente l’exige.
     *
     * @param modeVente  mode de vente choisi par le boulanger
     * @param stripeData données de configuration Stripe
     * @throws BusinessException si Stripe est obligatoire mais non configuré
     */
    public static void validateStripeRequirement(ModeVente modeVente, SignupStripeDTO stripeData) {
        if (modeVente == null) return;

        boolean required = modeVente == ModeVente.EN_LIGNE || modeVente == ModeVente.LES_DEUX;

        if (required && (stripeData == null || stripeData.getStripeAccountId() == null)) {
            throw new BusinessException(
                    ErrorCatalog.ACCOUNT_LOCKED, // ou tu peux créer un code plus précis, ex: STRIPE_REQUIRED
                    "Configuration Stripe obligatoire pour ce mode de vente."
            );
        }
    }
}
