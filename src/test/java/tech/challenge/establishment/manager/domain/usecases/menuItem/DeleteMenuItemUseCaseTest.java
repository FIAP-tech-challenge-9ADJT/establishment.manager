package tech.challenge.establishment.manager.domain.usecases.menuItem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tech.challenge.establishment.manager.domain.entities.MenuItem;
import tech.challenge.establishment.manager.domain.entities.Restaurant;
import tech.challenge.establishment.manager.domain.exceptions.AccessDeniedException;
import tech.challenge.establishment.manager.domain.exceptions.MenuItemNotFoundException;
import tech.challenge.establishment.manager.domain.repositories.MenuItemRepository;
import tech.challenge.establishment.manager.domain.repositories.RestaurantRepository;
import tech.challenge.establishment.manager.domain.valueobjects.MenuItemId;
import tech.challenge.establishment.manager.domain.valueobjects.RestaurantId;
import tech.challenge.establishment.manager.domain.valueobjects.UserId;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DeleteMenuItemUseCaseTest {

    @Mock
    private MenuItemRepository menuItemRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    private DeleteMenuItemUseCase deleteMenuItemUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        deleteMenuItemUseCase = new DeleteMenuItemUseCase(menuItemRepository, restaurantRepository);
    }

    @Test
    void shouldDeleteMenuItemSuccessfully() {
        MenuItemId menuItemId = MenuItemId.of(1L);
        RestaurantId restaurantId = RestaurantId.of(1L);
        UserId ownerId = UserId.of(1L);
        
        MenuItem menuItem = MenuItem.of(1L, "Pizza", "Deliciosa", 25.0, 
            "http://example.com/pizza.jpg", restaurantId);
        Restaurant restaurant = mock(Restaurant.class);
        
        when(menuItemRepository.findById(menuItemId)).thenReturn(Optional.of(menuItem));
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));
        when(restaurant.getOwnerId()).thenReturn(ownerId);

        deleteMenuItemUseCase.execute(menuItemId, ownerId, false);

        verify(menuItemRepository).findById(menuItemId);
        verify(menuItemRepository).deleteById(menuItemId);
    }

    @Test
    void shouldThrowExceptionWhenMenuItemNotFound() {
        MenuItemId menuItemId = MenuItemId.of(999L);
        
        when(menuItemRepository.findById(menuItemId)).thenReturn(Optional.empty());

        assertThrows(MenuItemNotFoundException.class, () -> {
            deleteMenuItemUseCase.execute(menuItemId, UserId.of(1L), false);
        });

        verify(menuItemRepository).findById(menuItemId);
        verify(menuItemRepository, never()).deleteById(any());
    }

    @Test
    void shouldAllowAdminToDeleteMenuItem() {
        MenuItemId menuItemId = MenuItemId.of(1L);
        RestaurantId restaurantId = RestaurantId.of(1L);
        UserId adminId = UserId.of(2L);
        UserId ownerId = UserId.of(1L);
        
        MenuItem menuItem = MenuItem.of(1L, "Pizza", "Deliciosa", 25.0, 
            "http://example.com/pizza.jpg", restaurantId);
        Restaurant restaurant = mock(Restaurant.class);
        
        when(menuItemRepository.findById(menuItemId)).thenReturn(Optional.of(menuItem));
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));
        when(restaurant.getOwnerId()).thenReturn(ownerId);

        deleteMenuItemUseCase.execute(menuItemId, adminId, true);

        verify(menuItemRepository).deleteById(menuItemId);
    }

    @Test
    void shouldThrowExceptionWhenUserIsNotOwner() {
        MenuItemId menuItemId = MenuItemId.of(1L);
        RestaurantId restaurantId = RestaurantId.of(1L);
        UserId currentUserId = UserId.of(2L);
        UserId ownerId = UserId.of(1L);
        
        MenuItem menuItem = MenuItem.of(1L, "Pizza", "Deliciosa", 25.0, 
            "http://example.com/pizza.jpg", restaurantId);
        Restaurant restaurant = mock(Restaurant.class);
        
        when(menuItemRepository.findById(menuItemId)).thenReturn(Optional.of(menuItem));
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));
        when(restaurant.getOwnerId()).thenReturn(ownerId);

        assertThrows(AccessDeniedException.class, () -> {
            deleteMenuItemUseCase.execute(menuItemId, currentUserId, false);
        });

        verify(menuItemRepository, never()).deleteById(any());
    }
}



