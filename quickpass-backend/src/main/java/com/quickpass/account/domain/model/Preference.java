package com.quickpass.account.domain.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * Entité représentant les préférences et consentements RGPD d’un compte QuickPass.
 *
 * <p>Chaque compte dispose d’un enregistrement unique de préférences,
 * créé automatiquement à l’inscription. Cette entité gère à la fois
 * les consentements légaux (CGU, RGPD, Stripe DPA) et les
 * préférences de communication (email, SMS).</p>
 */
@Entity
@Table(name = "preference")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Preference {

    /**
     * Identifiant unique de la préférence.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    /**
     * Relation un-à-un avec le compte associé.
     */
    @OneToOne
    @JoinColumn(name = "compte_id", nullable = false, unique = true)
    private Compte compte;

    /**
     * Indique si l’utilisateur accepte les conditions générales d’utilisation.
     */
    @Column(nullable = false)
    private boolean accepteCgu = false;

    /**
     * Indique si l’utilisateur accepte la politique RGPD.
     */
    @Column(nullable = false)
    private boolean accepteRgpd = false;

    /**
     * Indique si l’utilisateur accepte la DPA Stripe.
     */
    @Column(nullable = false)
    private boolean accepteStripe = false;

    /**
     * Indique si l’utilisateur accepte de recevoir des e-mails transactionnels.
     */
    @Column(nullable = false)
    private boolean email = true;

    /**
     * Indique si l’utilisateur accepte les notifications SMS.
     */
    @Column(nullable = false)
    private boolean sms = false;

    /**
     * Date de la dernière acceptation des consentements.
     */
    private LocalDateTime dateAcceptation;
}
