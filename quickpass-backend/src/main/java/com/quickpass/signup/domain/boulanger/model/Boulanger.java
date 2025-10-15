package com.quickpass.signup.domain.boulanger.model;

import com.quickpass.account.domain.model.Compte;
import com.quickpass.common.constants.ModeVente;
import com.quickpass.common.constants.StatutConfigurationPaiement;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Entité représentant un boulanger inscrit sur la plateforme QuickPass.
 * Hérite de la classe abstraite Compte.
 *
 * Cette entité définit la structure JPA sans logique de validation.
 * La validation des données est gérée au niveau des DTO.
 */
@Getter
@Setter
@Entity
@Table(name = "boulanger")
public class Boulanger extends Compte {

    /** Raison sociale du boulanger (nom de l’entreprise) */
    @Column(nullable = false)
    private String raisonSociale;

    /** Numéro SIRET (14 chiffres) — unique par boulanger */
    @Column(nullable = false, unique = true, length = 14)
    private String siret;

    /** Numéro de téléphone du commerce (facultatif, visible par les clients) */
    @Column(name = "telephone_commerce")
    private String telephoneCommerce;

    /** Mode de vente choisi par le boulanger (SUR_PLACE, EN_LIGNE, LES_DEUX) */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ModeVente modeVente = ModeVente.SUR_PLACE;

    /** Statut de la configuration du paiement (Stripe, etc.) */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutConfigurationPaiement statutConfigurationPaiement = StatutConfigurationPaiement.NON_CONFIGURE;
}
