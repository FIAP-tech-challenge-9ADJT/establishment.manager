package tech.challenge.establishment.manager.exceptions.handlers;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import tech.challenge.establishment.manager.exceptions.UnauthorizedException;
import tech.challenge.establishment.manager.presentation.dtos.error.ErrorResponseDTO;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SecurityExceptionHandlerTest {

    private SecurityExceptionHandler handler;

    @Mock
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        handler = new SecurityExceptionHandler();
        when(request.getRequestURI()).thenReturn("/test/security");
    }

    @Test
    void shouldHandleUnauthorizedException() {
        // Given
        UnauthorizedException exception = new UnauthorizedException("Token expired");

        // When
        ResponseEntity<ErrorResponseDTO> response = handler.handleUnauthorized(exception, request);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().status()).isEqualTo(401);
        assertThat(response.getBody().error()).isEqualTo("Unauthorized");
        assertThat(response.getBody().message()).isEqualTo("Token expired");
        assertThat(response.getBody().path()).isEqualTo("/test/security");
    }

    @Test
    void shouldHandleAccessDeniedException() {
        // Given
        AccessDeniedException exception = new AccessDeniedException("Access is denied");

        // When
        ResponseEntity<ErrorResponseDTO> response = handler.handleAccessDenied(exception, request);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().status()).isEqualTo(403);
        assertThat(response.getBody().error()).isEqualTo("Access Denied");
        assertThat(response.getBody().message()).isEqualTo("Você não tem permissão para acessar este recurso");
        assertThat(response.getBody().path()).isEqualTo("/test/security");
    }

    @Test
    void shouldHandleAuthenticationExceptionWithBadCredentials() {
        // Given
        BadCredentialsException exception = new BadCredentialsException("Bad credentials");

        // When
        ResponseEntity<ErrorResponseDTO> response = handler.handleAuthentication(exception, request);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().status()).isEqualTo(401);
        assertThat(response.getBody().error()).isEqualTo("Authentication Failed");
        assertThat(response.getBody().message()).isEqualTo("Credenciais inválidas");
        assertThat(response.getBody().path()).isEqualTo("/test/security");
    }

    @Test
    void shouldHandleAuthenticationExceptionWithGenericException() {
        // Given
        DisabledException exception = new DisabledException("User is disabled");

        // When
        ResponseEntity<ErrorResponseDTO> response = handler.handleAuthentication(exception, request);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().status()).isEqualTo(401);
        assertThat(response.getBody().error()).isEqualTo("Authentication Failed");
        assertThat(response.getBody().message()).isEqualTo("Falha na autenticação");
        assertThat(response.getBody().path()).isEqualTo("/test/security");
    }

    @Test
    void shouldHandleAuthenticationExceptionWithCustomMessage() {
        // Given
        AuthenticationException exception = new AuthenticationException("Custom auth error") {};

        // When
        ResponseEntity<ErrorResponseDTO> response = handler.handleAuthentication(exception, request);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().status()).isEqualTo(401);
        assertThat(response.getBody().error()).isEqualTo("Authentication Failed");
        assertThat(response.getBody().message()).isEqualTo("Falha na autenticação");
        assertThat(response.getBody().path()).isEqualTo("/test/security");
    }
}

