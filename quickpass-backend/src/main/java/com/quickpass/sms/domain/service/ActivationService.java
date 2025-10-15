package com.quickpass.sms.domain.service;

/**
 * Service métier responsable de la gestion des activations SMS.
 *
 * <p>Fournit les opérations principales liées à la validation de compte
 * par code OTP :</p>
 * <ul>
 *   <li>Envoi d’un nouveau code sécurisé à un numéro de téléphone</li>
 *   <li>Vérification du code reçu par l’utilisateur</li>
 *   <li>Application des règles de sécurité (rate-limit, TTL, tentatives)</li>
 * </ul>
 */
public interface ActivationService {

    void sendSms(Long compteId, String phoneNumber);

    boolean verifySms(Long compteId, String code);
}
