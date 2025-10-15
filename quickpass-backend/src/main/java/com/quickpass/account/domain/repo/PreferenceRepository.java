package com.quickpass.account.domain.repo;

import com.quickpass.account.domain.model.Preference;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository Spring Data JPA pour la gestion des préférences et consentements RGPD
 * associés à un compte utilisateur QuickPass.
 *
 * <p>Permet de rechercher, créer et mettre à jour les préférences
 * liées à un compte unique, quel que soit son type (boulanger, client, employé, etc.).</p>
 */
@Repository
public interface PreferenceRepository extends CrudRepository<Preference, Long> {

    /**
     * Recherche les préférences associées à un compte spécifique.
     *
     * @param compteId identifiant unique du compte
     * @return les préférences du compte si elles existent
     */
    Optional<Preference> findByCompte_Id(Long compteId);
}
