package tech.challenge.establishment.manager.domain.usecases.restaurantowner;

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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UpdateRestaurantOwnerUseCaseTest {

    @Mock
    private UserRepository userRepository;

    private UpdateRestaurantOwnerUseCase updateRestaurantOwnerUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        updateRestaurantOwnerUseCase = new UpdateRestaurantOwnerUseCase(userRepository);
    }

    @Test
    void shouldUpdateRestaurantOwnerSuccessfully() {
        UserId userId = UserId.of(1L);
        Address address = Address.create("Rua A", "São Paulo", "01234567", "100", null);
        
        Role restaurantOwnerRole = Role.of(1L, Role.RoleName.RESTAURANT_OWNER);
        Set<Role> roles = new HashSet<>();
        roles.add(restaurantOwnerRole);
        
        User existingUser = User.of(1L, "João Silva", "joao@email.com", "joao123", "password123",
                address, roles, LocalDateTime.now(), LocalDateTime.now());
        
        User updatedUser = existingUser.updateProfile("João Silva Atualizado", "joao.novo@email.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        User result = updateRestaurantOwnerUseCase.execute(userId, "João Silva Atualizado", "joao.novo@email.com");

        assertNotNull(result);
        verify(userRepository).findById(userId);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        UserId userId = UserId.of(999L);

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            updateRestaurantOwnerUseCase.execute(userId, "Nome", "email@test.com");
        });

        verify(userRepository).findById(userId);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenUserIsNotRestaurantOwner() {
        UserId userId = UserId.of(1L);
        Address address = Address.create("Rua B", "Rio", "12345678", "200", null);
        
        Role userRole = Role.of(2L, Role.RoleName.USER);
        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        
        User user = User.of(1L, "Maria Silva", "maria@email.com", "maria123", "password123",
                address, roles, LocalDateTime.now(), LocalDateTime.now());

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        assertThrows(UserNotFoundException.class, () -> {
            updateRestaurantOwnerUseCase.execute(userId, "Nome Atualizado", "email.novo@test.com");
        });

        verify(userRepository).findById(userId);
        verify(userRepository, never()).save(any(User.class));
    }
}



