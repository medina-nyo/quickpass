package com.quickpass.util;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import java.util.UUID;

/**
 * Générateur de traceId pour corréler les logs applicatifs.
 * <p>
 * Chaque requête ou opération critique est associée à un identifiant unique,
 * facilitant la traçabilité et le diagnostic en production.
 */
@Component
public class TraceIdGenerator {

    private static final String TRACE_ID_KEY = "traceId";

    /**
     * Génère et enregistre un nouveau traceId dans le contexte MDC.
     *
     * @return identifiant de trace généré
     */
    public String genererTraceId() {
        String traceId = UUID.randomUUID().toString();
        MDC.put(TRACE_ID_KEY, traceId);
        return traceId;
    }

    /**
     * Récupère le traceId courant dans le MDC.
     *
     * @return identifiant de trace ou null
     */
    public String obtenirTraceId() {
        return MDC.get(TRACE_ID_KEY);
    }

    /**
     * Supprime le traceId du MDC.
     */
    public void nettoyer() {
        MDC.remove(TRACE_ID_KEY);
    }
}
