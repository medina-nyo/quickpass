package com.quickpass.core.policy.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Exposition de métriques applicatives (Prometheus / Micrometer)
 * pour suivre la performance et la fiabilité des opérations critiques.
 */
@Component
@RequiredArgsConstructor
public class MonitoringMetrics {

    private final MeterRegistry registry;

    /**
     * Incrémente le compteur d’envois SMS.
     */
    public void incrementSmsSent() {
        Counter.builder("sms_send_count").register(registry).increment();
    }

    /**
     * Incrémente le compteur d’échecs d’envoi SMS.
     */
    public void incrementSmsFailed() {
        Counter.builder("sms_fail_rate").register(registry).increment();
    }

    /**
     * Incrémente le compteur de réussites d’activation.
     */
    public void incrementActivationSuccess() {
        Counter.builder("activation_success_rate").register(registry).increment();
    }
}
