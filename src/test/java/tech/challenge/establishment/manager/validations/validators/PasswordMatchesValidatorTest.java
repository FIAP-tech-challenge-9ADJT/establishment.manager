package tech.challenge.establishment.manager.validations.validators;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tech.challenge.establishment.manager.presentation.dtos.user.ChangePasswordDTO;
import tech.challenge.establishment.manager.validations.PasswordMatches;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PasswordMatchesValidatorTest {

    private PasswordMatchesValidator validator;

    @Mock
    private ConstraintValidatorContext context;

    @Mock
    private PasswordMatches passwordMatches;

    @BeforeEach
    void setUp() {
        validator = new PasswordMatchesValidator();
        when(passwordMatches.password()).thenReturn("password");
        when(passwordMatches.confirmPassword()).thenReturn("confirmPassword");
        validator.initialize(passwordMatches);
    }

    @Test
    void shouldReturnTrueForNullObject() {
        // Given
        Object obj = null;

        // When
        boolean result = validator.isValid(obj, context);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void shouldReturnTrueForChangePasswordDTOWithMatchingPasswords() {
        // Given
        ChangePasswordDTO dto = new ChangePasswordDTO(
                "currentPassword",
                "newPassword123",
                "newPassword123"
        );

        // When
        boolean result = validator.isValid(dto, context);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void shouldReturnFalseForChangePasswordDTOWithNonMatchingPasswords() {
        // Given
        ChangePasswordDTO dto = new ChangePasswordDTO(
                "currentPassword",
                "newPassword123",
                "differentPassword"
        );

        // When
        boolean result = validator.isValid(dto, context);

        // Then
        assertThat(result).isFalse();
    }

    @Test
    void shouldReturnFalseForChangePasswordDTOWithNullNewPassword() {
        // Given
        ChangePasswordDTO dto = new ChangePasswordDTO(
                "currentPassword",
                null,
                "newPassword123"
        );

        // When
        boolean result = validator.isValid(dto, context);

        // Then
        assertThat(result).isFalse();
    }

    @Test
    void shouldReturnFalseForChangePasswordDTOWithNullConfirmPassword() {
        // Given
        ChangePasswordDTO dto = new ChangePasswordDTO(
                "currentPassword",
                "newPassword123",
                null
        );

        // When
        boolean result = validator.isValid(dto, context);

        // Then
        assertThat(result).isFalse();
    }

    @Test
    void shouldReturnFalseForChangePasswordDTOWithBothPasswordsNull() {
        // Given
        ChangePasswordDTO dto = new ChangePasswordDTO(
                "currentPassword",
                null,
                null
        );

        // When
        boolean result = validator.isValid(dto, context);

        // Then
        // O validator retorna false quando newPassword é null, mesmo que ambos sejam null
        assertThat(result).isFalse();
    }

    @Test
    void shouldReturnTrueForGenericObjectWithMatchingPasswords() {
        // Given
        TestPasswordObject obj = new TestPasswordObject("password123", "password123");

        // When
        boolean result = validator.isValid(obj, context);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void shouldReturnFalseForGenericObjectWithNonMatchingPasswords() {
        // Given
        TestPasswordObject obj = new TestPasswordObject("password123", "differentPassword");

        // When
        boolean result = validator.isValid(obj, context);

        // Then
        assertThat(result).isFalse();
    }

    @Test
    void shouldReturnTrueForGenericObjectWithBothPasswordsNull() {
        // Given
        TestPasswordObject obj = new TestPasswordObject(null, null);

        // When
        boolean result = validator.isValid(obj, context);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void shouldReturnFalseForGenericObjectWithOnePasswordNull() {
        // Given
        TestPasswordObject obj = new TestPasswordObject("password123", null);

        // When
        boolean result = validator.isValid(obj, context);

        // Then
        assertThat(result).isFalse();
    }

    @Test
    void shouldReturnFalseForObjectWithInvalidFields() {
        // Given
        Object obj = new Object(); // Objeto sem os campos necessários

        // When
        boolean result = validator.isValid(obj, context);

        // Then
        assertThat(result).isFalse();
    }

    @Test
    void shouldReturnTrueForChangePasswordDTOWithEmptyPasswords() {
        // Given
        ChangePasswordDTO dto = new ChangePasswordDTO(
                "currentPassword",
                "",
                ""
        );

        // When
        boolean result = validator.isValid(dto, context);

        // Then
        assertThat(result).isTrue();
    }

    // Classe auxiliar para testes genéricos
    public static class TestPasswordObject {
        public String password;
        public String confirmPassword;

        public TestPasswordObject(String password, String confirmPassword) {
            this.password = password;
            this.confirmPassword = confirmPassword;
        }
    }
}
