package tech.challenge.establishment.manager.validations.validators;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ValidPhoneValidatorTest {

    private ValidPhoneValidator validator;

    @Mock
    private ConstraintValidatorContext context;

    @BeforeEach
    void setUp() {
        validator = new ValidPhoneValidator();
    }

    @Test
    void shouldReturnTrueForNullPhone() {
        // Given
        String phone = null;

        // When
        boolean result = validator.isValid(phone, context);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void shouldReturnTrueForEmptyPhone() {
        // Given
        String phone = "";

        // When
        boolean result = validator.isValid(phone, context);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void shouldReturnTrueForBlankPhone() {
        // Given
        String phone = "   ";

        // When
        boolean result = validator.isValid(phone, context);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void shouldReturnTrueForValidCellPhoneWithoutFormatting() {
        // Given - Celular de SP: 11987654321
        String phone = "11987654321";

        // When
        boolean result = validator.isValid(phone, context);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void shouldReturnTrueForValidCellPhoneWithFormatting() {
        // Given - Celular formatado: (11) 98765-4321
        String phone = "(11) 98765-4321";

        // When
        boolean result = validator.isValid(phone, context);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void shouldReturnTrueForValidLandlineWithoutFormatting() {
        // Given - Telefone fixo de SP: 1123456789
        String phone = "1123456789";

        // When
        boolean result = validator.isValid(phone, context);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void shouldReturnTrueForValidLandlineWithFormatting() {
        // Given - Telefone fixo formatado: (11) 2345-6789
        String phone = "(11) 2345-6789";

        // When
        boolean result = validator.isValid(phone, context);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void shouldReturnTrueForValidCellPhoneFromRJ() {
        // Given - Celular do RJ: (21) 99876-5432
        String phone = "(21) 99876-5432";

        // When
        boolean result = validator.isValid(phone, context);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void shouldReturnTrueForValidLandlineFromBH() {
        // Given - Telefone fixo de BH: (31) 3456-7890
        String phone = "(31) 3456-7890";

        // When
        boolean result = validator.isValid(phone, context);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void shouldReturnFalseForPhoneWithLessThan10Digits() {
        // Given
        String phone = "119876543";

        // When
        boolean result = validator.isValid(phone, context);

        // Then
        assertThat(result).isFalse();
    }

    @Test
    void shouldReturnFalseForPhoneWithMoreThan11Digits() {
        // Given
        String phone = "119876543210";

        // When
        boolean result = validator.isValid(phone, context);

        // Then
        assertThat(result).isFalse();
    }

    @Test
    void shouldReturnFalseForInvalidAreaCode() {
        // Given - Código de área inválido (10)
        String phone = "1098765432";

        // When
        boolean result = validator.isValid(phone, context);

        // Then
        assertThat(result).isFalse();
    }

    @Test
    void shouldReturnFalseForAnotherInvalidAreaCode() {
        // Given - Código de área inválido (00)
        String phone = "0098765432";

        // When
        boolean result = validator.isValid(phone, context);

        // Then
        assertThat(result).isFalse();
    }

    @Test
    void shouldReturnFalseForCellPhoneWithoutNinthDigit() {
        // Given - 11 dígitos mas terceiro dígito não é 9
        String phone = "11887654321";

        // When
        boolean result = validator.isValid(phone, context);

        // Then
        assertThat(result).isFalse();
    }

    @Test
    void shouldReturnFalseForLandlineWithInvalidThirdDigit() {
        // Given - 10 dígitos mas terceiro dígito fora do range 2-5
        String phone = "1198765432";

        // When
        boolean result = validator.isValid(phone, context);

        // Then
        assertThat(result).isFalse();
    }

    @Test
    void shouldReturnFalseForLandlineWithThirdDigitOne() {
        // Given - Telefone fixo com terceiro dígito 1 (inválido)
        String phone = "1112345678";

        // When
        boolean result = validator.isValid(phone, context);

        // Then
        assertThat(result).isFalse();
    }

    @Test
    void shouldReturnFalseForLandlineWithThirdDigitSix() {
        // Given - Telefone fixo com terceiro dígito 6 (inválido)
        String phone = "1162345678";

        // When
        boolean result = validator.isValid(phone, context);

        // Then
        assertThat(result).isFalse();
    }

    @Test
    void shouldReturnFalseForPhoneWithLetters() {
        // Given
        String phone = "(11) 9876A-4321";

        // When
        boolean result = validator.isValid(phone, context);

        // Then
        assertThat(result).isFalse();
    }

    @Test
    void shouldReturnTrueForPhoneWithSpaces() {
        // Given - Telefone com espaços
        String phone = " (11) 98765-4321 ";

        // When
        boolean result = validator.isValid(phone, context);

        // Then
        assertThat(result).isTrue();
    }
}

