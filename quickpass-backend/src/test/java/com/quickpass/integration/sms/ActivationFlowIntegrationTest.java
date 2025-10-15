package com.quickpass.integration.sms;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quickpass.sms.api.dto.SmsSendRequestDTO;
import com.quickpass.sms.api.dto.SmsVerifyRequestDTO;
import com.quickpass.sms.domain.model.ActivationSms;
import com.quickpass.sms.domain.model.enums.ActivationStatus;
import com.quickpass.sms.domain.repo.ActivationSmsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ActivationFlowIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ActivationSmsRepository activationSmsRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    private Long compteId;
    private String phoneNumber;

    @BeforeEach
    void setUp() {
        compteId = 1L;
        phoneNumber = "+33612345678";
    }

    @Test
    void shouldSendAndVerifySmsAndMarkAsVerified() throws Exception {
        SmsSendRequestDTO sendRequest = new SmsSendRequestDTO();
        sendRequest.setPhoneNumber(phoneNumber);

        mockMvc.perform(post("/api/v1/activation/{compteId}/send-sms", compteId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sendRequest)))
                .andExpect(status().isOk());

        Optional<ActivationSms> pendingOpt = activationSmsRepository.findByCompteIdAndStatus(compteId, ActivationStatus.EN_ATTENTE);
        assertThat(pendingOpt).isPresent();

        ActivationSms pending = pendingOpt.get();
        String knownCode = "654321";
        pending.setCodeHash(passwordEncoder.encode(knownCode));
        activationSmsRepository.save(pending);

        SmsVerifyRequestDTO verifyRequest = new SmsVerifyRequestDTO();
        verifyRequest.setCode(knownCode);

        mockMvc.perform(post("/api/v1/activation/{compteId}/verify-sms", compteId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(verifyRequest)))
                .andExpect(status().isOk());

        Optional<ActivationSms> verifiedOpt = activationSmsRepository.findByCompteIdAndStatus(compteId, ActivationStatus.VERIFIE);
        assertThat(verifiedOpt).isPresent();
    }
}
