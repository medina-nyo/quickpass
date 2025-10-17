package com.quickpass.signup.api.controller;

import com.quickpass.common.api.ApiResponse;
import com.quickpass.signup.api.dto.boulanger.step1.SignupEmailDTO;
import com.quickpass.signup.api.dto.boulanger.step2.SignupEmailCodeDTO;
import com.quickpass.signup.api.dto.boulanger.step3.SignupProfileBoulangerDTO;
import com.quickpass.signup.api.dto.boulanger.step4.SignupSmsCodeDTO;
import com.quickpass.signup.api.dto.boulanger.step5.SignupAdresseDTO;
import com.quickpass.signup.api.dto.boulanger.step6.SignupStripeDTO;
import com.quickpass.signup.api.dto.boulanger.step7.SignupPasswordDTO;
import com.quickpass.signup.api.dto.boulanger.step8.SignupConsentsDTO;
import com.quickpass.signup.domain.boulanger.constants.SignupStepBoulanger;
import com.quickpass.signup.domain.constants.SignupType;
import com.quickpass.signup.domain.model.SignupSession;
import com.quickpass.signup.domain.service.SignupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

/**
 * Contrôleur REST gérant le parcours d’inscription des boulangers.
 *
 * <p>Ce contrôleur expose les huit étapes du flux d’inscription,
 * depuis la saisie de l’adresse e-mail jusqu’à la validation des consentements.</p>
 */
@RestController
@RequestMapping("/api/v1/signup")
@RequiredArgsConstructor
public class BoulangerSignupController {

    private final SignupService signupService;

    /**
     * Étape 1 — Démarre une nouvelle session d’inscription.
     *
     * @param dto données du formulaire e-mail
     * @return identifiant de la session créée
     */
    @PostMapping("/start")
    public ResponseEntity<ApiResponse<Map<String, Object>>> start(@Valid @RequestBody SignupEmailDTO dto) {
        SignupSession session = signupService.start(dto.email(), SignupType.BOULANGER);
        return ResponseEntity.ok(ApiResponse.success(Map.of("sessionId", session.getId())));
    }

    /**
     * Bloque les requêtes GET sur /signup/start.
     *
     * @return réponse d’erreur 405 (Method Not Allowed)
     */
    @GetMapping("/start")
    public ResponseEntity<ApiResponse<Void>> methodNotAllowed() {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(ApiResponse.error("METHOD_NOT_ALLOWED", "Utilisez POST sur /signup/start"));
    }

    /**
     * Étape 2 — Vérifie le code e-mail reçu par le boulanger.
     *
     * @param sessionId identifiant de la session
     * @param dto données de vérification du code e-mail
     * @return confirmation de validation
     */
    @PostMapping("/{sessionId}/email-code")
    public ResponseEntity<ApiResponse<Void>> verifyEmailCode(
            @PathVariable Long sessionId,
            @Valid @RequestBody SignupEmailCodeDTO dto) {
        signupService.advanceStep(sessionId, SignupStepBoulanger.EMAIL_CODE);
        return ResponseEntity.ok(ApiResponse.success("Code e-mail validé."));
    }

    /**
     * Étape 3 — Enregistre les informations du profil boulanger.
     *
     * @param sessionId identifiant de la session
     * @param dto données du profil boulanger
     * @return confirmation d’enregistrement
     */
    @PutMapping("/{sessionId}/profile")
    public ResponseEntity<ApiResponse<Void>> submitProfile(
            @PathVariable Long sessionId,
            @Valid @RequestBody SignupProfileBoulangerDTO dto) {
        signupService.advanceStep(sessionId, SignupStepBoulanger.PROFILE);
        return ResponseEntity.ok(ApiResponse.success("Profil boulanger enregistré."));
    }

    /**
     * Étape 4 — Vérifie le code SMS envoyé au boulanger.
     *
     * @param sessionId identifiant de la session
     * @param dto données de vérification du code SMS
     * @return confirmation de validation
     */
    @PostMapping("/{sessionId}/sms-code")
    public ResponseEntity<ApiResponse<Void>> verifySmsCode(
            @PathVariable Long sessionId,
            @Valid @RequestBody SignupSmsCodeDTO dto) {
        signupService.advanceStep(sessionId, SignupStepBoulanger.SMS_CODE);
        return ResponseEntity.ok(ApiResponse.success("Code SMS validé."));
    }

    /**
     * Étape 5 — Soumet l’adresse et le mode de vente du commerce.
     *
     * @param sessionId identifiant de la session
     * @param dto données d’adresse et de mode de vente
     * @return confirmation d’enregistrement
     */
    @PutMapping("/{sessionId}/adresse")
    public ResponseEntity<ApiResponse<Void>> submitAdresse(
            @PathVariable Long sessionId,
            @Valid @RequestBody SignupAdresseDTO dto) {
        signupService.advanceStep(sessionId, SignupStepBoulanger.ADRESSE_MODE_VENTE);
        return ResponseEntity.ok(ApiResponse.success("Adresse enregistrée."));
    }

    /**
     * Étape 6 — Configure l’intégration Stripe pour les paiements.
     *
     * @param sessionId identifiant de la session
     * @param dto données de configuration Stripe
     * @return confirmation d’enregistrement
     */
    @PutMapping("/{sessionId}/stripe")
    public ResponseEntity<ApiResponse<Void>> submitStripe(
            @PathVariable Long sessionId,
            @Valid @RequestBody SignupStripeDTO dto) {
        signupService.advanceStep(sessionId, SignupStepBoulanger.STRIPE);
        return ResponseEntity.ok(ApiResponse.success("Configuration Stripe enregistrée."));
    }

    /**
     * Étape 7 — Définit le mot de passe du compte.
     *
     * @param sessionId identifiant de la session
     * @param dto mot de passe à enregistrer
     * @return confirmation d’enregistrement
     */
    @PutMapping("/{sessionId}/password")
    public ResponseEntity<ApiResponse<Void>> submitPassword(
            @PathVariable Long sessionId,
            @Valid @RequestBody SignupPasswordDTO dto) {
        signupService.advanceStep(sessionId, SignupStepBoulanger.PASSWORD);
        return ResponseEntity.ok(ApiResponse.success("Mot de passe enregistré."));
    }

    /**
     * Étape 8 — Valide les consentements légaux et termine l’inscription.
     *
     * @param sessionId identifiant de la session
     * @param dto consentements RGPD
     * @return confirmation de fin d’inscription
     */
    @PutMapping("/{sessionId}/consents")
    public ResponseEntity<ApiResponse<Void>> submitConsents(
            @PathVariable Long sessionId,
            @Valid @RequestBody SignupConsentsDTO dto) {
        signupService.advanceStep(sessionId, SignupStepBoulanger.CONSENTS);
        signupService.complete(sessionId);
        return ResponseEntity.ok(ApiResponse.success("Consentements validés, inscription terminée."));
    }
}