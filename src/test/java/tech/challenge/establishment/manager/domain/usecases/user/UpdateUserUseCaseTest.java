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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateUserUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private User user;

    @Mock
    private User updatedUser;

    private UpdateUserUseCase updateUserUseCase;

    @BeforeEach
    void setUp() {
        updateUserUseCase = new UpdateUserUseCase(userRepository);
    }

    @Test
    void shouldUpdateUserSuccessfully() {
        UserId userId = UserId.of(1L);
        String newName = "João Santos";
        String newEmail = "joao.santos@email.com";

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(user.updateProfile(newName, newEmail)).thenReturn(updatedUser);
        when(userRepository.save(updatedUser)).thenReturn(updatedUser);

        User result = updateUserUseCase.execute(userId, newName, newEmail);

        assertNotNull(result);
        assertEquals(updatedUser, result);

        verify(userRepository).findById(userId);
        verify(user).updateProfile(newName, newEmail);
        verify(userRepository).save(updatedUser);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        UserId userId = UserId.of(999L);
        String newName = "João Santos";
        String newEmail = "joao.santos@email.com";

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () ->
            updateUserUseCase.execute(userId, newName, newEmail)
        );

        verify(userRepository).findById(userId);
        verify(userRepository, never()).save(any());
    }
}


