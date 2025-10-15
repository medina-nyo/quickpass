package com.quickpass.signup.domain.validation;

import com.quickpass.signup.domain.boulanger.constants.SignupStepBoulanger;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Vérifie la cohérence de l'ordre des étapes selon le type de flux d'inscription.
 *
 * <p>Chaque type d'utilisateur (BOULANGER, CLIENT, EMPLOYÉ, ADMIN) possède un
 * parcours d'inscription spécifique avec des étapes prédéfinies. Ce validateur
 * empêche tout saut ou retour d'étape non autorisé.</p>
 *
 * <p>Architecture orientée métier (Package by Feature) :
 * ce composant appartient au domaine "signup" et centralise la logique
 * de cohérence multi-parcours.</p>
 */
@Component
public final class StepOrderChecker {

    /** Définit les différents flux d'inscription supportés. */
    public enum FlowDefinition {

        /**
         * Parcours d'inscription d'un boulanger (backoffice web).
         */
        BOULANGER(List.of(
                SignupStepBoulanger.EMAIL,
                SignupStepBoulanger.EMAIL_CODE,
                SignupStepBoulanger.PROFILE,
                SignupStepBoulanger.SMS_CODE,
                SignupStepBoulanger.ADRESSE_MODE_VENTE,
                SignupStepBoulanger.STRIPE,
                SignupStepBoulanger.PASSWORD,
                SignupStepBoulanger.CONSENTS
        )),

        /**
         * Parcours d'inscription d'un client (app mobile).
         */
        CLIENT(List.of(
                SignupStepBoulanger.EMAIL,
                SignupStepBoulanger.EMAIL_CODE,
                SignupStepBoulanger.PASSWORD,
                SignupStepBoulanger.CONSENTS
        )),

        /**
         * Parcours d'inscription d'un employé (PWA mobile-first).
         */
        EMPLOYE(List.of(
                SignupStepBoulanger.EMAIL,
                SignupStepBoulanger.SMS_CODE,
                SignupStepBoulanger.PASSWORD,
                SignupStepBoulanger.CONSENTS
        )),

        /**
         * Parcours d'inscription d'un administrateur (backoffice sécurisé).
         */
        ADMIN(List.of(
                SignupStepBoulanger.EMAIL,
                SignupStepBoulanger.PASSWORD
        ));

        private final List<SignupStepBoulanger> orderedSteps;

        FlowDefinition(List<SignupStepBoulanger> steps) {
            this.orderedSteps = steps;
        }

        public List<SignupStepBoulanger> getOrderedSteps() {
            return orderedSteps;
        }
    }

    /** Table des flux → séquences d'étapes autorisées. */
    private static final Map<FlowDefinition, List<SignupStepBoulanger>> STEP_ORDER_MAP = new EnumMap<>(FlowDefinition.class);

    static {
        for (FlowDefinition flow : FlowDefinition.values()) {
            STEP_ORDER_MAP.put(flow, flow.getOrderedSteps());
        }
    }

    /**
     * Vérifie la validité d'une transition d'étape.
     *
     * @param flow     type de flux (BOULANGER, CLIENT, etc.)
     * @param current  étape actuelle
     * @param nextName nom de l'étape suivante (en String pour compatibilité)
     * @throws IllegalStateException si la transition est invalide ou incohérente
     */
    public static void validateOrder(FlowDefinition flow, SignupStepBoulanger current, String nextName) {
        List<SignupStepBoulanger> steps = STEP_ORDER_MAP.get(flow);

        SignupStepBoulanger next;
        try {
            next = SignupStepBoulanger.valueOf(nextName);
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("Étape inconnue : " + nextName);
        }

        int currentIndex = steps.indexOf(current);
        int nextIndex = steps.indexOf(next);

        if (nextIndex == -1)
            throw new IllegalStateException("Étape " + nextName + " non autorisée pour le flux " + flow);

        if (nextIndex <= currentIndex)
            throw new IllegalStateException("Impossible de revenir en arrière : " + current + " → " + next);

        if (nextIndex - currentIndex > 1)
            throw new IllegalStateException("Saut d'étape détecté : " + current + " → " + next);
    }

    private StepOrderChecker() {
    }
}
