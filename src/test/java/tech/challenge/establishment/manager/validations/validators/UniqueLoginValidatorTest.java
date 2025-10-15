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
class UniqueLoginValidatorTest {

    private UniqueLoginValidator validator;

    @Mock
    private UserJpaRepository userRepository;

    @Mock
    private ConstraintValidatorContext context;

    @BeforeEach
    void setUp() {
        validator = new UniqueLoginValidator(userRepository);
    }

    @Test
    void shouldReturnTrueForNullLogin() {
        // Given
        String login = null;

        // When
        boolean result = validator.isValid(login, context);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void shouldReturnTrueForEmptyLogin() {
        // Given
        String login = "";

        // When
        boolean result = validator.isValid(login, context);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void shouldReturnTrueForBlankLogin() {
        // Given
        String login = "   ";

        // When
        boolean result = validator.isValid(login, context);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void shouldReturnTrueForUniqueLogin() {
        // Given
        String login = "uniqueuser";
        when(userRepository.existsByLogin(login)).thenReturn(false);

        // When
        boolean result = validator.isValid(login, context);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void shouldReturnFalseForExistingLogin() {
        // Given
        String login = "existinguser";
        when(userRepository.existsByLogin(login)).thenReturn(true);

        // When
        boolean result = validator.isValid(login, context);

        // Then
        assertThat(result).isFalse();
    }

    @Test
    void shouldReturnTrueForNewLoginWithNumbers() {
        // Given
        String login = "user123";
        when(userRepository.existsByLogin(login)).thenReturn(false);

        // When
        boolean result = validator.isValid(login, context);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void shouldReturnFalseForExistingLoginWithNumbers() {
        // Given
        String login = "user123";
        when(userRepository.existsByLogin(login)).thenReturn(true);

        // When
        boolean result = validator.isValid(login, context);

        // Then
        assertThat(result).isFalse();
    }

    @Test
    void shouldReturnTrueForLoginWithSpecialCharacters() {
        // Given
        String login = "user_name";
        when(userRepository.existsByLogin(login)).thenReturn(false);

        // When
        boolean result = validator.isValid(login, context);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void shouldReturnFalseForExistingLoginWithSpecialCharacters() {
        // Given
        String login = "user_name";
        when(userRepository.existsByLogin(login)).thenReturn(true);

        // When
        boolean result = validator.isValid(login, context);

        // Then
        assertThat(result).isFalse();
    }

    @Test
    void shouldReturnTrueForLoginWithDots() {
        // Given
        String login = "user.name";
        when(userRepository.existsByLogin(login)).thenReturn(false);

        // When
        boolean result = validator.isValid(login, context);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void shouldReturnTrueForShortLogin() {
        // Given
        String login = "usr";
        when(userRepository.existsByLogin(login)).thenReturn(false);

        // When
        boolean result = validator.isValid(login, context);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void shouldReturnFalseForExistingShortLogin() {
        // Given
        String login = "usr";
        when(userRepository.existsByLogin(login)).thenReturn(true);

        // When
        boolean result = validator.isValid(login, context);

        // Then
        assertThat(result).isFalse();
    }

    @Test
    void shouldReturnTrueForLongLogin() {
        // Given
        String login = "verylongusernamethatisquitelong";
        when(userRepository.existsByLogin(login)).thenReturn(false);

        // When
        boolean result = validator.isValid(login, context);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void shouldReturnFalseForExistingLongLogin() {
        // Given
        String login = "verylongusernamethatisquitelong";
        when(userRepository.existsByLogin(login)).thenReturn(true);

        // When
        boolean result = validator.isValid(login, context);

        // Then
        assertThat(result).isFalse();
    }

    @Test
    void shouldReturnTrueForLoginWithMixedCase() {
        // Given
        String login = "UserName";
        when(userRepository.existsByLogin(login)).thenReturn(false);

        // When
        boolean result = validator.isValid(login, context);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void shouldReturnFalseForExistingLoginWithMixedCase() {
        // Given
        String login = "UserName";
        when(userRepository.existsByLogin(login)).thenReturn(true);

        // When
        boolean result = validator.isValid(login, context);

        // Then
        assertThat(result).isFalse();
    }
}

