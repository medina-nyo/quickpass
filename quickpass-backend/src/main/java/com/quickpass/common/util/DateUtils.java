package com.quickpass.common.util;

import java.time.LocalDateTime;

/**
 * Utilitaires de gestion des dates et heures pour QuickPass.
 * Fournit des raccourcis pour manipuler les durées
 * (ex : expiration de session, délais de validation, etc.)
 */
public final class DateUtils {

    private DateUtils() {
        // Empêche l’instanciation
    }

    /**
     * Retourne la date/heure actuelle + le nombre d’heures indiqué.
     *
     * @param hours Nombre d’heures à ajouter
     * @return LocalDateTime futur (ex : now + 24h)
     */
    public static LocalDateTime plusHours(long hours) {
        return LocalDateTime.now().plusHours(hours);
    }

    /**
     * Vérifie si une date est expirée par rapport à maintenant.
     *
     * @param date Date à comparer
     * @return true si la date est passée
     */
    public static boolean isExpired(LocalDateTime date) {
        return date.isBefore(LocalDateTime.now());
    }

    /**
     * Retourne la date/heure actuelle.
     */
    public static LocalDateTime now() {
        return LocalDateTime.now();
    }
}
