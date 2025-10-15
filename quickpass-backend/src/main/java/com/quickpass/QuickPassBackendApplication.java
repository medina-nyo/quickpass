package com.quickpass;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Point d’entrée principal de l’application QuickPass Backend.
 *
 * <p>Active le scheduling Spring pour les tâches planifiées (RGPD cleanup, sessions, etc.).</p>
 */
@SpringBootApplication
@EnableScheduling
public class QuickPassBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuickPassBackendApplication.class, args);
	}
}
