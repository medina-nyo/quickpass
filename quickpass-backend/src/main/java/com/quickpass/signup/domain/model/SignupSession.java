package com.quickpass.signup.domain.model;

import com.quickpass.signup.domain.constants.SignupType;
import com.quickpass.signup.domain.boulanger.constants.SignupStepBoulanger;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * Représente une session d'inscription pour un utilisateur (ex: boulanger).
 *
 * <p>Cette entité permet de suivre la progression étape par étape du processus
 * d'inscription. Chaque session est limitée dans le temps (TTL = 24h).</p>
 */
@Entity
@Table(name = "signup_session")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignupSession {

    /** Identifiant unique de la session (auto-généré par la base MySQL). */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    /** Identifiant du compte lié à cette session (peut être temporaire). */
    @Column(name = "compte_id")
    private Long compteId;

    /** Type d'inscription (Boulanger, Client, etc.). */
    @Enumerated(EnumType.STRING)
    @Column(name = "signup_type", nullable = false)
    private SignupType signupType;

    /** Étape actuelle du processus d'inscription. */
    @Enumerated(EnumType.STRING)
    @Column(name = "current_step", nullable = false)
    private SignupStepBoulanger currentStep;

    /** Date d'expiration (TTL = 24h). */
    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    /** Indique si la session est terminée. */
    @Column(nullable = false)
    private boolean completed;

    /** Date de création de la session. */
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    /** Date de dernière mise à jour. */
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /**
     * Initialise automatiquement les valeurs obligatoires avant l’insertion.
     * <p>
     * Cette méthode garantit que toutes les contraintes NOT NULL sont respectées,
     * même si l’application ou les tests n’ont pas explicitement fourni certaines valeurs.
     * </p>
     */
    @PrePersist
    protected void onCreate() {
        if (signupType == null) signupType = SignupType.BOULANGER;
        if (currentStep == null) currentStep = SignupStepBoulanger.EMAIL;
        if (createdAt == null) createdAt = LocalDateTime.now();
        if (updatedAt == null) updatedAt = LocalDateTime.now();
        if (expiresAt == null) expiresAt = LocalDateTime.now().plusHours(24);
    }

    /**
     * Met à jour la date de dernière modification avant chaque update.
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
