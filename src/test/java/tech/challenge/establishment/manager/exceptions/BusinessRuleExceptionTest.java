package tech.challenge.establishment.manager.exceptions;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BusinessRuleExceptionTest {

    @Test
    void shouldCreateExceptionWithMessage() {
        // Given
        String message = "Business rule violation";

        // When
        BusinessRuleException exception = new BusinessRuleException(message);

        // Then
        assertThat(exception.getMessage()).isEqualTo(message);
        assertThat(exception).isInstanceOf(RuntimeException.class);
    }

    @Test
    void shouldCreateExceptionWithNullMessage() {
        // Given
        String message = null;

        // When
        BusinessRuleException exception = new BusinessRuleException(message);

        // Then
        assertThat(exception.getMessage()).isNull();
    }

    @Test
    void shouldCreateExceptionWithEmptyMessage() {
        // Given
        String message = "";

        // When
        BusinessRuleException exception = new BusinessRuleException(message);

        // Then
        assertThat(exception.getMessage()).isEmpty();
    }
}

