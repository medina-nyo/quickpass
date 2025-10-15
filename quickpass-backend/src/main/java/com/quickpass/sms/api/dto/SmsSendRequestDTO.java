package com.quickpass.sms.api.dto;

import com.quickpass.common.validation.RegexPatterns;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Requête d’envoi d’un code d’activation SMS.
 *
 * <p>Endpoint: POST /api/v1/activation/{compteId}/send-sms</p>
 * <p>Le compteId est passé en path variable. Le corps contient uniquement le numéro.</p>
 */
@Getter
@Setter
@NoArgsConstructor
public class SmsSendRequestDTO {

    /**
     * Numéro de téléphone de l’utilisateur (format E.164 FR).
     * Exemple : +33612345678
     */
    @NotBlank(message = "Le numéro de téléphone est obligatoire.")
    @Pattern(
            regexp = RegexPatterns.MOBILE_PERSONNEL_FR,
            message = "Le numéro de téléphone doit être un mobile français valide (ex: +33612345678)."
    )
    private String phoneNumber;
}
