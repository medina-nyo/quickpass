package com.quickpass.core.policy.domain.model;

import com.quickpass.core.policy.domain.model.constants.AuditAction;
import com.quickpass.core.policy.domain.model.constants.AuditStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Entité représentant un enregistrement d'audit pour une action sensible.
 * <p>
 * Chaque opération critique (ex. : envoi SMS, vérification, intégration Stripe)
 * est journalisée pour assurer la traçabilité et la conformité RGPD.
 */
@Entity
@Table(name = "audit_log")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLog {

    /**
     * Identifiant unique de l'enregistrement d'audit.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    /**
     * Identifiant du compte concerné.
     */
    @Column(nullable = false)
    private Long compteId;

    /**
     * Action métier auditée.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private AuditAction action;

    /**
     * Statut de l’action auditée.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private AuditStatus status;

    /**
     * Détail ou message complémentaire sur l’événement.
     */
    @Column(length = 500)
    private String detail;

    /**
     * Date et heure de l’enregistrement.
     */
    @Column(nullable = false)
    private LocalDateTime timestamp;

    /**
     * Initialise automatiquement le timestamp lors de la création.
     */
    @PrePersist
    protected void onCreate() {
        if (timestamp == null) {
            timestamp = LocalDateTime.now();
        }
    }
}
