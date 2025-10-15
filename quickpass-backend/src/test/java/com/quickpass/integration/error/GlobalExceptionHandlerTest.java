package com.quickpass.integration.error;

import com.quickpass.error.ErrorCatalog;
import com.quickpass.error.exception.BusinessException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(
        properties = "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration"
)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandlerTest.TestController.class)
class GlobalExceptionHandlerTest {

    @RestController
    @RequestMapping("/test-error")
    static class TestController {

        @GetMapping("/business")
        public void throwBusinessError() {
            throw new BusinessException(ErrorCatalog.SIGNUP_EMAIL_EXISTS, "Erreur métier simulée");
        }

        @GetMapping("/validation")
        public void throwValidationError() {
            throw new com.quickpass.error.exception.BusinessException(ErrorCatalog.SMS_INVALID_CODE, "Champ invalide");
        }

        @GetMapping("/internal")
        public void throwInternalError() {
            throw new RuntimeException("Erreur interne simulée");
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("⚙️ BusinessException → 409 RFC7807")
    void shouldReturnBusinessError() throws Exception {
        mockMvc.perform(get("/test-error/business")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.code").value("SIGNUP_EMAIL_EXISTS"))
                .andExpect(jsonPath("$.status").value(HttpStatus.CONFLICT.value()))
                .andExpect(jsonPath("$.title").exists())
                .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    @DisplayName("⚙️ SMS_INVALID_CODE → 400 RFC7807")
    void shouldReturnValidationError() throws Exception {
        mockMvc.perform(get("/test-error/validation")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.code").value("SMS_INVALID_CODE"))
                .andExpect(jsonPath("$.title").exists());
    }

    @Test
    @DisplayName("⚙️ Exception → 500 RFC7807")
    void shouldReturnInternalError() throws Exception {
        mockMvc.perform(get("/test-error/internal")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.code").value("ERREUR_INTERNE"))
                .andExpect(jsonPath("$.title").value("Erreur interne du serveur"))
                .andExpect(jsonPath("$.timestamp").exists());
    }
}
