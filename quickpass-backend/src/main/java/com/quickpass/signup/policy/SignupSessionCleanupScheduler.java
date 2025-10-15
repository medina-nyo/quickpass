package com.quickpass.signup.policy;

import com.quickpass.signup.domain.repo.SignupSessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Tâche planifiée responsable du nettoyage des sessions d’inscription expirées.
 *
 * <p>Cette opération est exécutée quotidiennement à 02h00, afin d’assurer
 * la conformité RGPD et d’éviter la rétention de données temporaires inutiles.</p>
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SignupSessionCleanupScheduler {

    private final SignupSessionRepository signupSessionRepository;

    /**
     * Supprime les sessions expirées de la base de données.
     */
    @Transactional
    @Scheduled(cron = "0 0 2 * * *", zone = "Europe/Paris")
    public void cleanExpiredSessions() {
        LocalDateTime now = LocalDateTime.now();
        int deleted = signupSessionRepository.deleteExpiredSessions(now);

        if (deleted > 0) {
            log.info("{} sessions d’inscription expirées supprimées à {}", deleted, now);
        } else {
            log.debug("Aucune session expirée détectée à {}", now);
        }
    }
}
