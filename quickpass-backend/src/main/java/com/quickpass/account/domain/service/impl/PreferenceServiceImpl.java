package com.quickpass.account.domain.service.impl;

import com.quickpass.account.domain.model.Compte;
import com.quickpass.account.domain.model.Preference;
import com.quickpass.account.domain.repo.PreferenceRepository;
import com.quickpass.account.domain.service.PreferenceService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Service d'initialisation et de gestion des préférences RGPD pour les comptes QuickPass.
 *
 * <p>Garantit qu’un compte possède toujours des préférences RGPD par défaut,
 * conformes à la réglementation. Aucune donnée personnelle n’est activée
 * sans consentement explicite de l’utilisateur.</p>
 *
 * <p>Ce service est exécuté automatiquement à la création d’un nouveau compte.</p>
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PreferenceServiceImpl implements PreferenceService {

    /** Repository JPA pour la gestion des préférences */
    private final PreferenceRepository preferenceRepository;

    /** EntityManager injecté pour obtenir une référence managée de Compte */
    private final EntityManager entityManager;

    /**
     * Initialise les préférences RGPD par défaut pour un compte donné.
     *
     * @param compteId identifiant unique du compte concerné
     */
    @Override
    public void initDefaults(Long compteId) {
        preferenceRepository.findByCompte_Id(compteId).ifPresentOrElse(
                pref -> log.debug("Préférences déjà existantes pour le compte {}", compteId),
                () -> {
                    try {
                        Compte compteRef = entityManager.getReference(Compte.class, compteId);

                        Preference defaults = Preference.builder()
                                .compte(compteRef)
                                .accepteCgu(false)
                                .accepteRgpd(false)
                                .accepteStripe(false)
                                .email(true)
                                .sms(false)
                                .dateAcceptation(LocalDateTime.now())
                                .build();

                        preferenceRepository.save(defaults);
                        log.info("Préférences RGPD par défaut créées pour le compte {}", compteId);

                    } catch (Exception e) {
                        log.error("Erreur lors de la création des préférences pour le compte {} : {}", compteId, e.getMessage(), e);
                        throw e;
                    }
                }
        );
    }
}
