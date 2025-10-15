package com.quickpass.stripe.domain.model.constants;

/**
 * Statut du compte Stripe associé à un utilisateur.
 */
public enum StripeStatus {
    NON_CONFIGURE,
    EN_ATTENTE,
    ACTIF,
    ERREUR
}
