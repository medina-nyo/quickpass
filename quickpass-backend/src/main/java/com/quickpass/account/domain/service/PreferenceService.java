package com.quickpass.account.domain.service;

/**
 * Service de gestion des préférences et consentements RGPD.
 *
 * <p>Permet d’initialiser, de consulter et de modifier les préférences
 * associées à un compte utilisateur (boulanger, client, employé, etc.).</p>
 */
public interface PreferenceService {

    /**
     * Initialise les préférences RGPD par défaut pour un compte donné.
     *
     * <p>Crée automatiquement un enregistrement de préférences s’il
     * n’existe pas déjà, avec les consentements de base et les
     * notifications par défaut.</p>
     *
     * @param compteId identifiant du compte pour lequel créer les préférences
     */
    void initDefaults(Long compteId);
}
