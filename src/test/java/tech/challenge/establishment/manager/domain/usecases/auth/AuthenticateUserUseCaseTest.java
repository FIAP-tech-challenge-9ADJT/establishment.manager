package tech.challenge.establishment.manager.domain.usecases.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tech.challenge.establishment.manager.domain.entities.User;
import tech.challenge.establishment.manager.domain.exceptions.InvalidCredentialsException;
import tech.challenge.establishment.manager.domain.repositories.UserRepository;
import tech.challenge.establishment.manager.domain.valueobjects.Login;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticateUserUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private User user;

    private AuthenticateUserUseCase authenticateUserUseCase;

    @BeforeEach
    void setUp() {
        authenticateUserUseCase = new AuthenticateUserUseCase(userRepository);
    }

    @Test
    void shouldAuthenticateUserSuccessfully() {
        String login = "user123";
        String password = "password123";

        when(userRepository.findByLogin(Login.of(login))).thenReturn(Optional.of(user));

        User result = authenticateUserUseCase.execute(login, password);

        assertNotNull(result);
        assertEquals(user, result);

        verify(userRepository).findByLogin(Login.of(login));
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        String login = "nonexistent";
        String password = "password123";

        when(userRepository.findByLogin(Login.of(login))).thenReturn(Optional.empty());

        assertThrows(InvalidCredentialsException.class, () ->
            authenticateUserUseCase.execute(login, password)
        );

        verify(userRepository).findByLogin(Login.of(login));
    }
}

