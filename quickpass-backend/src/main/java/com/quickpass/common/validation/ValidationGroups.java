package com.quickpass.common.validation;

/**
 * Groupes de validation Jakarta Bean Validation.
 * <p>
 * Permet d'appliquer des validations différentes selon l’étape
 * du processus d’inscription.
 */
public interface ValidationGroups {

    interface EtapeEmail {}
    interface EtapeTelephone {}
    interface EtapeMotDePasse {}
    interface EtapeProfil {}
    interface EtapeAdresse {}
}
