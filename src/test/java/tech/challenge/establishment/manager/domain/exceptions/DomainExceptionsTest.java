package tech.challenge.establishment.manager.domain.exceptions;

import org.junit.jupiter.api.Test;
import tech.challenge.establishment.manager.domain.entities.RestaurantAddress;
import tech.challenge.establishment.manager.domain.valueobjects.*;

import static org.junit.jupiter.api.Assertions.*;

class DomainExceptionsTest {

    @Test
    void shouldCreateUserNotFoundException() {
        UserId userId = UserId.of(1L);
        UserNotFoundException exception = new UserNotFoundException(userId);
        
        assertNotNull(exception);
        assertTrue(exception.getMessage().contains("1"));
    }

    @Test
    void shouldCreateUserNotFoundExceptionWithLogin() {
        String login = "user123";
        UserNotFoundException exception = new UserNotFoundException(login);
        
        assertNotNull(exception);
        assertTrue(exception.getMessage().contains("user123"));
    }

    @Test
    void shouldCreateUserAlreadyExistsException() {
        UserAlreadyExistsException exception = new UserAlreadyExistsException("email", "test@example.com");
        
        assertNotNull(exception);
        assertTrue(exception.getMessage().contains("test@example.com"));
    }

    @Test
    void shouldCreateUserAlreadyExistsExceptionWithLogin() {
        UserAlreadyExistsException exception = new UserAlreadyExistsException("login", "user123");
        
        assertNotNull(exception);
        assertTrue(exception.getMessage().contains("user123"));
    }

    @Test
    void shouldCreateRestaurantNotFoundException() {
        RestaurantId restaurantId = RestaurantId.of(1L);
        RestaurantNotFoundException exception = new RestaurantNotFoundException(restaurantId);
        
        assertNotNull(exception);
        assertTrue(exception.getMessage().contains("1"));
    }

    @Test
    void shouldCreateRestaurantAlreadyExistsException() {
        RestaurantAddress address = RestaurantAddress.create("Rua A", "São Paulo", "01234567", "100", null);
        RestaurantAlreadyExistsException exception = new RestaurantAlreadyExistsException("Restaurant Name", address);
        
        assertNotNull(exception);
        assertTrue(exception.getMessage().contains("Restaurant Name"));
    }

    @Test
    void shouldCreateMenuItemNotFoundException() {
        MenuItemId menuItemId = MenuItemId.of(1L);
        MenuItemNotFoundException exception = new MenuItemNotFoundException(menuItemId);
        
        assertNotNull(exception);
        assertTrue(exception.getMessage().contains("1"));
    }

    @Test
    void shouldCreateMenuItemAlreadyExistsException() {
        String name = "Pizza";
        RestaurantId restaurantId = RestaurantId.of(1L);
        MenuItemAlreadyExistsException exception = new MenuItemAlreadyExistsException(name, restaurantId);
        
        assertNotNull(exception);
        assertTrue(exception.getMessage().contains("Pizza"));
        assertTrue(exception.getMessage().contains("1"));
    }

    @Test
    void shouldCreateInvalidCredentialsException() {
        InvalidCredentialsException exception = new InvalidCredentialsException();
        
        assertNotNull(exception);
        assertNotNull(exception.getMessage());
    }

    @Test
    void shouldCreateAccessDeniedException() {
        String message = "Access denied";
        AccessDeniedException exception = new AccessDeniedException(message);
        
        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
    }

    @Test
    void shouldVerifyExceptionsInheritFromDomainException() {
        assertTrue(new UserNotFoundException(UserId.of(1L)) instanceof DomainException);
        assertTrue(new UserAlreadyExistsException("email", "test@test.com") instanceof DomainException);
        assertTrue(new RestaurantNotFoundException(RestaurantId.of(1L)) instanceof DomainException);
        RestaurantAddress address = RestaurantAddress.create("Rua A", "Cidade", "01234567", "100", null);
        assertTrue(new RestaurantAlreadyExistsException("Test", address) instanceof DomainException);
        assertTrue(new MenuItemNotFoundException(MenuItemId.of(1L)) instanceof DomainException);
        assertTrue(new MenuItemAlreadyExistsException("Test", RestaurantId.of(1L)) instanceof DomainException);
        assertTrue(new InvalidCredentialsException() instanceof DomainException);
        assertTrue(new AccessDeniedException("Test") instanceof DomainException);
    }

    @Test
    void shouldVerifyExceptionsAreRuntimeExceptions() {
        assertTrue(new UserNotFoundException(UserId.of(1L)) instanceof RuntimeException);
        assertTrue(new InvalidCredentialsException() instanceof RuntimeException);
        assertTrue(new AccessDeniedException("Test") instanceof RuntimeException);
    }
}

