package tech.challenge.establishment.manager.exceptions;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DataConflictExceptionTest {

    @Test
    void shouldCreateExceptionWithMessage() {
        // Given
        String message = "Data conflict occurred";

        // When
        DataConflictException exception = new DataConflictException(message);

        // Then
        assertThat(exception.getMessage()).isEqualTo(message);
        assertThat(exception).isInstanceOf(RuntimeException.class);
    }

    @Test
    void shouldCreateExceptionWithNullMessage() {
        // Given
        String message = null;

        // When
        DataConflictException exception = new DataConflictException(message);

        // Then
        assertThat(exception.getMessage()).isNull();
    }

    @Test
    void shouldCreateExceptionWithEmptyMessage() {
        // Given
        String message = "";

        // When
        DataConflictException exception = new DataConflictException(message);

        // Then
        assertThat(exception.getMessage()).isEmpty();
    }
}

