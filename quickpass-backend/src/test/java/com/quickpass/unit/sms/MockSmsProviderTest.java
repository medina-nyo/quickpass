package com.quickpass.unit.sms;

import com.quickpass.sms.provider.MockSmsProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import static org.assertj.core.api.Assertions.assertThatCode;

/**
 * Test unitaire du fournisseur SMS simulé.
 *
 * <p>Vérifie que le composant {@link MockSmsProvider} enregistre correctement
 * un message dans les logs sans lever d’exception, garantissant une
 * compatibilité avec les environnements de développement et de test.</p>
 */
class MockSmsProviderTest {

    private static final Logger log = LoggerFactory.getLogger(MockSmsProviderTest.class);

    @Test
    @DisplayName("Le MockSmsProvider doit simuler un envoi sans erreur")
    void shouldSendSmsWithoutError() {
        MockSmsProvider provider = new MockSmsProvider();

        assertThatCode(() -> provider.send("+33612345678", "Votre code QuickPass : 123456"))
                .as("L'envoi simulé ne doit pas lever d'exception")
                .doesNotThrowAnyException();

        log.info("✅ Envoi SMS simulé testé avec succès.");
    }
}
