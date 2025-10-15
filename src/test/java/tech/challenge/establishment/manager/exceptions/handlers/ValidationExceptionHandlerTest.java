package tech.challenge.establishment.manager.exceptions.handlers;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import tech.challenge.establishment.manager.exceptions.BusinessRuleException;
import tech.challenge.establishment.manager.presentation.dtos.error.ErrorResponseDTO;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ValidationExceptionHandlerTest {

    private ValidationExceptionHandler handler;

    @Mock
    private HttpServletRequest request;

    @Mock
    private MethodArgumentNotValidException methodArgumentNotValidException;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private MethodArgumentTypeMismatchException methodArgumentTypeMismatchException;

    @BeforeEach
    void setUp() {
        handler = new ValidationExceptionHandler();
        when(request.getRequestURI()).thenReturn("/test/validation");
    }

    @Test
    void shouldHandleBusinessRuleException() {
        // Given
        BusinessRuleException exception = new BusinessRuleException("Invalid business rule");

        // When
        ResponseEntity<ErrorResponseDTO> response = handler.handleBusinessRule(exception, request);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().status()).isEqualTo(400);
        assertThat(response.getBody().error()).isEqualTo("Business Rule Violation");
        assertThat(response.getBody().message()).isEqualTo("Invalid business rule");
        assertThat(response.getBody().path()).isEqualTo("/test/validation");
    }

    @Test
    void shouldHandleMethodArgumentNotValidException() {
        // Given
        FieldError fieldError1 = new FieldError("user", "name", "invalid", false, null, null, "Name is required");
        FieldError fieldError2 = new FieldError("user", "email", "test", false, null, null, "Email format is invalid");

        when(methodArgumentNotValidException.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError1, fieldError2));

        // When
        ResponseEntity<ErrorResponseDTO> response = handler.handleValidation(methodArgumentNotValidException, request);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().status()).isEqualTo(400);
        assertThat(response.getBody().error()).isEqualTo("Validation Failed");
        assertThat(response.getBody().message()).isEqualTo("Dados inválidos fornecidos");
        assertThat(response.getBody().path()).isEqualTo("/test/validation");
        assertThat(response.getBody().fieldErrors()).hasSize(2);
        
        // Check first field error
        assertThat(response.getBody().fieldErrors().get(0).field()).isEqualTo("name");
        assertThat(response.getBody().fieldErrors().get(0).rejectedValue()).isEqualTo("invalid");
        assertThat(response.getBody().fieldErrors().get(0).message()).isEqualTo("Name is required");
        
        // Check second field error
        assertThat(response.getBody().fieldErrors().get(1).field()).isEqualTo("email");
        assertThat(response.getBody().fieldErrors().get(1).rejectedValue()).isEqualTo("test");
        assertThat(response.getBody().fieldErrors().get(1).message()).isEqualTo("Email format is invalid");
    }

    @Test
    void shouldHandleMethodArgumentNotValidExceptionWithEmptyFieldErrors() {
        // Given
        when(methodArgumentNotValidException.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(List.of());

        // When
        ResponseEntity<ErrorResponseDTO> response = handler.handleValidation(methodArgumentNotValidException, request);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().status()).isEqualTo(400);
        assertThat(response.getBody().error()).isEqualTo("Validation Failed");
        assertThat(response.getBody().message()).isEqualTo("Dados inválidos fornecidos");
        assertThat(response.getBody().path()).isEqualTo("/test/validation");
        assertThat(response.getBody().fieldErrors()).isEmpty();
    }

    @Test
    void shouldHandleMethodArgumentTypeMismatchException() {
        // Given
        when(methodArgumentTypeMismatchException.getName()).thenReturn("id");
        when(methodArgumentTypeMismatchException.getRequiredType()).thenReturn((Class) Long.class);

        // When
        ResponseEntity<ErrorResponseDTO> response = handler.handleTypeMismatch(methodArgumentTypeMismatchException, request);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().status()).isEqualTo(400);
        assertThat(response.getBody().error()).isEqualTo("Type Mismatch");
        assertThat(response.getBody().message()).isEqualTo("Parâmetro 'id' deve ser do tipo Long");
        assertThat(response.getBody().path()).isEqualTo("/test/validation");
    }

    @Test
    void shouldHandleMethodArgumentTypeMismatchExceptionWithDifferentType() {
        // Given
        when(methodArgumentTypeMismatchException.getName()).thenReturn("active");
        when(methodArgumentTypeMismatchException.getRequiredType()).thenReturn((Class) Boolean.class);

        // When
        ResponseEntity<ErrorResponseDTO> response = handler.handleTypeMismatch(methodArgumentTypeMismatchException, request);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().status()).isEqualTo(400);
        assertThat(response.getBody().error()).isEqualTo("Type Mismatch");
        assertThat(response.getBody().message()).isEqualTo("Parâmetro 'active' deve ser do tipo Boolean");
        assertThat(response.getBody().path()).isEqualTo("/test/validation");
    }
}
