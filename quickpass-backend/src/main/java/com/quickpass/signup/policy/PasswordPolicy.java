package com.quickpass.signup.policy;

import java.util.regex.Pattern;

/**
 * Détermine si un mot de passe respecte les règles de sécurité de l’application QuickPass.
 * Conforme aux recommandations de l’ANSSI et de l’OWASP (version 2025).
 */
public final class PasswordPolicy {

    private static final int MIN_LENGTH = 12;

    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-={}\\[\\]|:;\"'<>,.?/~`]).{12,}$"
    );

    private PasswordPolicy() {
    }

    /**
     * Vérifie si le mot de passe est conforme à la politique de sécurité.
     *
     * @param password le mot de passe à vérifier
     * @return true si le mot de passe respecte les règles, sinon false
     */
    public static boolean isValid(String password) {
        if (password == null || password.isBlank()) {
            return false;
        }

        if (password.length() < MIN_LENGTH) {
            return false;
        }

        return PASSWORD_PATTERN.matcher(password).matches();
    }
}
