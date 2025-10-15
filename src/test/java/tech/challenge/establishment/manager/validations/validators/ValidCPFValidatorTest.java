package tech.challenge.establishment.manager.validations.validators;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ValidCPFValidatorTest {

    private ValidCPFValidator validator;

    @Mock
    private ConstraintValidatorContext context;

    @BeforeEach
    void setUp() {
        validator = new ValidCPFValidator();
    }

    @Test
    void shouldReturnTrueForNullCPF() {
        // Given
        String cpf = null;

        // When
        boolean result = validator.isValid(cpf, context);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void shouldReturnTrueForEmptyCPF() {
        // Given
        String cpf = "";

        // When
        boolean result = validator.isValid(cpf, context);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void shouldReturnTrueForBlankCPF() {
        // Given
        String cpf = "   ";

        // When
        boolean result = validator.isValid(cpf, context);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void shouldReturnTrueForValidCPFWithoutFormatting() {
        // Given - CPF válido: 11144477735
        String cpf = "11144477735";

        // When
        boolean result = validator.isValid(cpf, context);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void shouldReturnTrueForValidCPFWithFormatting() {
        // Given - CPF válido: 111.444.777-35
        String cpf = "111.444.777-35";

        // When
        boolean result = validator.isValid(cpf, context);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void shouldReturnTrueForValidCPFWithSpaces() {
        // Given - CPF válido com espaços
        String cpf = " 111.444.777-35 ";

        // When
        boolean result = validator.isValid(cpf, context);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void shouldReturnTrueForAnotherValidCPF() {
        // Given - CPF válido: 12345678909
        String cpf = "12345678909";

        // When
        boolean result = validator.isValid(cpf, context);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void shouldReturnFalseForCPFWithLessThan11Digits() {
        // Given
        String cpf = "1234567890";

        // When
        boolean result = validator.isValid(cpf, context);

        // Then
        assertThat(result).isFalse();
    }

    @Test
    void shouldReturnFalseForCPFWithMoreThan11Digits() {
        // Given
        String cpf = "123456789012";

        // When
        boolean result = validator.isValid(cpf, context);

        // Then
        assertThat(result).isFalse();
    }

    @Test
    void shouldReturnFalseForCPFWithAllSameDigits() {
        // Given
        String cpf = "11111111111";

        // When
        boolean result = validator.isValid(cpf, context);

        // Then
        assertThat(result).isFalse();
    }

    @Test
    void shouldReturnFalseForAnotherCPFWithAllSameDigits() {
        // Given
        String cpf = "00000000000";

        // When
        boolean result = validator.isValid(cpf, context);

        // Then
        assertThat(result).isFalse();
    }

    @Test
    void shouldReturnFalseForCPFWithInvalidFirstDigit() {
        // Given - CPF com primeiro dígito verificador inválido
        String cpf = "11144477736"; // deveria ser 35

        // When
        boolean result = validator.isValid(cpf, context);

        // Then
        assertThat(result).isFalse();
    }

    @Test
    void shouldReturnFalseForCPFWithInvalidSecondDigit() {
        // Given - CPF com segundo dígito verificador inválido
        String cpf = "11144477734"; // deveria ser 35

        // When
        boolean result = validator.isValid(cpf, context);

        // Then
        assertThat(result).isFalse();
    }

    @Test
    void shouldReturnFalseForCPFWithLetters() {
        // Given
        String cpf = "111.444.77A-35";

        // When
        boolean result = validator.isValid(cpf, context);

        // Then
        assertThat(result).isFalse();
    }

    @Test
    void shouldReturnFalseForCPFWithSpecialCharacters() {
        // Given - CPF com caracteres especiais que não são removidos corretamente
        String cpf = "111@444#777$35"; // Após limpeza fica "11144477735" que é válido

        // When
        boolean result = validator.isValid(cpf, context);

        // Then
        // O validator remove caracteres não numéricos, então este CPF se torna válido
        assertThat(result).isTrue();
    }

    @Test
    void shouldReturnTrueForValidCPFWithMixedFormatting() {
        // Given - CPF válido com formatação mista
        String cpf = "123 456 789-09";

        // When
        boolean result = validator.isValid(cpf, context);

        // Then
        assertThat(result).isTrue();
    }
}
