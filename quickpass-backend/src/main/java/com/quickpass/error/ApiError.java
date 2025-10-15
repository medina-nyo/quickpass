package com.quickpass.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

/**
 * Représente une erreur conforme à la spécification RFC 7807.
 *
 * <p>Utilisée pour toutes les réponses d’erreurs uniformisées dans l’API QuickPass.</p>
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiError {

    /** URI identifiant le type d’erreur (ex: https://api.quickpass.com/erreurs/validation) */
    private String type;

    /** Titre court décrivant le type de problème */
    private String title;

    /** Message utilisateur (détail lisible) */
    private String message;

    /** Statut HTTP associé */
    private int status;

    /** Détail technique (facultatif, utile au debug ou logs) */
    private String detail;

    /** Code d’erreur unique issu du {@link ErrorCatalog} */
    private String code;

    /** Horodatage de l’occurrence de l’erreur */
    private OffsetDateTime timestamp;
}
