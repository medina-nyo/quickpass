package com.quickpass.stripe.service.impl;

import com.quickpass.stripe.domain.model.StripeAccount;
import com.quickpass.stripe.domain.model.constants.StripeStatus;
import com.quickpass.stripe.repo.StripeAccountRepository;
import com.quickpass.stripe.service.StripeAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implémentation transactionnelle du service Stripe.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class StripeAccountServiceImpl implements StripeAccountService {

    private final StripeAccountRepository repository;

    @Override
    public StripeAccount creerCompte(Long compteId, String stripeAccountId) {
        StripeAccount account = StripeAccount.builder()
                .compteId(compteId)
                .stripeAccountId(stripeAccountId)
                .status(StripeStatus.EN_ATTENTE)
                .build();
        return repository.save(account);
    }

    @Override
    public void mettreAJourStatut(Long compteId, StripeStatus statut) {
        repository.findByCompteId(compteId).ifPresent(account -> {
            account.setStatus(statut);
            repository.save(account);
        });
    }

    @Override
    public StripeAccount obtenirParCompteId(Long compteId) {
        return repository.findByCompteId(compteId).orElse(null);
    }
}
