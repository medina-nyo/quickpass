package com.quickpass.sms.provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Fournisseur SMS simulé pour les environnements de développement.
 *
 * <p>Simule l’envoi d’un message sans interaction avec un service tiers.
 * Utilisé pour les tests d’intégration et la pré-production.</p>
 */
@Component
public class MockSmsProvider {

    private static final Logger log = LoggerFactory.getLogger(MockSmsProvider.class);

    public void send(String phoneNumber, String message) {
        log.info("📱 [MOCK SMS] Envoi à {} : {}", phoneNumber, message);
    }
}
