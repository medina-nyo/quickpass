package com.quickpass.signup.domain.service.impl;

import com.quickpass.signup.api.dto.boulanger.step1.SignupEmailDTO;
import com.quickpass.signup.api.dto.boulanger.step2.SignupEmailCodeDTO;
import com.quickpass.signup.api.dto.boulanger.step3.SignupProfileBoulangerDTO;
import com.quickpass.signup.api.dto.boulanger.step4.SignupSmsCodeDTO;
import com.quickpass.signup.api.dto.boulanger.step5.SignupAdresseDTO;
import com.quickpass.signup.api.dto.boulanger.step6.SignupStripeDTO;
import com.quickpass.signup.api.dto.boulanger.step7.SignupPasswordDTO;
import com.quickpass.signup.api.dto.boulanger.step8.SignupConsentsDTO;
import com.quickpass.signup.domain.model.SignupSession;
import com.quickpass.signup.domain.service.BoulangerSignupService;
import org.springframework.stereotype.Service;
import java.util.UUID;

/**
 * Implémentation du service d’inscription spécifique aux boulangers.
 * Chaque méthode gère une étape du parcours :
 * - Validation des données
 * - Vérification de l’ordre
 * - Mise à jour de la session
 */
@Service
public class BoulangerSignupServiceImpl implements BoulangerSignupService {

    @Override
    public SignupSession start(SignupEmailDTO dto) {
        // TODO: vérifier unicité e-mail + créer Compte + Session
        return null;
    }

    @Override
    public SignupSession verifyEmailCode(UUID sessionId, SignupEmailCodeDTO dto) {
        // TODO: vérifier code OTP e-mail
        return null;
    }

    @Override
    public SignupSession submitProfile(UUID sessionId, SignupProfileBoulangerDTO dto) {
        // TODO: compléter profil + créer entité Boulanger
        return null;
    }

    @Override
    public SignupSession verifySmsCode(UUID sessionId, SignupSmsCodeDTO dto) {
        // TODO: valider OTP SMS
        return null;
    }

    @Override
    public SignupSession submitAdresse(UUID sessionId, SignupAdresseDTO dto) {
        // TODO: enregistrer adresse + mode de vente
        return null;
    }

    @Override
    public SignupSession submitStripe(UUID sessionId, SignupStripeDTO dto) {
        // TODO: enregistrer compte Stripe
        return null;
    }

    @Override
    public SignupSession submitPassword(UUID sessionId, SignupPasswordDTO dto) {
        // TODO: hash mot de passe + vérifier policy
        return null;
    }

    @Override
    public SignupSession submitConsents(UUID sessionId, SignupConsentsDTO dto) {
        // TODO: activer le compte et marquer session comme complète
        return null;
    }
}
