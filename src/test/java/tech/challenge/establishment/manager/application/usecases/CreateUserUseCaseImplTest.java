package tech.challenge.establishment.manager.application.usecases;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class CreateUserUseCaseImplTest {

    @Mock
    private UserRepository userRepository;
    
    @Mock
    private RoleRepository roleRepository;
    
    @Mock
    private PasswordEncoder passwordEncoder;

    private CreateUserUseCaseImpl createUserUseCaseImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        createUserUseCaseImpl = new CreateUserUseCaseImpl(userRepository, roleRepository, passwordEncoder);
    }

    @Test
    void shouldCreateUserWithEncodedPassword() {
        String name = "João Silva";
        String email = "joao@email.com";
        String login = "joao123";
        String password = "password123";
        String encodedPassword = "encoded_password";
        Address address = Address.create("Rua A", "São Paulo", "01234567", "100", null);
        Role role = Role.create(Role.RoleName.USER);

        when(userRepository.existsByEmail(Email.of(email))).thenReturn(false);
        when(userRepository.existsByLogin(Login.of(login))).thenReturn(false);
        when(passwordEncoder.encode(password)).thenReturn(encodedPassword);
        when(roleRepository.findByName(Role.RoleName.USER)).thenReturn(Optional.of(role));
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        User result = createUserUseCaseImpl.execute(name, email, login, password, address, Role.RoleName.USER);

        assertNotNull(result);
        verify(passwordEncoder).encode(password);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenEmailExists() {
        String email = "existing@email.com";

        when(userRepository.existsByEmail(Email.of(email))).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class, () -> {
            createUserUseCaseImpl.execute("Name", email, "login", "password", null, Role.RoleName.USER);
        });

        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenLoginExists() {
        String email = "email@test.com";
        String login = "existinglogin";

        when(userRepository.existsByEmail(Email.of(email))).thenReturn(false);
        when(userRepository.existsByLogin(Login.of(login))).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class, () -> {
            createUserUseCaseImpl.execute("Name", email, login, "password", null, Role.RoleName.USER);
        });

        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenRoleNotFound() {
        String email = "email@test.com";
        String login = "login123";

        when(userRepository.existsByEmail(Email.of(email))).thenReturn(false);
        when(userRepository.existsByLogin(Login.of(login))).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encoded");
        when(roleRepository.findByName(Role.RoleName.USER)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            createUserUseCaseImpl.execute("Name", email, login, "password", null, Role.RoleName.USER);
        });

        verify(userRepository, never()).save(any());
    }
}



