package com.quickpass.signup.domain.boulanger.constants;

/**
 * Représente les différentes étapes du parcours d’inscription du boulanger.
 * Chaque étape correspond à un DTO et une action spécifique dans le flux.
 * <p>
 * Utilisé par :
 * <ul>
 *   <li>SignupSession – pour suivre la progression de l’inscription</li>
 *   <li>SignupService – pour contrôler l’ordre et la validation des étapes</li>
 * </ul>
 * <p>
 * Ce fichier est spécifique au parcours du boulanger
 * afin de permettre la coexistence future d’autres flux
 * (ex : client, employé) sans collision.
 */
public enum SignupStepBoulanger {
    EMAIL,
    EMAIL_CODE,
    PROFILE,
    SMS_CODE,
    ADRESSE_MODE_VENTE,
    STRIPE,
    PASSWORD,
    CONSENTS;
}
