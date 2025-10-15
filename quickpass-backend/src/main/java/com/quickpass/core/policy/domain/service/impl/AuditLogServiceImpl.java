package com.quickpass.core.policy.domain.service.impl;

import com.quickpass.core.policy.domain.model.AuditLog;
import com.quickpass.core.policy.domain.model.constants.AuditAction;
import com.quickpass.core.policy.domain.model.constants.AuditStatus;
import com.quickpass.core.policy.domain.repo.AuditLogRepository;
import com.quickpass.core.policy.domain.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Implémentation transactionnelle du service d’audit.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class AuditLogServiceImpl implements AuditLogService {

    private final AuditLogRepository repository;

    @Override
    public void enregistrer(Long compteId, AuditAction action, AuditStatus status, String detail) {
        AuditLog log = AuditLog.builder()
                .compteId(compteId)
                .action(action)
                .status(status)
                .detail(detail)
                .timestamp(LocalDateTime.now())
                .build();
        repository.save(log);
    }
}
