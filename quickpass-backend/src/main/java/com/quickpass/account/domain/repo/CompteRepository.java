package com.quickpass.account.domain.repo;

import com.quickpass.account.domain.model.Compte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository pour l'entité abstraite Compte.
 *
 * <p>Permet les opérations CRUD de base et la vérification d'unicité
 * des identifiants (email, téléphone) avant la création d'un nouveau compte.</p>
 */
@Repository
public interface CompteRepository extends JpaRepository<Compte, Long> {

    /**
     * Vérifie si un compte existe déjà dans la base de données pour l'adresse e-mail donnée.
     *
     * @param email L'adresse e-mail à vérifier.
     * @return true si un compte existe, false sinon.
     */
    boolean existsByEmail(String email);
}