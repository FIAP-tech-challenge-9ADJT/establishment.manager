package tech.challenge.establishment.manager.exceptions.handlers;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import tech.challenge.establishment.manager.presentation.dtos.error.ErrorResponseDTO;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GeneralExceptionHandlerTest {

    private GeneralExceptionHandler handler;

    @Mock
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        handler = new GeneralExceptionHandler();
        when(request.getRequestURI()).thenReturn("/test/general");
    }

    @Test
    void shouldHandleNoResourceFoundException() {
        // Given
        NoResourceFoundException exception = new NoResourceFoundException(null, "/unknown/endpoint");

        // When
        ResponseEntity<ErrorResponseDTO> response = handler.handleNoResourceFound(exception, request);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().status()).isEqualTo(404);
        assertThat(response.getBody().error()).isEqualTo("Endpoint Not Found");
        assertThat(response.getBody().message()).isEqualTo("O endpoint solicitado não foi encontrado");
        assertThat(response.getBody().path()).isEqualTo("/test/general");
    }

    @Test
    void shouldHandleGenericException() {
        // Given
        Exception exception = new RuntimeException("Something went wrong");

        // When
        ResponseEntity<ErrorResponseDTO> response = handler.handleGeneral(exception, request);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().status()).isEqualTo(500);
        assertThat(response.getBody().error()).isEqualTo("Internal Server Error");
        assertThat(response.getBody().message()).isEqualTo("Erro interno: RuntimeException - Something went wrong");
        assertThat(response.getBody().path()).isEqualTo("/test/general");
    }

    @Test
    void shouldHandleGenericExceptionWithNullMessage() {
        // Given
        Exception exception = new NullPointerException();

        // When
        ResponseEntity<ErrorResponseDTO> response = handler.handleGeneral(exception, request);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().status()).isEqualTo(500);
        assertThat(response.getBody().error()).isEqualTo("Internal Server Error");
        assertThat(response.getBody().message()).isEqualTo("Erro interno: NullPointerException - null");
        assertThat(response.getBody().path()).isEqualTo("/test/general");
    }

    @Test
    void shouldHandleCustomException() {
        // Given
        Exception exception = new IllegalArgumentException("Invalid argument provided");

        // When
        ResponseEntity<ErrorResponseDTO> response = handler.handleGeneral(exception, request);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().status()).isEqualTo(500);
        assertThat(response.getBody().error()).isEqualTo("Internal Server Error");
        assertThat(response.getBody().message()).isEqualTo("Erro interno: IllegalArgumentException - Invalid argument provided");
        assertThat(response.getBody().path()).isEqualTo("/test/general");
    }
}

