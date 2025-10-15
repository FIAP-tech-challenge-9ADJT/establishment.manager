package tech.challenge.establishment.manager.application.usecases.menuItem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tech.challenge.establishment.manager.domain.entities.MenuItem;
import tech.challenge.establishment.manager.domain.entities.Restaurant;
import tech.challenge.establishment.manager.domain.repositories.MenuItemRepository;
import tech.challenge.establishment.manager.domain.repositories.RestaurantRepository;
import tech.challenge.establishment.manager.domain.valueobjects.MenuItemId;
import tech.challenge.establishment.manager.domain.valueobjects.RestaurantId;
import tech.challenge.establishment.manager.domain.valueobjects.UserId;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MenuItemUseCaseImplTests {

    @Mock
    private MenuItemRepository menuItemRepository;
    
    @Mock
    private RestaurantRepository restaurantRepository;

    private CreateMenuItemUseCaseImpl createMenuItemUseCaseImpl;
    private FindAllMenuItemUseCaseImpl findAllMenuItemUseCaseImpl;
    private FindMenuItemByIdUseCaseImpl findMenuItemByIdUseCaseImpl;
    private UpdateMenuItemUseCaseImpl updateMenuItemUseCaseImpl;
    private DeleteMenuItemUseCaseImpl deleteMenuItemUseCaseImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        createMenuItemUseCaseImpl = new CreateMenuItemUseCaseImpl(menuItemRepository, restaurantRepository);
        findAllMenuItemUseCaseImpl = new FindAllMenuItemUseCaseImpl(menuItemRepository);
        findMenuItemByIdUseCaseImpl = new FindMenuItemByIdUseCaseImpl(menuItemRepository);
        updateMenuItemUseCaseImpl = new UpdateMenuItemUseCaseImpl(menuItemRepository, restaurantRepository);
        deleteMenuItemUseCaseImpl = new DeleteMenuItemUseCaseImpl(menuItemRepository, restaurantRepository);
    }

    @Test
    void shouldCreateMenuItem() {
        RestaurantId restaurantId = RestaurantId.of(1L);
        UserId ownerId = UserId.of(1L);
        Restaurant restaurant = mock(Restaurant.class);

        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));
        when(restaurant.getOwnerId()).thenReturn(ownerId);
        when(menuItemRepository.existsByNameAndRestaurant(any(), any())).thenReturn(false);
        when(menuItemRepository.save(any(MenuItem.class))).thenAnswer(i -> i.getArgument(0));

        MenuItem result = createMenuItemUseCaseImpl.execute("Pizza", "Delicious", 25.0, 
            "http://example.com/pizza.jpg", restaurantId, ownerId, false);

        assertNotNull(result);
        verify(menuItemRepository).save(any(MenuItem.class));
    }

    @Test
    void shouldFindAllMenuItems() {
        RestaurantId restaurantId = RestaurantId.of(1L);
        MenuItem menuItem = mock(MenuItem.class);

        when(menuItemRepository.findAllByRestaurant(restaurantId)).thenReturn(Arrays.asList(menuItem));

        List<MenuItem> result = findAllMenuItemUseCaseImpl.execute(restaurantId);

        assertEquals(1, result.size());
        verify(menuItemRepository).findAllByRestaurant(restaurantId);
    }

    @Test
    void shouldFindMenuItemById() {
        MenuItemId id = MenuItemId.of(1L);
        MenuItem menuItem = mock(MenuItem.class);

        when(menuItemRepository.findById(id)).thenReturn(Optional.of(menuItem));

        MenuItem result = findMenuItemByIdUseCaseImpl.execute(id);

        assertNotNull(result);
        verify(menuItemRepository).findById(id);
    }

    @Test
    void shouldUpdateMenuItem() {
        MenuItemId id = MenuItemId.of(1L);
        RestaurantId restaurantId = RestaurantId.of(1L);
        UserId ownerId = UserId.of(1L);
        MenuItem existing = MenuItem.of(1L, "Pizza", "Desc", 25.0, "http://example.com/pizza.jpg", restaurantId);
        Restaurant restaurant = mock(Restaurant.class);

        when(menuItemRepository.findById(id)).thenReturn(Optional.of(existing));
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));
        when(restaurant.getOwnerId()).thenReturn(ownerId);
        when(menuItemRepository.save(any(MenuItem.class))).thenAnswer(i -> i.getArgument(0));

        MenuItem result = updateMenuItemUseCaseImpl.execute(id, "New Pizza", "New Desc", 30.0, 
            "http://example.com/new.jpg", restaurantId, ownerId, false);

        assertNotNull(result);
        verify(menuItemRepository).save(any(MenuItem.class));
    }

    @Test
    void shouldDeleteMenuItem() {
        MenuItemId id = MenuItemId.of(1L);
        RestaurantId restaurantId = RestaurantId.of(1L);
        UserId ownerId = UserId.of(1L);
        MenuItem menuItem = MenuItem.of(1L, "Pizza", "Desc", 25.0, "http://example.com/pizza.jpg", restaurantId);
        Restaurant restaurant = mock(Restaurant.class);

        when(menuItemRepository.findById(id)).thenReturn(Optional.of(menuItem));
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));
        when(restaurant.getOwnerId()).thenReturn(ownerId);

        assertDoesNotThrow(() -> deleteMenuItemUseCaseImpl.execute(id, ownerId, false));

        verify(menuItemRepository).deleteById(id);
    }
}



