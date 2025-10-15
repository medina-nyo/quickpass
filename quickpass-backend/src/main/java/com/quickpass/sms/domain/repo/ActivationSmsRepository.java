package com.quickpass.sms.domain.repo;

import com.quickpass.sms.domain.model.ActivationSms;
import com.quickpass.sms.domain.model.enums.ActivationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Repository JPA pour la persistance des entités {@link ActivationSms}.
 */
public interface ActivationSmsRepository extends JpaRepository<ActivationSms, Long> {

    Optional<ActivationSms> findByCompteIdAndStatus(Long compteId, ActivationStatus status);

    @Query("""
           SELECT COUNT(a)
           FROM ActivationSms a
           WHERE a.compteId = :compteId
             AND a.createdAt >= :since
           """)
    long countRecentSends(@Param("compteId") Long compteId,
                          @Param("since") LocalDateTime since);
}
