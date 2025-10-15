package com.quickpass.integration.signup;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quickpass.account.domain.service.PreferenceService;
import com.quickpass.common.constants.ModeVente;
import com.quickpass.signup.api.dto.boulanger.step1.SignupEmailDTO;
import com.quickpass.signup.api.dto.boulanger.step3.SignupProfileBoulangerDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Import(BoulangerSignupControllerTest.TestBeans.class)
class BoulangerSignupControllerTest {

    @TestConfiguration
    static class TestBeans {
        @Bean
        PreferenceService preferenceService() {
            return compteId -> { /* no-op */ };
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private SignupEmailDTO validEmailDto;
    private SignupProfileBoulangerDTO validProfileDto;

    @BeforeEach
    void setUp() {
        validEmailDto = new SignupEmailDTO("jean.dupont@example.com");
        validProfileDto = new SignupProfileBoulangerDTO(
                "Jean",
                "Dupont",
                "+33612345678",
                "Boulangerie du Coin",
                "12345678901234",
                "+33123456789",
                ModeVente.SUR_PLACE
        );
    }

    @Test
    @DisplayName("Step 1 — /start : doit créer une session et retourner data.sessionId")
    void shouldStartSignupSuccessfully() throws Exception {
        mockMvc.perform(post("/api/v1/signup/start")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validEmailDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.sessionId").exists());
    }

    @Test
    @DisplayName("Step 3 — /{id}/profile : SIRET invalide → 400 RFC7807 (VALIDATION_ERREUR)")
    void shouldFailWithInvalidSiret() throws Exception {
        SignupProfileBoulangerDTO invalid = new SignupProfileBoulangerDTO(
                "Jean",
                "Dupont",
                "+33612345678",
                "Boulangerie du Coin",
                "1234", // <- invalide
                "+33123456789",
                ModeVente.SUR_PLACE
        );

        mockMvc.perform(put("/api/v1/signup/1/profile")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERREUR"))
                .andExpect(jsonPath("$.title").value("Erreur de validation"))
                .andExpect(jsonPath("$.detail").exists());
    }

    @Test
    @DisplayName("Step 6 — /{id}/stripe : stripeAccountId manquant → 400 RFC7807 (VALIDATION_ERREUR)")
    void shouldRejectStripeWithoutAccountId() throws Exception {
        String stripeJson = """
            { "stripeAccountId": "" }
            """;

        mockMvc.perform(put("/api/v1/signup/1/stripe")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(stripeJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERREUR"))
                .andExpect(jsonPath("$.title").value("Erreur de validation"))
                .andExpect(jsonPath("$.detail").exists());
    }

    @Test
    @DisplayName("Step 8 — /{id}/consents : consentements manquants → 400 RFC7807 (VALIDATION_ERREUR)")
    void shouldFailIfConsentsMissing() throws Exception {
        String consentsJson = """
            {
              "accepteCgu": false,
              "accepteRgpd": false,
              "accepteStripe": false
            }
            """;

        mockMvc.perform(put("/api/v1/signup/1/consents")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(consentsJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERREUR"))
                .andExpect(jsonPath("$.title").value("Erreur de validation"))
                .andExpect(jsonPath("$.detail").exists());
    }
}
