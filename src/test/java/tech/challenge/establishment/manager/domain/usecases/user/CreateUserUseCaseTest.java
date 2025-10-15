package tech.challenge.establishment.manager.domain.usecases.user;

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
class CreateUserUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    private CreateUserUseCase createUserUseCase;

    @BeforeEach
    void setUp() {
        createUserUseCase = new CreateUserUseCase(userRepository, roleRepository);
    }

    @Test
    void shouldCreateUserSuccessfully() {
        String name = "João Silva";
        String email = "joao@email.com";
        String login = "joao123";
        String password = "password123";
        Address address = Address.create("Rua A", "São Paulo", "01234-567", "123", null);
        Role.RoleName roleName = Role.RoleName.USER;

        Role role = Role.create(roleName);
        User expectedUser = User.create(name, email, login, password, address).addRole(role);

        when(userRepository.existsByEmail(any(Email.class))).thenReturn(false);
        when(userRepository.existsByLogin(any(Login.class))).thenReturn(false);
        when(roleRepository.findByName(roleName)).thenReturn(Optional.of(role));
        when(userRepository.save(any(User.class))).thenReturn(expectedUser);

        User result = createUserUseCase.execute(name, email, login, password, address, roleName);

        assertNotNull(result);
        assertEquals(name, result.getName().value());
        assertEquals(email, result.getEmail().value());
        assertEquals(login, result.getLogin().value());
        assertTrue(result.hasRole(roleName));

        verify(userRepository).existsByEmail(Email.of(email));
        verify(userRepository).existsByLogin(Login.of(login));
        verify(roleRepository).findByName(roleName);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        String email = "existing@email.com";
        
        when(userRepository.existsByEmail(Email.of(email))).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class, () ->
            createUserUseCase.execute("João", email, "joao123", "password", null, Role.RoleName.USER)
        );

        verify(userRepository).existsByEmail(Email.of(email));
        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenLoginAlreadyExists() {
        String email = "joao@email.com";
        String login = "existinglogin";
        
        when(userRepository.existsByEmail(Email.of(email))).thenReturn(false);
        when(userRepository.existsByLogin(Login.of(login))).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class, () ->
            createUserUseCase.execute("João", email, login, "password", null, Role.RoleName.USER)
        );

        verify(userRepository).existsByEmail(Email.of(email));
        verify(userRepository).existsByLogin(Login.of(login));
        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenRoleNotFound() {
        String email = "joao@email.com";
        String login = "joao123";
        Role.RoleName roleName = Role.RoleName.ADMIN;
        
        when(userRepository.existsByEmail(Email.of(email))).thenReturn(false);
        when(userRepository.existsByLogin(Login.of(login))).thenReturn(false);
        when(roleRepository.findByName(roleName)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
            createUserUseCase.execute("João", email, login, "password", null, roleName)
        );

        verify(roleRepository).findByName(roleName);
        verify(userRepository, never()).save(any());
    }
}

