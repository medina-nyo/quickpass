package com.quickpass.error;

/**
 * Liste des codes d'erreurs standardisés pour l'application QuickPass.
 *
 * <p>Ces codes sont utilisés dans {@link com.quickpass.error.ApiError}
 * pour uniformiser les réponses API et faciliter le traitement côté frontend.</p>
 *
 * <p>Les codes sont en français pour une meilleure lisibilité côté métier et logs.</p>
 */
public enum ErrorCode {

    VALIDATION_ERREUR,
    CHAMP_OBLIGATOIRE,

    EMAIL_INVALIDE,
    EMAIL_EXISTANT,


    TELEPHONE_INVALIDE,
    TELEPHONE_EXISTANT,


    SIRET_INVALIDE,
    SIRET_INTROUVABLE,


    CONTRAINTE_VIOLÉE,


    INSCRIPTION_ETAPE_INVALIDE,
    INSCRIPTION_SESSION_EXPIREE,


    ERREUR_INTERNE
}
