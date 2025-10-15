package com.quickpass.unit.error;

import com.quickpass.error.ErrorCatalog;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Vérifie la cohérence du catalogue d'erreurs métier.
 */
class ErrorCatalogTest {

    @Test
    void shouldContainAllExpectedErrors() {
        assertNotNull(ErrorCatalog.SIGNUP_EMAIL_EXISTS);
        assertNotNull(ErrorCatalog.SMS_INVALID_CODE);
        assertNotNull(ErrorCatalog.SMS_MAX_ATTEMPTS);
        assertNotNull(ErrorCatalog.ACCOUNT_LOCKED);
    }

    @Test
    void shouldReturnCorrectHttpStatus() {
        assertEquals(409, ErrorCatalog.SIGNUP_EMAIL_EXISTS.getStatus().value());
        assertEquals(400, ErrorCatalog.SMS_INVALID_CODE.getStatus().value());
        assertEquals(429, ErrorCatalog.SMS_MAX_ATTEMPTS.getStatus().value());
        assertEquals(423, ErrorCatalog.ACCOUNT_LOCKED.getStatus().value());
    }
}
