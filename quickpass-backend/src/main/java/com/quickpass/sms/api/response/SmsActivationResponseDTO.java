package com.quickpass.sms.api.response;

import com.quickpass.sms.domain.model.enums.ActivationStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * Réponse standard retournée par les endpoints d’activation SMS.
 *
 * <p>Ce DTO décrit l’état du processus d’activation après une action
 * (envoi ou vérification du code). Il est conçu pour être stable,
 * contractuel et exploitable par les clients front (web/mobile).</p>
 *
 * <p>Exemples d’usages :</p>
 * <ul>
 *   <li>{@link ActivationStatus#EN_ATTENTE} → code envoyé, en attente de vérification</li>
 *   <li>{@link ActivationStatus#VERIFIE} → code validé avec succès</li>
 *   <li>{@link ActivationStatus#EXPIRE} → code expiré ou invalidé</li>
 * </ul>
 *
 * @author QuickPass
 * @since 1.0
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Réponse renvoyée après une opération d’activation par SMS.")
public class SmsActivationResponseDTO {

    @Schema(
            description = "Identifiant unique du compte concerné.",
            example = "42"
    )
    private Long compteId;

    @Schema(
            description = "Statut courant du processus d’activation.",
            example = "EN_ATTENTE"
    )
    private ActivationStatus status;

    @Schema(
            description = "Message d’information ou d’erreur lié à l’opération.",
            example = "Code vérifié avec succès."
    )
    private String message;

    /**
     * Fabrique une réponse standardisée à partir de paramètres métiers.
     *
     * <p>Cette méthode est souvent utilisée par les services
     * pour simplifier la construction d’une réponse consistante
     * sans passer par un builder explicite.</p>
     */
    public static SmsActivationResponseDTO of(Long compteId, ActivationStatus status, String message) {
        return SmsActivationResponseDTO.builder()
                .compteId(compteId)
                .status(status)
                .message(message)
                .build();
    }
}
