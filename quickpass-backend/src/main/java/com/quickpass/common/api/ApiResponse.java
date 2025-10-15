package com.quickpass.common.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Structure standard de réponse API pour QuickPass.
 *
 * <p>Toutes les réponses, qu’elles soient de succès ou d’erreur,
 * suivent cette structure pour assurer la cohérence entre les endpoints.</p>
 *
 * @param <T> type du contenu retourné
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    /**
     * Indique si la requête s’est terminée avec succès.
     */
    private boolean success;

    /**
     * Contenu utile de la réponse, uniquement présent en cas de succès.
     */
    private T data;

    /**
     * Code d’erreur métier ou HTTP (ex: SIGNUP_EMAIL_EXISTS, 400).
     */
    private String code;

    /**
     * Message lisible par le développeur ou l’utilisateur.
     */
    private String message;

    /**
     * Date/heure de génération de la réponse.
     */
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();


    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .data(data)
                .build();
    }

    public static ApiResponse<Void> success(String message) {
        return ApiResponse.<Void>builder()
                .success(true)
                .message(message)
                .build();
    }

    public static ApiResponse<Void> error(String code, String message) {
        return ApiResponse.<Void>builder()
                .success(false)
                .code(code)
                .message(message)
                .build();
    }
}
