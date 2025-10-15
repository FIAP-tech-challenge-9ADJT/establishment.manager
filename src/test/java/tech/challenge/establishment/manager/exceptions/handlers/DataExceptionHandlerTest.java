package tech.challenge.establishment.manager.exceptions.handlers;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import tech.challenge.establishment.manager.domain.exceptions.AccessDeniedException;
import tech.challenge.establishment.manager.exceptions.DataConflictException;
import tech.challenge.establishment.manager.exceptions.ResourceNotFoundException;
import tech.challenge.establishment.manager.presentation.dtos.error.ErrorResponseDTO;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DataExceptionHandlerTest {

    private DataExceptionHandler handler;

    @Mock
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        handler = new DataExceptionHandler();
        when(request.getRequestURI()).thenReturn("/test/endpoint");
    }

    @Test
    void shouldHandleAccessDeniedException() {
        // Given
        AccessDeniedException exception = new AccessDeniedException("Access denied to resource");

        // When
        ResponseEntity<ErrorResponseDTO> response = handler.handleAccessDenied(exception, request);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().status()).isEqualTo(403);
        assertThat(response.getBody().error()).isEqualTo("Access Denied");
        assertThat(response.getBody().message()).isEqualTo("Access denied to resource");
        assertThat(response.getBody().path()).isEqualTo("/test/endpoint");
    }

    @Test
    void shouldHandleResourceNotFoundException() {
        // Given
        ResourceNotFoundException exception = new ResourceNotFoundException("Resource not found");

        // When
        ResponseEntity<ErrorResponseDTO> response = handler.handleResourceNotFound(exception, request);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().status()).isEqualTo(404);
        assertThat(response.getBody().error()).isEqualTo("Resource Not Found");
        assertThat(response.getBody().message()).isEqualTo("Resource not found");
        assertThat(response.getBody().path()).isEqualTo("/test/endpoint");
    }

    @Test
    void shouldHandleEntityNotFoundException() {
        // Given
        EntityNotFoundException exception = new EntityNotFoundException("Entity not found in database");

        // When
        ResponseEntity<ErrorResponseDTO> response = handler.handleEntityNotFound(exception, request);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().status()).isEqualTo(404);
        assertThat(response.getBody().error()).isEqualTo("Entity Not Found");
        assertThat(response.getBody().message()).isEqualTo("Entity not found in database");
        assertThat(response.getBody().path()).isEqualTo("/test/endpoint");
    }

    @Test
    void shouldHandleDataConflictException() {
        // Given
        DataConflictException exception = new DataConflictException("Data conflict occurred");

        // When
        ResponseEntity<ErrorResponseDTO> response = handler.handleDataConflict(exception, request);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().status()).isEqualTo(409);
        assertThat(response.getBody().error()).isEqualTo("Data Conflict");
        assertThat(response.getBody().message()).isEqualTo("Data conflict occurred");
        assertThat(response.getBody().path()).isEqualTo("/test/endpoint");
    }

    @Test
    void shouldHandleDataIntegrityViolationWithDuplicateEntry() {
        // Given
        DataIntegrityViolationException exception = new DataIntegrityViolationException(
                "Duplicate entry 'test@example.com' for key 'email'"
        );

        // When
        ResponseEntity<ErrorResponseDTO> response = handler.handleDataIntegrityViolation(exception, request);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().status()).isEqualTo(409);
        assertThat(response.getBody().error()).isEqualTo("Data Integrity Violation");
        assertThat(response.getBody().message()).isEqualTo("Já existe um registro com essas informações");
        assertThat(response.getBody().path()).isEqualTo("/test/endpoint");
    }

    @Test
    void shouldHandleDataIntegrityViolationWithForeignKeyConstraint() {
        // Given
        DataIntegrityViolationException exception = new DataIntegrityViolationException(
                "Cannot delete or update a parent row: a foreign key constraint fails"
        );

        // When
        ResponseEntity<ErrorResponseDTO> response = handler.handleDataIntegrityViolation(exception, request);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().status()).isEqualTo(409);
        assertThat(response.getBody().error()).isEqualTo("Data Integrity Violation");
        assertThat(response.getBody().message()).isEqualTo("Não é possível realizar esta operação devido a dependências");
        assertThat(response.getBody().path()).isEqualTo("/test/endpoint");
    }

    @Test
    void shouldHandleDataIntegrityViolationWithGenericMessage() {
        // Given
        DataIntegrityViolationException exception = new DataIntegrityViolationException(
                "Some other integrity violation"
        );

        // When
        ResponseEntity<ErrorResponseDTO> response = handler.handleDataIntegrityViolation(exception, request);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().status()).isEqualTo(409);
        assertThat(response.getBody().error()).isEqualTo("Data Integrity Violation");
        assertThat(response.getBody().message()).isEqualTo("Violação de integridade dos dados");
        assertThat(response.getBody().path()).isEqualTo("/test/endpoint");
    }
}

