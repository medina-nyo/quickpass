package com.quickpass.integration.sms;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quickpass.sms.api.dto.SmsSendRequestDTO;
import com.quickpass.sms.api.dto.SmsVerifyRequestDTO;
import com.quickpass.sms.domain.model.ActivationSms;
import com.quickpass.sms.domain.model.enums.ActivationStatus;
import com.quickpass.sms.domain.repo.ActivationSmsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ActivationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ActivationSmsRepository activationSmsRepository;

    @BeforeEach
    void setup() {
        activationSmsRepository.deleteAll();
        activationSmsRepository.save(
                ActivationSms.builder()
                        .compteId(1L)
                        .codeHash(new Argon2PasswordEncoder(16, 32, 1, 65536, 3).encode("123456"))
                        .expiresAt(LocalDateTime.now().plusMinutes(10))
                        .status(ActivationStatus.EN_ATTENTE)
                        .build()
        );
    }

    @Test
    @DisplayName("Envoi d’un SMS: 200 OK avec numéro valide")
    void shouldSendSmsSuccessfully() throws Exception {
        SmsSendRequestDTO request = new SmsSendRequestDTO();
        request.setPhoneNumber("+33612345678");

        mockMvc.perform(post("/api/v1/activation/{compteId}/send-sms", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Vérification d’un code valide: 200 OK")
    void shouldVerifySmsSuccessfully() throws Exception {
        SmsVerifyRequestDTO request = new SmsVerifyRequestDTO();
        request.setCode("123456"); // le compteId est dans le path

        mockMvc.perform(post("/api/v1/activation/{compteId}/verify-sms", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Vérification sans code: 400 BAD REQUEST (validation)")
    void verifySms_missingCode_returnsBadRequest() throws Exception {
        // body vide => déclenche la validation @NotBlank sur code
        mockMvc.perform(post("/api/v1/activation/{compteId}/verify-sms", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }
}
