package tech.challenge.establishment.manager.exceptions;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UnauthorizedExceptionTest {

    @Test
    void shouldCreateExceptionWithMessage() {
        // Given
        String message = "Unauthorized access";

        // When
        UnauthorizedException exception = new UnauthorizedException(message);

        // Then
        assertThat(exception.getMessage()).isEqualTo(message);
        assertThat(exception).isInstanceOf(RuntimeException.class);
    }

    @Test
    void shouldCreateExceptionWithNullMessage() {
        // Given
        String message = null;

        // When
        UnauthorizedException exception = new UnauthorizedException(message);

        // Then
        assertThat(exception.getMessage()).isNull();
    }

    @Test
    void shouldCreateExceptionWithEmptyMessage() {
        // Given
        String message = "";

        // When
        UnauthorizedException exception = new UnauthorizedException(message);

        // Then
        assertThat(exception.getMessage()).isEmpty();
    }
}

