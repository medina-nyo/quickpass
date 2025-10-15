package com.quickpass.signup.domain.repo;

import com.quickpass.signup.domain.model.SignupSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;

/**
 * Repository de persistance pour les sessions d’inscription.
 */
@Repository
public interface SignupSessionRepository extends JpaRepository<SignupSession, Long> {

    @Modifying
    @Query("DELETE FROM SignupSession s WHERE s.expiresAt < :cutoff")
    int deleteExpiredSessions(@Param("cutoff") LocalDateTime cutoff);
}
