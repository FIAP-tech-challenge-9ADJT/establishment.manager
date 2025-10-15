package tech.challenge.establishment.manager.domain.usecases.restaurantowner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tech.challenge.establishment.manager.domain.entities.Address;
import tech.challenge.establishment.manager.domain.entities.Role;
import tech.challenge.establishment.manager.domain.entities.User;
import tech.challenge.establishment.manager.domain.exceptions.UserAlreadyExistsException;
import tech.challenge.establishment.manager.domain.repositories.RoleRepository;
import tech.challenge.establishment.manager.domain.repositories.UserRepository;
import tech.challenge.establishment.manager.domain.valueobjects.Email;
import tech.challenge.establishment.manager.domain.valueobjects.Login;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateRestaurantOwnerUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    private CreateRestaurantOwnerUseCase createRestaurantOwnerUseCase;

    @BeforeEach
    void setUp() {
        createRestaurantOwnerUseCase = new CreateRestaurantOwnerUseCase(userRepository, roleRepository);
    }

    @Test
    void shouldCreateRestaurantOwnerSuccessfully() {
        String name = "Maria Silva";
        String email = "maria@restaurant.com";
        String login = "maria123";
        String password = "password123";
        Address address = Address.create("Rua B", "Rio de Janeiro", "12345-678", "456", null);
        Role.RoleName roleName = Role.RoleName.RESTAURANT_OWNER;

        Role role = Role.create(roleName);
        User expectedUser = User.create(name, email, login, password, address).addRole(role);

        when(userRepository.existsByEmail(Email.of(email))).thenReturn(false);
        when(userRepository.existsByLogin(Login.of(login))).thenReturn(false);
        when(roleRepository.findByName(roleName)).thenReturn(Optional.of(role));
        when(userRepository.save(any(User.class))).thenReturn(expectedUser);

        User result = createRestaurantOwnerUseCase.execute(name, email, login, password, address, roleName);

        assertNotNull(result);
        assertEquals(name, result.getName().value());
        assertEquals(email, result.getEmail().value());
        assertTrue(result.hasRole(Role.RoleName.RESTAURANT_OWNER));

        verify(userRepository).existsByEmail(Email.of(email));
        verify(userRepository).existsByLogin(Login.of(login));
        verify(roleRepository).findByName(roleName);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        String email = "existing@restaurant.com";
        
        when(userRepository.existsByEmail(Email.of(email))).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class, () ->
            createRestaurantOwnerUseCase.execute("Maria", email, "maria123", "password", null, Role.RoleName.RESTAURANT_OWNER)
        );

        verify(userRepository).existsByEmail(Email.of(email));
        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenLoginAlreadyExists() {
        String email = "maria@restaurant.com";
        String login = "existinglogin";
        
        when(userRepository.existsByEmail(Email.of(email))).thenReturn(false);
        when(userRepository.existsByLogin(Login.of(login))).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class, () ->
            createRestaurantOwnerUseCase.execute("Maria", email, login, "password", null, Role.RoleName.RESTAURANT_OWNER)
        );

        verify(userRepository).existsByEmail(Email.of(email));
        verify(userRepository).existsByLogin(Login.of(login));
        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenRoleNotFound() {
        String email = "maria@restaurant.com";
        String login = "maria123";
        Role.RoleName roleName = Role.RoleName.RESTAURANT_OWNER;
        
        when(userRepository.existsByEmail(Email.of(email))).thenReturn(false);
        when(userRepository.existsByLogin(Login.of(login))).thenReturn(false);
        when(roleRepository.findByName(roleName)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
            createRestaurantOwnerUseCase.execute("Maria", email, login, "password", null, roleName)
        );

        verify(roleRepository).findByName(roleName);
        verify(userRepository, never()).save(any());
    }
}

