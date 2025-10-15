package com.quickpass.error;

import com.quickpass.error.exception.BusinessException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.OffsetDateTime;
import java.util.stream.Collectors;

/**
 * Gestion centralisée des exceptions selon la RFC 7807.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String RETRY_AFTER_SECONDS = "900";

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiError> handleBusinessException(BusinessException ex) {
        ErrorCatalog catalog = ex.getErrorCatalog();
        ApiError error = new ApiError(
                "https://api.quickpass.com/erreurs/" + catalog.getCode().toLowerCase(),
                catalog.name(),
                catalog.getMessage(),
                catalog.getStatus().value(),
                ex.getDetail(),
                catalog.getCode(),
                OffsetDateTime.now()
        );
        HttpHeaders headers = new HttpHeaders();
        if (catalog == ErrorCatalog.SMS_MAX_ATTEMPTS || catalog == ErrorCatalog.SMS_RATE_LIMIT) {
            headers.add(HttpHeaders.RETRY_AFTER, RETRY_AFTER_SECONDS);
        }
        return new ResponseEntity<>(error, headers, catalog.getStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex) {
        String details = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .collect(Collectors.joining(", "));
        ApiError error = new ApiError(
                "https://api.quickpass.com/erreurs/validation",
                "Erreur de validation",
                "Certains champs sont invalides.",
                HttpStatus.BAD_REQUEST.value(),
                details,
                "VALIDATION_ERREUR",
                OffsetDateTime.now()
        );
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest req) {
        ApiError error = new ApiError(
                "https://api.quickpass.com/erreurs/requete",
                "Requête invalide",
                ex.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                req.getRequestURI(),
                "REQUETE_INVALIDE",
                OffsetDateTime.now()
        );
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneric(Exception ex) {
        ApiError error = new ApiError(
                "https://api.quickpass.com/erreurs/interne",
                "Erreur interne du serveur",
                ex.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ex.getClass().getSimpleName(),
                "ERREUR_INTERNE",
                OffsetDateTime.now()
        );
        return ResponseEntity.internalServerError().body(error);
    }
}
