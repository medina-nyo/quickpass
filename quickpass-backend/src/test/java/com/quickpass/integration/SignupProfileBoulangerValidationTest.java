package com.quickpass.integration;

import com.quickpass.signup.api.dto.boulanger.step3.SignupProfileBoulangerDTO;
import com.quickpass.common.constants.ModeVente;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test professionnel de validation Jakarta pour le record
 * {@link SignupProfileBoulangerDTO}.
 *
 * <p>Vérifie les contraintes de validation (regex, @NotBlank, @NotNull)
 * sur plusieurs cas valides et invalides.</p>
 *
 * <p>Ce test est isolé de Spring Boot pour s'exécuter en quelques millisecondes.</p>
 */
@TestInstance(Lifecycle.PER_CLASS)
class SignupProfileBoulangerValidationTest {

    private Validator validator;

    @BeforeAll
    void setupValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        this.validator = factory.getValidator();
    }


    static Stream<SignupProfileBoulangerDTO> validDtos() {
        return Stream.of(
                new SignupProfileBoulangerDTO(
                        "Jean", "Dupont", "+33612345678",
                        "Boulangerie du Coin", "12345678901234",
                        "+33123456789", ModeVente.SUR_PLACE
                ),
                new SignupProfileBoulangerDTO(
                        "Marie", "Martin", "+33798765432",
                        "Pâtisserie Fine", "98765432109876",
                        "+33498765432", ModeVente.LES_DEUX
                )
        );
    }

    @ParameterizedTest
    @MethodSource("validDtos")
    @DisplayName("DTO valide : aucune violation détectée")
    void givenValidDtos_whenValidate_thenNoViolations(SignupProfileBoulangerDTO dto) {
        Set<ConstraintViolation<SignupProfileBoulangerDTO>> violations = validator.validate(dto);
        assertThat(violations)
                .as("Le DTO valide ne doit contenir aucune erreur.")
                .isEmpty();
    }



    record InvalidCase(String label, SignupProfileBoulangerDTO dto, List<String> expectedFields) {}

    static Stream<InvalidCase> invalidDtos() {
        return Stream.of(
                new InvalidCase(
                        "Champs vides et format invalide",
                        new SignupProfileBoulangerDTO(
                                "", "", "abc", "", "1234", "xyz", null
                        ),
                        List.of("prenom", "nom", "telephonePersonnel", "raisonSociale", "siret", "modeVente")
                ),
                new InvalidCase(
                        "Téléphone manquant mais obligatoire",
                        new SignupProfileBoulangerDTO(
                                "Luc", "Durand", null, "Boulangerie du Marché",
                                "12345678901234", "+33411112222", ModeVente.SUR_PLACE
                        ),
                        List.of("telephonePersonnel")
                ),
                new InvalidCase(
                        "SIRET trop court",
                        new SignupProfileBoulangerDTO(
                                "Alice", "Moreau", "+33699999999", "Pâtisserie d'Or",
                                "98765", "+33422223333", ModeVente.EN_LIGNE
                        ),
                        List.of("siret")
                )
        );
    }

    @ParameterizedTest(name = "{index} - {0}")
    @MethodSource("invalidDtos")
    @DisplayName("DTO invalide : violations détectées")
    void givenInvalidDtos_whenValidate_thenViolationsDetected(InvalidCase testCase) {
        Set<ConstraintViolation<SignupProfileBoulangerDTO>> violations = validator.validate(testCase.dto());
        assertThat(violations)
                .as("Le DTO invalide doit générer au moins une violation.")
                .isNotEmpty();

        List<String> invalidFields = violations.stream()
                .map(v -> v.getPropertyPath().toString())
                .toList();

        assertThat(invalidFields)
                .as("Les champs invalides doivent correspondre à ceux attendus.")
                .containsAnyElementsOf(testCase.expectedFields());

        System.out.println("Cas testé : " + testCase.label());
        violations.forEach(v ->
                System.out.println("Violation → " + v.getPropertyPath() + " : " + v.getMessage()));
    }
}
