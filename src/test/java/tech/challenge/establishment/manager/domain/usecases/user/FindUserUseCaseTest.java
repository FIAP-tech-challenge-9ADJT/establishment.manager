package tech.challenge.establishment.manager.domain.usecases.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tech.challenge.establishment.manager.domain.entities.User;
import tech.challenge.establishment.manager.domain.exceptions.UserNotFoundException;
import tech.challenge.establishment.manager.domain.repositories.UserRepository;
import tech.challenge.establishment.manager.domain.valueobjects.UserId;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindUserUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private User user;

    private FindUserUseCase findUserUseCase;

    @BeforeEach
    void setUp() {
        findUserUseCase = new FindUserUseCase(userRepository);
    }

    @Test
    void shouldFindUserSuccessfully() {
        UserId userId = UserId.of(1L);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        User result = findUserUseCase.execute(userId);

        assertNotNull(result);
        assertEquals(user, result);

        verify(userRepository).findById(userId);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        UserId userId = UserId.of(999L);

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () ->
            findUserUseCase.execute(userId)
        );

        verify(userRepository).findById(userId);
    }
}

