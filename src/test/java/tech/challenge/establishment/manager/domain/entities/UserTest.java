package tech.challenge.establishment.manager.domain.entities;

import org.junit.jupiter.api.Test;
import tech.challenge.establishment.manager.domain.valueobjects.Email;
import tech.challenge.establishment.manager.domain.valueobjects.Login;
import tech.challenge.establishment.manager.domain.valueobjects.Name;
import tech.challenge.establishment.manager.domain.valueobjects.Password;

import java.time.LocalDateTime;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void shouldCreateUserSuccessfully() {
        String name = "João Silva";
        String email = "joao@email.com";
        String login = "joao123";
        String password = "password123";
        Address address = Address.create("Rua A", "São Paulo", "01234-567", "123", null);

        User user = User.create(name, email, login, password, address);

        assertNotNull(user);
        assertEquals(name, user.getName().value());
        assertEquals(email, user.getEmail().value());
        assertEquals(login, user.getLogin().value());
        assertEquals(password, user.getPassword().value());
        assertEquals(address, user.getAddress());
        assertNotNull(user.getRoles());
        assertTrue(user.getRoles().isEmpty());
        assertNotNull(user.getCreatedAt());
        assertNotNull(user.getUpdatedAt());
    }

    @Test
    void shouldThrowExceptionForNullName() {
        assertThrows(NullPointerException.class, () ->
            User.create(null, "email@test.com", "login", "password", null)
        );
    }

    @Test
    void shouldThrowExceptionForNullEmail() {
        assertThrows(NullPointerException.class, () ->
            User.create("Name", null, "login", "password", null)
        );
    }

    @Test
    void shouldThrowExceptionForNullLogin() {
        assertThrows(NullPointerException.class, () ->
            User.create("Name", "email@test.com", null, "password", null)
        );
    }

    @Test
    void shouldThrowExceptionForNullPassword() {
        assertThrows(NullPointerException.class, () ->
            User.create("Name", "email@test.com", "login", null, null)
        );
    }

    @Test
    void shouldAddRoleSuccessfully() {
        User user = User.create("João", "joao@email.com", "joao123", "password123", null);
        Role role = Role.create(Role.RoleName.USER);

        User userWithRole = user.addRole(role);

        assertTrue(userWithRole.hasRole(Role.RoleName.USER));
        assertEquals(1, userWithRole.getRoles().size());
    }

    @Test
    void shouldCheckIfUserIsAdmin() {
        User user = User.create("Admin", "admin@email.com", "admin123", "password123", null);
        Role adminRole = Role.create(Role.RoleName.ADMIN);

        User adminUser = user.addRole(adminRole);

        assertTrue(adminUser.isAdmin());
        assertTrue(adminUser.hasRole(Role.RoleName.ADMIN));
    }

    @Test
    void shouldChangePasswordSuccessfully() {
        User user = User.create("João", "joao@email.com", "joao123", "password123", null);
        String newPassword = "newPassword456";

        User userWithNewPassword = user.changePassword(newPassword);

        assertEquals(newPassword, userWithNewPassword.getPassword().value());
        assertNotNull(userWithNewPassword.getUpdatedAt());
    }

    @Test
    void shouldUpdateProfileSuccessfully() {
        User user = User.create("João", "joao@email.com", "joao123", "password123", null);
        String newName = "João Santos";
        String newEmail = "joao.santos@email.com";

        User updatedUser = user.updateProfile(newName, newEmail);

        assertEquals(newName, updatedUser.getName().value());
        assertEquals(newEmail, updatedUser.getEmail().value());
        assertEquals(user.getLogin(), updatedUser.getLogin());
        assertEquals(user.getPassword(), updatedUser.getPassword());
    }

    @Test
    void shouldUpdateAddressSuccessfully() {
        User user = User.create("João", "joao@email.com", "joao123", "password123", null);
        Address newAddress = Address.create("Rua B", "Rio de Janeiro", "12345-678", "456", null);

        User userWithNewAddress = user.updateAddress(newAddress);

        assertEquals(newAddress, userWithNewAddress.getAddress());
        assertEquals(user.getName(), userWithNewAddress.getName());
        assertEquals(user.getEmail(), userWithNewAddress.getEmail());
    }

    @Test
    void shouldBeEqualWhenSameId() {
        User user1 = User.of(1L, "João", "joao@email.com", "joao123", "password123", null, new HashSet<>(), LocalDateTime.now(), LocalDateTime.now());
        User user2 = User.of(1L, "Maria", "maria@email.com", "maria123", "password456", null, new HashSet<>(), LocalDateTime.now(), LocalDateTime.now());

        assertEquals(user1, user2);
        assertEquals(user1.hashCode(), user2.hashCode());
    }

    @Test
    void shouldNotBeEqualWhenDifferentId() {
        User user1 = User.of(1L, "João", "joao@email.com", "joao123", "password123", null, new HashSet<>(), LocalDateTime.now(), LocalDateTime.now());
        User user2 = User.of(2L, "João", "joao@email.com", "joao123", "password123", null, new HashSet<>(), LocalDateTime.now(), LocalDateTime.now());

        assertNotEquals(user1, user2);
    }

    @Test
    void shouldBeEqualToItself() {
        User user = User.create("João", "joao@email.com", "joao123", "password123", null);
        assertEquals(user, user);
    }

    @Test
    void shouldNotBeEqualToNull() {
        User user = User.create("João", "joao@email.com", "joao123", "password123", null);
        assertNotEquals(user, null);
    }

    @Test
    void shouldNotBeEqualToDifferentClass() {
        User user = User.create("João", "joao@email.com", "joao123", "password123", null);
        assertNotEquals(user, "String");
    }

    @Test
    void shouldReturnFalseWhenUserIsNotAdmin() {
        User user = User.create("User", "user@email.com", "user123", "password123", null);
        Role userRole = Role.create(Role.RoleName.USER);
        User userWithRole = user.addRole(userRole);

        assertFalse(userWithRole.isAdmin());
    }

    @Test
    void shouldReturnFalseWhenHasRoleNotPresent() {
        User user = User.create("User", "user@email.com", "user123", "password123", null);
        Role userRole = Role.create(Role.RoleName.USER);
        User userWithRole = user.addRole(userRole);

        assertFalse(userWithRole.hasRole(Role.RoleName.ADMIN));
    }

    @Test
    void shouldAddMultipleRoles() {
        User user = User.create("User", "user@email.com", "user123", "password123", null);
        Role userRole = Role.create(Role.RoleName.USER);
        Role ownerRole = Role.create(Role.RoleName.RESTAURANT_OWNER);

        User userWithRoles = user.addRole(userRole).addRole(ownerRole);

        assertTrue(userWithRoles.hasRole(Role.RoleName.USER));
        assertTrue(userWithRoles.hasRole(Role.RoleName.RESTAURANT_OWNER));
        assertEquals(2, userWithRoles.getRoles().size());
    }
}
