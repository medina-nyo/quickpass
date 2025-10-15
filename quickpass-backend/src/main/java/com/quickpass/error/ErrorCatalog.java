package com.quickpass.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCatalog {

    SIGNUP_EMAIL_EXISTS("SIGNUP_EMAIL_EXISTS", "Cette adresse email est déjà utilisée.", HttpStatus.CONFLICT),
    SIGNUP_SESSION_EXPIRED("SIGNUP_SESSION_EXPIRED", "La session d’inscription a expiré.", HttpStatus.GONE),

    SMS_INVALID_CODE("SMS_INVALID_CODE", "Le code SMS saisi est invalide.", HttpStatus.BAD_REQUEST),
    SMS_MAX_ATTEMPTS("SMS_MAX_ATTEMPTS", "Nombre maximum de tentatives atteint.", HttpStatus.TOO_MANY_REQUESTS),
    SMS_RATE_LIMIT("SMS_RATE_LIMIT", "Trop de SMS envoyés cette heure-ci (limite : 3/h).", HttpStatus.TOO_MANY_REQUESTS),
    SMS_NO_PENDING("SMS_NO_PENDING", "Aucun SMS actif trouvé pour ce compte.", HttpStatus.NOT_FOUND),
    SMS_EXPIRED("SMS_EXPIRED", "Le code SMS est expiré.", HttpStatus.GONE),

    ACCOUNT_LOCKED("ACCOUNT_LOCKED", "Ce compte est temporairement bloqué.", HttpStatus.LOCKED);

    private final String code;
    private final String message;
    private final HttpStatus status;

    ErrorCatalog(String code, String message, HttpStatus status) {
        this.code = code;
        this.message = message;
        this.status = status;
    }
}
