package com.quickpass.core.policy.domain.model.constants;

/**
 * Liste des actions métier auditées.
 */
public enum AuditAction {
    SMS_SEND,
    SMS_VERIFY,
    STRIPE_CONNECT,
    STRIPE_ERROR
}
