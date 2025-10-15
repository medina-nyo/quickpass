package com.quickpass.signup.domain.service;

import com.quickpass.signup.domain.boulanger.constants.SignupStepBoulanger;
import com.quickpass.signup.domain.constants.SignupType;
import com.quickpass.signup.domain.model.SignupSession;

/**
 * Service central gérant le flux d’inscription des utilisateurs.
 *
 * <p>Cette interface définit les opérations principales nécessaires
 * pour créer et suivre la progression d’une session d’inscription,
 * qu’il s’agisse d’un boulanger, client ou employé.</p>
 *
 * <ul>
 *     <li>Création et initialisation d’une nouvelle session</li>
 *     <li>Avancement entre les différentes étapes du parcours</li>
 *     <li>Validation finale de l’inscription</li>
 *     <li>Récupération d’une session existante</li>
 * </ul>
 */
public interface SignupService {

    /**
     * Démarre une nouvelle session d’inscription.
     *
     * @param email       adresse e-mail utilisée pour l’inscription
     * @param signupType  type d’inscription (BOULANGER, CLIENT, etc.)
     * @return la session d’inscription nouvellement créée
     */
    SignupSession start(String email, SignupType signupType);

    /**
     * Fait progresser la session à l’étape suivante du parcours d’inscription.
     *
     * @param sessionId identifiant unique de la session d’inscription
     * @param nextStep  prochaine étape à valider
     */
    void advanceStep(Long sessionId, SignupStepBoulanger nextStep);

    /**
     * Marque la session comme terminée une fois tous les consentements validés.
     *
     * @param sessionId identifiant unique de la session d’inscription
     */
    void complete(Long sessionId);

    /**
     * Récupère une session d’inscription à partir de son identifiant.
     *
     * @param sessionId identifiant unique de la session d’inscription
     * @return la session correspondante
     */
    SignupSession getById(Long sessionId);
}
