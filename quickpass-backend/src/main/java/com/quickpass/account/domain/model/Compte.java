package com.quickpass.account.domain.model;

import com.quickpass.common.constants.EtatCompte;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * Classe abstraite représentant un compte utilisateur QuickPass.
 *
 * <p>Cette entité contient les informations d'identification de base
 * et sert de classe mère pour les différents types de comptes
 * (Boulanger, Client, etc.).</p>
 *
 * <p>Chaque compte dispose d’une relation 1:1 avec {@link Preference}
 * afin de garantir la conformité RGPD et la gestion centralisée
 * des consentements et notifications.</p>
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "compte")
public abstract class Compte {

    /**
     * Identifiant unique du compte.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Adresse e-mail unique utilisée pour l’authentification.
     */
    @Column(nullable = false, unique = true, length = 150)
    private String email;

    /**
     * Mot de passe chiffré (Argon2id).
     */
    @Column(nullable = false, length = 255)
    private String motDePasseHash;

    /**
     * Numéro de téléphone au format E.164.
     */
    @Column(name = "telephone_e164", unique = true, length = 20)
    private String telephoneE164;

    /**
     * État du compte (en attente, actif, bloqué, désactivé).
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EtatCompte etat = EtatCompte.EN_ATTENTE;

    /**
     * Date de création du compte.
     */
    @Column(nullable = false, updatable = false)
    private LocalDateTime dateCreation = LocalDateTime.now();

    /**
     * Date de dernière mise à jour.
     */
    @Column(nullable = false)
    private LocalDateTime dateMaj = LocalDateTime.now();

    /**
     * Relation 1:1 avec la table des préférences RGPD.
     */
    @OneToOne(mappedBy = "compte", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Preference preference;

    /**
     * Met automatiquement à jour la date de modification avant chaque update.
     */
    @PreUpdate
    protected void onUpdate() {
        this.dateMaj = LocalDateTime.now();
    }
}
