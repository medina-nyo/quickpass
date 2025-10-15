package com.quickpass.signup.domain.service.factory;

import com.quickpass.signup.domain.constants.SignupType;
import com.quickpass.signup.domain.validation.StepOrderChecker;
import org.springframework.stereotype.Component;

/**
 * Fabrique responsable de la définition des flux d’inscription.
 *
 * <p>Retourne la séquence d’étapes applicable selon le type d’utilisateur.
 * Chaque type d’inscription dispose de son propre ensemble d’étapes ordonnées.</p>
 */
@Component
public class SignupFlowFactory {

    /**
     * Retourne la définition du flux correspondant au type d’utilisateur.
     *
     * @param type type d’inscription à traiter
     * @return la définition du flux applicable (non null)
     * @throws UnsupportedOperationException si le type n’est pas encore implémenté
     */
    public StepOrderChecker.FlowDefinition getFlow(SignupType type) {
        return switch (type) {
            case BOULANGER -> StepOrderChecker.FlowDefinition.BOULANGER;
            case CLIENT -> StepOrderChecker.FlowDefinition.CLIENT;
            case EMPLOYE -> StepOrderChecker.FlowDefinition.EMPLOYE;
        };
    }
}
