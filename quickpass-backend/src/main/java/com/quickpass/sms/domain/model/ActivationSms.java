package com.quickpass.sms.domain.model;

import com.quickpass.sms.domain.model.enums.ActivationStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * Entité représentant un code d’activation SMS associé à un compte utilisateur.
 *
 * <p>Chaque instance correspond à un code OTP envoyé lors de la vérification
 * d’un numéro de téléphone. Le code est stocké sous forme de hachage
 * et possède une durée de vie limitée (10 minutes par défaut).</p>
 *
 * <p>Elle permet aussi de suivre le nombre de tentatives et le statut
 * d’activation : en attente, vérifié ou expiré.</p>
 */
@Entity
@Table(name = "activation_sms")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivationSms {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(nullable = false)
    private Long compteId;

    @Column(nullable = false)
    private String codeHash;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Builder.Default
    @Column(nullable = false)
    private int attempts = 0;

    @Builder.Default
    @Column(nullable = false)
    private int maxAttempts = 5;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ActivationStatus status;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiresAt);
    }

    public boolean canAttempt() {
        return attempts < maxAttempts && !isExpired();
    }

    public void incrementAttempts() {
        this.attempts++;
    }

    public void markVerified() {
        this.status = ActivationStatus.VERIFIE;
    }
}
