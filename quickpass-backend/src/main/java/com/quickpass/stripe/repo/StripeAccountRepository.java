package com.quickpass.stripe.repo;

import com.quickpass.stripe.domain.model.StripeAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository JPA pour la gestion des comptes Stripe.
 */
@Repository
public interface StripeAccountRepository extends JpaRepository<StripeAccount, Long> {

    /**
     * Recherche un compte Stripe via l’identifiant du compte QuickPass.
     *
     * @param compteId identifiant du compte QuickPass
     * @return le compte Stripe s’il existe
     */
    Optional<StripeAccount> findByCompteId(Long compteId);
}
