package tech.challenge.establishment.manager.application.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tech.challenge.establishment.manager.application.usecases.CreateUserUseCaseImpl;
import tech.challenge.establishment.manager.domain.entities.Address;
import tech.challenge.establishment.manager.domain.entities.Role;
import tech.challenge.establishment.manager.domain.entities.User;
import tech.challenge.establishment.manager.domain.usecases.user.FindUserUseCase;
import tech.challenge.establishment.manager.domain.usecases.user.UpdateUserUseCase;
import tech.challenge.establishment.manager.domain.valueobjects.UserId;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserApplicationServiceTest {

    @Mock
    private CreateUserUseCaseImpl createUserUseCase;

    @Mock
    private FindUserUseCase findUserUseCase;

    @Mock
    private UpdateUserUseCase updateUserUseCase;

    @Mock
    private User user;

    @Mock
    private Address address;

    private UserApplicationService userApplicationService;

    @BeforeEach
    void setUp() {
        userApplicationService = new UserApplicationService(
                createUserUseCase,
                findUserUseCase,
                updateUserUseCase
        );
    }

    @Test
    void shouldCreateCustomerSuccessfully() {
        String name = "João Silva";
        String email = "joao@email.com";
        String login = "joao123";
        String password = "password123";

        when(createUserUseCase.execute(name, email, login, password, address, Role.RoleName.USER))
                .thenReturn(user);

        User result = userApplicationService.createCustomer(name, email, login, password, address);

        assertNotNull(result);
        assertEquals(user, result);

        verify(createUserUseCase).execute(name, email, login, password, address, Role.RoleName.USER);
    }

    @Test
    void shouldCreateRestaurantOwnerSuccessfully() {
        String name = "Maria Silva";
        String email = "maria@restaurant.com";
        String login = "maria123";
        String password = "password123";

        when(createUserUseCase.execute(name, email, login, password, address, Role.RoleName.RESTAURANT_OWNER))
                .thenReturn(user);

        User result = userApplicationService.createRestaurantOwner(name, email, login, password, address);

        assertNotNull(result);
        assertEquals(user, result);

        verify(createUserUseCase).execute(name, email, login, password, address, Role.RoleName.RESTAURANT_OWNER);
    }

    @Test
    void shouldCreateAdminSuccessfully() {
        String name = "Admin User";
        String email = "admin@system.com";
        String login = "admin123";
        String password = "password123";

        when(createUserUseCase.execute(name, email, login, password, address, Role.RoleName.ADMIN))
                .thenReturn(user);

        User result = userApplicationService.createAdmin(name, email, login, password, address);

        assertNotNull(result);
        assertEquals(user, result);

        verify(createUserUseCase).execute(name, email, login, password, address, Role.RoleName.ADMIN);
    }

    @Test
    void shouldFindUserSuccessfully() {
        UserId userId = UserId.of(1L);

        when(findUserUseCase.execute(userId)).thenReturn(user);

        User result = userApplicationService.findUser(userId);

        assertNotNull(result);
        assertEquals(user, result);

        verify(findUserUseCase).execute(userId);
    }

    @Test
    void shouldUpdateUserSuccessfully() {
        UserId userId = UserId.of(1L);
        String newName = "João Santos";
        String newEmail = "joao.santos@email.com";

        when(updateUserUseCase.execute(userId, newName, newEmail)).thenReturn(user);

        User result = userApplicationService.updateUser(userId, newName, newEmail);

        assertNotNull(result);
        assertEquals(user, result);

        verify(updateUserUseCase).execute(userId, newName, newEmail);
    }
}


