package tech.challenge.establishment.manager.application.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import tech.challenge.establishment.manager.domain.entities.User;
import tech.challenge.establishment.manager.domain.exceptions.InvalidCredentialsException;
import tech.challenge.establishment.manager.domain.usecases.auth.AuthenticateUserUseCase;
import tech.challenge.establishment.manager.domain.usecases.auth.ChangePasswordUseCase;
import tech.challenge.establishment.manager.domain.valueobjects.Password;
import tech.challenge.establishment.manager.domain.valueobjects.UserId;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthApplicationServiceTest {

    @Mock
    private AuthenticateUserUseCase authenticateUserUseCase;

    @Mock
    private ChangePasswordUseCase changePasswordUseCase;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private User user;

    private AuthApplicationService authApplicationService;

    @BeforeEach
    void setUp() {
        authApplicationService = new AuthApplicationService(
                authenticateUserUseCase, 
                changePasswordUseCase, 
                passwordEncoder
        );
    }

    @Test
    void shouldAuthenticateUserSuccessfully() {
        String login = "user123";
        String password = "password123";
        String encodedPassword = "$2a$10$encodedpassword";

        when(authenticateUserUseCase.execute(login, password)).thenReturn(user);
        when(user.getPassword()).thenReturn(Password.encoded(encodedPassword));
        when(passwordEncoder.matches(password, encodedPassword)).thenReturn(true);

        User result = authApplicationService.authenticate(login, password);

        assertNotNull(result);
        assertEquals(user, result);

        verify(authenticateUserUseCase).execute(login, password);
        verify(passwordEncoder).matches(password, encodedPassword);
    }

    @Test
    void shouldThrowExceptionWhenPasswordDoesNotMatch() {
        String login = "user123";
        String password = "wrongpassword";
        String encodedPassword = "$2a$10$encodedpassword";

        when(authenticateUserUseCase.execute(login, password)).thenReturn(user);
        when(user.getPassword()).thenReturn(Password.encoded(encodedPassword));
        when(passwordEncoder.matches(password, encodedPassword)).thenReturn(false);

        assertThrows(InvalidCredentialsException.class, () ->
            authApplicationService.authenticate(login, password)
        );

        verify(authenticateUserUseCase).execute(login, password);
        verify(passwordEncoder).matches(password, encodedPassword);
    }

    @Test
    void shouldChangePasswordSuccessfully() {
        UserId userId = UserId.of(1L);
        String newPassword = "newPassword123";
        String encodedPassword = "$2a$10$encodednewpassword";

        when(passwordEncoder.encode(newPassword)).thenReturn(encodedPassword);

        authApplicationService.changePassword(userId, newPassword);

        verify(passwordEncoder).encode(newPassword);
        verify(changePasswordUseCase).execute(userId, encodedPassword);
    }

    @Test
    void shouldThrowExceptionWhenAuthenticateUserUseCaseThrows() {
        String login = "user123";
        String password = "password123";

        when(authenticateUserUseCase.execute(login, password))
                .thenThrow(new InvalidCredentialsException());

        assertThrows(InvalidCredentialsException.class, () ->
            authApplicationService.authenticate(login, password)
        );

        verify(authenticateUserUseCase).execute(login, password);
        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }
}


