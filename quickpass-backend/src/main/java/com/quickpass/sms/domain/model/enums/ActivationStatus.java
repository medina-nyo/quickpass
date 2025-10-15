package com.quickpass.sms.domain.model.enums;

/**
 * Statuts possibles pour un code d’activation SMS.
 *
 * <ul>
 *   <li>{@link #EN_ATTENTE} — Code généré et non encore validé</li>
 *   <li>{@link #VERIFIE} — Code validé avec succès</li>
 *   <li>{@link #EXPIRE} — Code expiré après le délai de validité</li>
 * </ul>
 */
public enum ActivationStatus {
    EN_ATTENTE,
    VERIFIE,
    EXPIRE
}
