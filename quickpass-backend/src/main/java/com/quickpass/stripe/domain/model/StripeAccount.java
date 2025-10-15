package com.quickpass.stripe.domain.model;

import com.quickpass.stripe.domain.model.constants.StripeStatus;
import jakarta.persistence.*;
import lombok.*;

/**
 * Entité représentant le compte Stripe associé à un commerçant.
 * <p>
 * Seuls l’identifiant Stripe et le statut sont conservés,
 * conformément aux exigences RGPD.
 */
@Entity
@Table(name = "stripe_account")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StripeAccount {

    /**
     * Identifiant unique du compte Stripe local.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    /**
     * Identifiant du compte QuickPass associé.
     */
    @Column(nullable = false, unique = true)
    private Long compteId;

    /**
     * Identifiant du compte Stripe (fourni par l’API Stripe).
     */
    @Column(nullable = false, unique = true, length = 120)
    private String stripeAccountId;

    /**
     * Statut de configuration Stripe.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private StripeStatus status;
}
