package com.quickpass.stripe.service;

import com.quickpass.stripe.domain.model.StripeAccount;
import com.quickpass.stripe.domain.model.constants.StripeStatus;

/**
 * Service métier de gestion des comptes Stripe.
 */
public interface StripeAccountService {

    /**
     * Crée un nouveau compte Stripe.
     *
     * @param compteId        identifiant du compte QuickPass
     * @param stripeAccountId identifiant Stripe externe
     * @return le compte Stripe créé
     */
    StripeAccount creerCompte(Long compteId, String stripeAccountId);

    /**
     * Met à jour le statut Stripe d’un compte.
     *
     * @param compteId identifiant du compte QuickPass
     * @param statut   nouveau statut Stripe
     */
    void mettreAJourStatut(Long compteId, StripeStatus statut);

    /**
     * Récupère le compte Stripe associé à un compte QuickPass.
     *
     * @param compteId identifiant du compte QuickPass
     * @return le compte Stripe correspondant ou null
     */
    StripeAccount obtenirParCompteId(Long compteId);
}
