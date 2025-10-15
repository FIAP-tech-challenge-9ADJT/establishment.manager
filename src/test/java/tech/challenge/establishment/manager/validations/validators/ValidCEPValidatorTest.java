package tech.challenge.establishment.manager.validations.validators;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ValidCEPValidatorTest {

    private ValidCEPValidator validator;

    @Mock
    private ConstraintValidatorContext context;

    @BeforeEach
    void setUp() {
        validator = new ValidCEPValidator();
    }

    @Test
    void shouldReturnTrueForNullCEP() {
        // Given
        String cep = null;

        // When
        boolean result = validator.isValid(cep, context);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void shouldReturnTrueForEmptyCEP() {
        // Given
        String cep = "";

        // When
        boolean result = validator.isValid(cep, context);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void shouldReturnTrueForBlankCEP() {
        // Given
        String cep = "   ";

        // When
        boolean result = validator.isValid(cep, context);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void shouldReturnTrueForValidCEPWithoutFormatting() {
        // Given
        String cep = "01234567";

        // When
        boolean result = validator.isValid(cep, context);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void shouldReturnTrueForValidCEPWithHyphen() {
        // Given
        String cep = "01234-567";

        // When
        boolean result = validator.isValid(cep, context);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void shouldReturnTrueForValidCEPWithSpaces() {
        // Given
        String cep = " 01234567 ";

        // When
        boolean result = validator.isValid(cep, context);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void shouldReturnFalseForCEPWithLessThan8Digits() {
        // Given
        String cep = "0123456";

        // When
        boolean result = validator.isValid(cep, context);

        // Then
        assertThat(result).isFalse();
    }

    @Test
    void shouldReturnFalseForCEPWithMoreThan8Digits() {
        // Given
        String cep = "012345678";

        // When
        boolean result = validator.isValid(cep, context);

        // Then
        assertThat(result).isFalse();
    }

    @Test
    void shouldReturnFalseForCEPWithLetters() {
        // Given
        String cep = "0123456A";

        // When
        boolean result = validator.isValid(cep, context);

        // Then
        assertThat(result).isFalse();
    }

    @Test
    void shouldReturnFalseForCEPWithSpecialCharacters() {
        // Given
        String cep = "01234@67";

        // When
        boolean result = validator.isValid(cep, context);

        // Then
        assertThat(result).isFalse();
    }

    @Test
    void shouldReturnTrueForAllZerosCEP() {
        // Given
        String cep = "00000000";

        // When
        boolean result = validator.isValid(cep, context);

        // Then
        assertThat(result).isTrue();
    }
}

