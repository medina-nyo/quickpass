package com.quickpass.common.validation;

/**
 * Centralisation de toutes les expressions régulières utilisées
 * dans l'application pour garantir la cohérence et faciliter la maintenance.
 */
public final class RegexPatterns {

    private RegexPatterns() {

    }

    /** Format du numéro de téléphone mobile français (E.164) : ex. +33612345678 */
    public static final String MOBILE_PERSONNEL_FR = "^\\+33[67]\\d{8}$";

    /** Format du numéro de téléphone du commerce (fixe ou mobile en France) : ex. +33123456789 ou +33612345678 */
    public static final String TELEPHONE_COMMERCE_FR = "^\\+33[1-9]\\d{8}$";

    /** Numéro SIRET : exactement 14 chiffres */
    public static final String SIRET = "^[0-9]{14}$";

    /** * Format d'email strict : exige au moins un point dans le domaine (ex: nom@domaine.fr)
     * Votre ancienne Regex : public static final String EMAIL = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
     */
    public static final String EMAIL = "^[A-Za-z0-9._-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,63}$";
}