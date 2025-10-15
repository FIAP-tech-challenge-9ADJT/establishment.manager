package tech.challenge.establishment.manager.domain.usecases.auth;

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
class ChangePasswordUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private User user;

    @Mock
    private User updatedUser;

    private ChangePasswordUseCase changePasswordUseCase;

    @BeforeEach
    void setUp() {
        changePasswordUseCase = new ChangePasswordUseCase(userRepository);
    }

    @Test
    void shouldChangePasswordSuccessfully() {
        UserId userId = UserId.of(1L);
        String newPassword = "newPassword123";

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(user.changePassword(newPassword)).thenReturn(updatedUser);
        when(userRepository.save(updatedUser)).thenReturn(updatedUser);

        changePasswordUseCase.execute(userId, newPassword);

        verify(userRepository).findById(userId);
        verify(user).changePassword(newPassword);
        verify(userRepository).save(updatedUser);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        UserId userId = UserId.of(999L);
        String newPassword = "newPassword123";

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () ->
            changePasswordUseCase.execute(userId, newPassword)
        );

        verify(userRepository).findById(userId);
        verify(userRepository, never()).save(any());
    }
}
