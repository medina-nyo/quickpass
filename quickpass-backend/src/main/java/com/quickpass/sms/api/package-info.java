/**
 * Couche API REST du module SMS.
 *
 * <p>Expose les endpoints d’activation SMS :</p>
 * <ul>
 *   <li><b>POST</b> /api/v1/activation/{compteId}/send-sms – envoi d’un code OTP sécurisé</li>
 *   <li><b>POST</b> /api/v1/activation/{compteId}/verify-sms – vérification du code reçu</li>
 * </ul>
 *
 * <p>Cette couche s’appuie sur {@link com.quickpass.sms.domain.service.ActivationService}
 * pour la logique métier (génération, envoi et vérification des codes)
 * et renvoie des {@link com.quickpass.common.api.ApiResponse} standardisées.</p>
 */
package com.quickpass.sms.api;
