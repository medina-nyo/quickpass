package com.quickpass.sms.api.controller;

import com.quickpass.common.api.ApiResponse;
import com.quickpass.sms.api.dto.SmsSendRequestDTO;
import com.quickpass.sms.api.dto.SmsVerifyRequestDTO;
import com.quickpass.sms.api.response.SmsActivationResponseDTO;
import com.quickpass.sms.domain.model.enums.ActivationStatus;
import com.quickpass.sms.domain.service.ActivationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/activation")
@RequiredArgsConstructor
public class ActivationController {

    private final ActivationService activationService;

    @PostMapping("/{compteId}/send-sms")
    public ResponseEntity<ApiResponse<SmsActivationResponseDTO>> sendSms(
            @PathVariable Long compteId,
            @Valid @RequestBody SmsSendRequestDTO request) {

        activationService.sendSms(compteId, request.getPhoneNumber());

        var response = new SmsActivationResponseDTO(
                compteId,
                ActivationStatus.EN_ATTENTE,
                "Code d’activation envoyé avec succès."
        );
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/{compteId}/verify-sms")
    public ResponseEntity<ApiResponse<SmsActivationResponseDTO>> verifySms(
            @PathVariable Long compteId,
            @Valid @RequestBody SmsVerifyRequestDTO request) {

        boolean verified = activationService.verifySms(compteId, request.getCode());
        var status = verified ? ActivationStatus.VERIFIE : ActivationStatus.EXPIRE;

        var response = new SmsActivationResponseDTO(
                compteId,
                status,
                verified ? "Code vérifié avec succès." : "Le code est invalide ou expiré."
        );
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
