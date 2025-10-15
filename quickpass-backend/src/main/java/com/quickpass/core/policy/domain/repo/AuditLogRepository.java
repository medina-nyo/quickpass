package com.quickpass.core.policy.domain.repo;

import com.quickpass.core.policy.domain.model.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository JPA pour la gestion des enregistrements d’audit.
 */
@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
}
