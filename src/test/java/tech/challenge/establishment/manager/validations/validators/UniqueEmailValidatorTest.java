package tech.challenge.establishment.manager.validations.validators;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tech.challenge.establishment.manager.infrastructure.persistence.repositories.UserJpaRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UniqueEmailValidatorTest {

    private UniqueEmailValidator validator;

    @Mock
    private UserJpaRepository userRepository;

    @Mock
    private ConstraintValidatorContext context;

    @BeforeEach
    void setUp() {
        validator = new UniqueEmailValidator(userRepository);
    }

    @Test
    void shouldReturnTrueForNullEmail() {
        // Given
        String email = null;

        // When
        boolean result = validator.isValid(email, context);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void shouldReturnTrueForEmptyEmail() {
        // Given
        String email = "";

        // When
        boolean result = validator.isValid(email, context);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void shouldReturnTrueForBlankEmail() {
        // Given
        String email = "   ";

        // When
        boolean result = validator.isValid(email, context);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void shouldReturnTrueForUniqueEmail() {
        // Given
        String email = "unique@example.com";
        when(userRepository.existsByEmail(email)).thenReturn(false);

        // When
        boolean result = validator.isValid(email, context);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void shouldReturnFalseForExistingEmail() {
        // Given
        String email = "existing@example.com";
        when(userRepository.existsByEmail(email)).thenReturn(true);

        // When
        boolean result = validator.isValid(email, context);

        // Then
        assertThat(result).isFalse();
    }

    @Test
    void shouldReturnTrueForNewEmailWithDifferentCase() {
        // Given
        String email = "NEW@EXAMPLE.COM";
        when(userRepository.existsByEmail(email)).thenReturn(false);

        // When
        boolean result = validator.isValid(email, context);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void shouldReturnFalseForExistingEmailWithDifferentCase() {
        // Given
        String email = "EXISTING@EXAMPLE.COM";
        when(userRepository.existsByEmail(email)).thenReturn(true);

        // When
        boolean result = validator.isValid(email, context);

        // Then
        assertThat(result).isFalse();
    }

    @Test
    void shouldReturnTrueForEmailWithSpecialCharacters() {
        // Given
        String email = "user+test@example.co.uk";
        when(userRepository.existsByEmail(email)).thenReturn(false);

        // When
        boolean result = validator.isValid(email, context);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void shouldReturnFalseForExistingEmailWithSpecialCharacters() {
        // Given
        String email = "user+test@example.co.uk";
        when(userRepository.existsByEmail(email)).thenReturn(true);

        // When
        boolean result = validator.isValid(email, context);

        // Then
        assertThat(result).isFalse();
    }

    @Test
    void shouldReturnTrueForEmailWithNumbers() {
        // Given
        String email = "user123@domain456.com";
        when(userRepository.existsByEmail(email)).thenReturn(false);

        // When
        boolean result = validator.isValid(email, context);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void shouldReturnTrueForLongEmail() {
        // Given
        String email = "verylongusernamethatisquitelong@verylongdomainnamethatisverylong.com";
        when(userRepository.existsByEmail(email)).thenReturn(false);

        // When
        boolean result = validator.isValid(email, context);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void shouldReturnFalseForExistingLongEmail() {
        // Given
        String email = "verylongusernamethatisquitelong@verylongdomainnamethatisverylong.com";
        when(userRepository.existsByEmail(email)).thenReturn(true);

        // When
        boolean result = validator.isValid(email, context);

        // Then
        assertThat(result).isFalse();
    }
}

