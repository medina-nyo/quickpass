package com.quickpass.unit.common.security;

import com.quickpass.signup.policy.PasswordPolicy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests unitaires de la classe PasswordPolicy.
 * Vérifie la conformité des règles de sécurité imposées par l’application.
 */
class PasswordPolicyTest {

    @Test
    @DisplayName("Mot de passe conforme (fort et complet)")
    void shouldAcceptStrongPassword() {
        String password = "Motdepasse@2025";
        assertThat(PasswordPolicy.isValid(password)).isTrue();
    }

    @Test
    @DisplayName("Mot de passe trop court")
    void shouldRejectShortPassword() {
        String password = "Abc@12";
        assertThat(PasswordPolicy.isValid(password)).isFalse();
    }

    @Test
    @DisplayName("Mot de passe sans majuscule")
    void shouldRejectPasswordWithoutUppercase() {
        String password = "motdepasse@2025";
        assertThat(PasswordPolicy.isValid(password)).isFalse();
    }

    @Test
    @DisplayName("Mot de passe sans chiffre")
    void shouldRejectPasswordWithoutDigit() {
        String password = "Motdepasse@";
        assertThat(PasswordPolicy.isValid(password)).isFalse();
    }

    @Test
    @DisplayName("Mot de passe sans symbole")
    void shouldRejectPasswordWithoutSymbol() {
        String password = "Motdepasse2025";
        assertThat(PasswordPolicy.isValid(password)).isFalse();
    }

    @Test
    @DisplayName("Mot de passe vide ou null")
    void shouldRejectEmptyOrNullPassword() {
        assertThat(PasswordPolicy.isValid("")).isFalse();
        assertThat(PasswordPolicy.isValid(null)).isFalse();
    }
}
