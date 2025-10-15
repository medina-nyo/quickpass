package com.quickpass.signup.api.dto.boulanger.step8;

import jakarta.validation.constraints.AssertTrue;

/**
 * Étape 8 – Validation des consentements légaux.
 * Inclut :
 * - Acceptation des CGU
 * - Consentement RGPD
 * - Validation des conditions Stripe
 */
public record SignupConsentsDTO(
        @AssertTrue(message = "cgu.non_acceptees")
        boolean accepteCgu,

        @AssertTrue(message = "rgpd.non_accepte")
        boolean accepteRgpd,

        @AssertTrue(message = "stripe.non_accepte")
        boolean accepteStripe
) {}
