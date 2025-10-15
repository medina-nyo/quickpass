package com.quickpass.core.policy.domain.service;

import com.quickpass.core.policy.domain.model.constants.AuditAction;
import com.quickpass.core.policy.domain.model.constants.AuditStatus;

/**
 * Service métier de gestion des journaux d’audit.
 */
public interface AuditLogService {

    /**
     * Enregistre une action auditée dans le système.
     *
     * @param compteId identifiant du compte concerné
     * @param action   type d’action auditée
     * @param status   résultat de l’action
     * @param detail   message optionnel
     */
    void enregistrer(Long compteId, AuditAction action, AuditStatus status, String detail);
}
