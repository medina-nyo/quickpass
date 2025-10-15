package com.quickpass.unit.sms;

import com.quickpass.sms.domain.model.ActivationSms;
import com.quickpass.sms.domain.repo.ActivationSmsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ActivationServiceRateLimitTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ActivationSmsRepository activationSmsRepository;

    private final Long compteId = 42L;
    private final String phoneNumber = "+33698765432";

    @BeforeEach
    void clean() {
        activationSmsRepository.deleteAll();
    }

    @Test
    void shouldLockAfterFiveInvalidAttempts() throws Exception {
        // Envoi initial du SMS
        mockMvc.perform(post("/api/v1/activation/{compteId}/send-sms", compteId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"phoneNumber\": \"" + phoneNumber + "\"}"))
                .andExpect(status().isOk());

        // 5 tentatives invalides
        for (int i = 0; i < 5; i++) {
            mockMvc.perform(post("/api/v1/activation/{compteId}/verify-sms", compteId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"code\": \"000000\"}"))
                    .andExpect(status().isBadRequest());
        }

        // 6e tentative -> bloquée (429)
        mockMvc.perform(post("/api/v1/activation/{compteId}/verify-sms", compteId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"code\": \"000000\"}"))
                .andExpect(status().isTooManyRequests())
                .andExpect(header().string(HttpHeaders.RETRY_AFTER, "900"))
                .andExpect(jsonPath("$.code").value("SMS_MAX_ATTEMPTS"))
                .andExpect(jsonPath("$.message").value("Nombre maximum de tentatives atteint."));

        List<ActivationSms> all = activationSmsRepository.findAll();
        assertThat(all).isNotEmpty();
        ActivationSms sms = all.get(0);
        assertThat(sms.getAttempts()).isGreaterThanOrEqualTo(sms.getMaxAttempts());
        assertThat(sms.getExpiresAt()).isNotNull();
    }
}
