package com.quickpass.error.exception;

import com.quickpass.error.ErrorCatalog;
import lombok.Getter;

/**
 * Exception métier utilisée pour signaler une erreur fonctionnelle
 * de façon contrôlée, avec un code d’erreur centralisé dans {@link ErrorCatalog}.
 *
 * <p>Elle contient à la fois :</p>
 * <ul>
 *   <li>un code unique (ex : <code>SMS_EXPIRED</code>)</li>
 *   <li>un message utilisateur lisible (ex : <code>Le code SMS est expiré.</code>)</li>
 *   <li>un éventuel détail technique pour le débogage</li>
 * </ul>
 *
 * <p>En entreprise, ce format est standard pour faciliter :
 * la journalisation, la traçabilité et les tests unitaires/assertions.</p>
 */
@Getter
public class BusinessException extends RuntimeException {

    /** Code d’erreur centralisé dans le catalogue. */
    private final ErrorCatalog errorCatalog;

    /** Détail complémentaire (facultatif) pour le débogage. */
    private final String detail;

    /**
     * Construit une nouvelle exception métier.
     *
     * @param errorCatalog le code d’erreur issu du catalogue global
     * @param detail       le détail optionnel du contexte de l’erreur
     */
    public BusinessException(ErrorCatalog errorCatalog, String detail) {
        super(errorCatalog.name() + " : " + errorCatalog.getMessage());
        this.errorCatalog = errorCatalog;
        this.detail = detail;
    }
}
