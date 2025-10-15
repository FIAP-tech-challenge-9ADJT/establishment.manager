package tech.challenge.establishment.manager.domain.usecases.menuItem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tech.challenge.establishment.manager.domain.entities.MenuItem;
import tech.challenge.establishment.manager.domain.entities.Restaurant;
import tech.challenge.establishment.manager.domain.exceptions.AccessDeniedException;
import tech.challenge.establishment.manager.domain.exceptions.MenuItemAlreadyExistsException;
import tech.challenge.establishment.manager.domain.repositories.MenuItemRepository;
import tech.challenge.establishment.manager.domain.repositories.RestaurantRepository;
import tech.challenge.establishment.manager.domain.valueobjects.RestaurantId;
import tech.challenge.establishment.manager.domain.valueobjects.UserId;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CreateMenuItemUseCaseTest {

    @Mock
    private MenuItemRepository menuItemRepository;
    @Mock
    private RestaurantRepository restaurantRepository;

    private CreateMenuItemUseCase createMenuItemUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        createMenuItemUseCase = new CreateMenuItemUseCase(menuItemRepository, restaurantRepository);
    }

    @Test
    void shouldCreateMenuItemWhenUserIsOwner() {
        String name = "Pizza Margherita";
        String description = "Traditional Italian pizza";
        Double price = 25.99;
        String photoUrl = "http://example.com/pizza.jpg";
        RestaurantId restaurantId = RestaurantId.of(1L);
        UserId ownerId = UserId.of(1L);
        Restaurant restaurant = mock(Restaurant.class);
        MenuItem savedMenuItem = mock(MenuItem.class);

        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));
        when(restaurant.getOwnerId()).thenReturn(ownerId);
        when(menuItemRepository.existsByNameAndRestaurant(name, restaurantId)).thenReturn(false);
        when(menuItemRepository.save(any(MenuItem.class))).thenReturn(savedMenuItem);

        MenuItem result = createMenuItemUseCase.execute(name, description, price, photoUrl, restaurantId, ownerId, false);

        assertNotNull(result);
        assertEquals(savedMenuItem, result);
        verify(restaurantRepository).findById(restaurantId);
        verify(menuItemRepository).existsByNameAndRestaurant(name, restaurantId);
        verify(menuItemRepository).save(any(MenuItem.class));
    }

    @Test
    void shouldCreateMenuItemWhenUserIsAdmin() {
        String name = "Pizza Margherita";
        String description = "Traditional Italian pizza";
        Double price = 25.99;
        String photoUrl = "http://example.com/pizza.jpg";
        RestaurantId restaurantId = RestaurantId.of(1L);
        UserId currentUserId = UserId.of(2L);
        UserId ownerId = UserId.of(1L);
        Restaurant restaurant = mock(Restaurant.class);
        MenuItem savedMenuItem = mock(MenuItem.class);

        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));
        when(restaurant.getOwnerId()).thenReturn(ownerId);
        when(menuItemRepository.existsByNameAndRestaurant(name, restaurantId)).thenReturn(false);
        when(menuItemRepository.save(any(MenuItem.class))).thenReturn(savedMenuItem);

        MenuItem result = createMenuItemUseCase.execute(name, description, price, photoUrl, restaurantId, currentUserId, true);

        assertNotNull(result);
        assertEquals(savedMenuItem, result);
        verify(restaurantRepository).findById(restaurantId);
        verify(menuItemRepository).existsByNameAndRestaurant(name, restaurantId);
        verify(menuItemRepository).save(any(MenuItem.class));
    }

    @Test
    void shouldThrowExceptionWhenRestaurantNotFound() {
        String name = "Pizza Margherita";
        String description = "Traditional Italian pizza";
        Double price = 25.99;
        String photoUrl = "http://example.com/pizza.jpg";
        RestaurantId restaurantId = RestaurantId.of(1L);
        UserId currentUserId = UserId.of(1L);

        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            createMenuItemUseCase.execute(name, description, price, photoUrl, restaurantId, currentUserId, false);
        });

        verify(restaurantRepository).findById(restaurantId);
        verify(menuItemRepository, never()).save(any(MenuItem.class));
    }

    @Test
    void shouldThrowAccessDeniedExceptionWhenUserIsNotOwnerAndNotAdmin() {
        String name = "Pizza Margherita";
        String description = "Traditional Italian pizza";
        Double price = 25.99;
        String photoUrl = "http://example.com/pizza.jpg";
        RestaurantId restaurantId = RestaurantId.of(1L);
        UserId currentUserId = UserId.of(2L);
        UserId ownerId = UserId.of(1L);
        Restaurant restaurant = mock(Restaurant.class);

        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));
        when(restaurant.getOwnerId()).thenReturn(ownerId);

        assertThrows(AccessDeniedException.class, () -> {
            createMenuItemUseCase.execute(name, description, price, photoUrl, restaurantId, currentUserId, false);
        });

        verify(restaurantRepository).findById(restaurantId);
        verify(menuItemRepository, never()).save(any(MenuItem.class));
    }

    @Test
    void shouldThrowMenuItemAlreadyExistsException() {
        String name = "Pizza Margherita";
        String description = "Traditional Italian pizza";
        Double price = 25.99;
        String photoUrl = "http://example.com/pizza.jpg";
        RestaurantId restaurantId = RestaurantId.of(1L);
        UserId ownerId = UserId.of(1L);
        Restaurant restaurant = mock(Restaurant.class);

        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));
        when(restaurant.getOwnerId()).thenReturn(ownerId);
        when(menuItemRepository.existsByNameAndRestaurant(name, restaurantId)).thenReturn(true);

        assertThrows(MenuItemAlreadyExistsException.class, () -> {
            createMenuItemUseCase.execute(name, description, price, photoUrl, restaurantId, ownerId, false);
        });

        verify(restaurantRepository).findById(restaurantId);
        verify(menuItemRepository).existsByNameAndRestaurant(name, restaurantId);
        verify(menuItemRepository, never()).save(any(MenuItem.class));
    }
}


