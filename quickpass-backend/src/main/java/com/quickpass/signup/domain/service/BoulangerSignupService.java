package com.quickpass.signup.domain.service;

import java.util.UUID;
import com.quickpass.signup.domain.model.SignupSession;
import com.quickpass.signup.api.dto.boulanger.step1.SignupEmailDTO;
import com.quickpass.signup.api.dto.boulanger.step2.SignupEmailCodeDTO;
import com.quickpass.signup.api.dto.boulanger.step3.SignupProfileBoulangerDTO;
import com.quickpass.signup.api.dto.boulanger.step4.SignupSmsCodeDTO;
import com.quickpass.signup.api.dto.boulanger.step5.SignupAdresseDTO;
import com.quickpass.signup.api.dto.boulanger.step6.SignupStripeDTO;
import com.quickpass.signup.api.dto.boulanger.step7.SignupPasswordDTO;
import com.quickpass.signup.api.dto.boulanger.step8.SignupConsentsDTO;

/**
 * Service métier dédié au parcours d’inscription d’un boulanger.
 * Orchestration complète des 8 étapes.
 */
public interface BoulangerSignupService {

    SignupSession start(SignupEmailDTO dto);

    SignupSession verifyEmailCode(UUID sessionId, SignupEmailCodeDTO dto);

    SignupSession submitProfile(UUID sessionId, SignupProfileBoulangerDTO dto);

    SignupSession verifySmsCode(UUID sessionId, SignupSmsCodeDTO dto);

    SignupSession submitAdresse(UUID sessionId, SignupAdresseDTO dto);

    SignupSession submitStripe(UUID sessionId, SignupStripeDTO dto);

    SignupSession submitPassword(UUID sessionId, SignupPasswordDTO dto);

    SignupSession submitConsents(UUID sessionId, SignupConsentsDTO dto);
}
