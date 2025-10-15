package tech.challenge.establishment.manager.domain.usecases.admin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tech.challenge.establishment.manager.domain.entities.Address;
import tech.challenge.establishment.manager.domain.entities.Role;
import tech.challenge.establishment.manager.domain.entities.User;
import tech.challenge.establishment.manager.domain.exceptions.UserNotFoundException;
import tech.challenge.establishment.manager.domain.repositories.UserRepository;
import tech.challenge.establishment.manager.domain.valueobjects.UserId;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DeleteUserUseCaseTest {

    @Mock
    private UserRepository userRepository;

    private DeleteUserUseCase deleteUserUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        deleteUserUseCase = new DeleteUserUseCase(userRepository);
    }

    @Test
    void shouldDeleteUserSuccessfully() {
        UserId userId = UserId.of(1L);
        Address address = Address.create("Rua A", "São Paulo", "01234567", "100", null);
        
        Role userRole = Role.of(1L, Role.RoleName.USER);
        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        
        User user = User.of(1L, "João Silva", "joao@email.com", "joao123", "password123",
                address, roles, LocalDateTime.now(), LocalDateTime.now());

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        doNothing().when(userRepository).delete(userId);

        assertDoesNotThrow(() -> deleteUserUseCase.execute(userId));

        verify(userRepository).findById(userId);
        verify(userRepository).delete(userId);
    }

    @Test
    void shouldDeleteAdminUserSuccessfully() {
        UserId userId = UserId.of(1L);
        Address address = Address.create("Rua B", "Rio", "12345678", "200", null);
        
        Role adminRole = Role.of(1L, Role.RoleName.ADMIN);
        Set<Role> roles = new HashSet<>();
        roles.add(adminRole);
        
        User adminUser = User.of(1L, "Admin User", "admin@email.com", "admin123", "password123",
                address, roles, LocalDateTime.now(), LocalDateTime.now());

        when(userRepository.findById(userId)).thenReturn(Optional.of(adminUser));
        doNothing().when(userRepository).delete(userId);

        assertDoesNotThrow(() -> deleteUserUseCase.execute(userId));

        verify(userRepository).findById(userId);
        verify(userRepository).delete(userId);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        UserId userId = UserId.of(999L);

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            deleteUserUseCase.execute(userId);
        });

        verify(userRepository).findById(userId);
        verify(userRepository, never()).delete(any());
    }
}



