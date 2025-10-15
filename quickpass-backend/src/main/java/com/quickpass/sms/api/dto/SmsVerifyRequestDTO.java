package com.quickpass.sms.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Requête pour la vérification d'un code SMS.
 *
 * <p>Utilisée dans l'endpoint :
 * <b>POST /api/v1/activation/{compteId}/verify-sms</b></p>
 *
 * <p>Le {@code compteId} est fourni dans le chemin de l'URL,
 * et non dans le corps JSON.</p>
 */
@Getter
@Setter
@NoArgsConstructor
@Schema(description = "Requête contenant le code d’activation SMS à vérifier.")
public class SmsVerifyRequestDTO {

    /**
     * Code SMS saisi par l'utilisateur.
     */
    @NotBlank(message = "Le code SMS est obligatoire.")
    @Schema(example = "123456", description = "Code reçu par SMS (6 chiffres)")
    private String code;
}
