package tech.challenge.establishment.manager.application.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tech.challenge.establishment.manager.application.usecases.menuItem.*;
import tech.challenge.establishment.manager.domain.entities.MenuItem;
import tech.challenge.establishment.manager.domain.valueobjects.MenuItemId;
import tech.challenge.establishment.manager.domain.valueobjects.RestaurantId;
import tech.challenge.establishment.manager.domain.valueobjects.UserId;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MenuItemApplicationServiceTest {

    @Mock
    private CreateMenuItemUseCaseImpl createMenuItemUseCase;
    @Mock
    private UpdateMenuItemUseCaseImpl updateMenuItemUseCase;
    @Mock
    private DeleteMenuItemUseCaseImpl deleteMenuItemUseCase;
    @Mock
    private FindMenuItemByIdUseCaseImpl findMenuItemByIdUseCase;
    @Mock
    private FindAllMenuItemUseCaseImpl findAllMenuItemUseCase;

    private MenuItemApplicationService menuItemApplicationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        menuItemApplicationService = new MenuItemApplicationService(
                createMenuItemUseCase,
                updateMenuItemUseCase,
                deleteMenuItemUseCase,
                findMenuItemByIdUseCase,
                findAllMenuItemUseCase
        );
    }

    @Test
    void shouldCreateMenuItem() {
        String name = "Pizza Margherita";
        String description = "Traditional Italian pizza";
        Double price = 25.99;
        String photoUrl = "http://example.com/pizza.jpg";
        RestaurantId restaurantId = RestaurantId.of(1L);
        UserId currentUserId = UserId.of(1L);
        boolean isAdmin = false;
        MenuItem expectedMenuItem = mock(MenuItem.class);

        when(createMenuItemUseCase.execute(name, description, price, photoUrl, restaurantId, currentUserId, isAdmin))
                .thenReturn(expectedMenuItem);

        MenuItem result = menuItemApplicationService.createMenuItem(name, description, price, photoUrl, restaurantId, currentUserId, isAdmin);

        assertEquals(expectedMenuItem, result);
        verify(createMenuItemUseCase).execute(name, description, price, photoUrl, restaurantId, currentUserId, isAdmin);
    }

    @Test
    void shouldUpdateMenuItem() {
        MenuItemId menuItemId = MenuItemId.of(1L);
        String name = "Updated Pizza";
        String description = "Updated description";
        Double price = 30.99;
        String photoUrl = "http://example.com/updated-pizza.jpg";
        RestaurantId restaurantId = RestaurantId.of(1L);
        UserId currentUserId = UserId.of(1L);
        boolean isAdmin = true;
        MenuItem expectedMenuItem = mock(MenuItem.class);

        when(updateMenuItemUseCase.execute(menuItemId, name, description, price, photoUrl, restaurantId, currentUserId, isAdmin))
                .thenReturn(expectedMenuItem);

        MenuItem result = menuItemApplicationService.updateMenuItem(menuItemId, name, description, price, photoUrl, restaurantId, currentUserId, isAdmin);

        assertEquals(expectedMenuItem, result);
        verify(updateMenuItemUseCase).execute(menuItemId, name, description, price, photoUrl, restaurantId, currentUserId, isAdmin);
    }

    @Test
    void shouldDeleteMenuItem() {
        MenuItemId menuItemId = MenuItemId.of(1L);
        UserId currentUserId = UserId.of(1L);
        boolean isAdmin = true;

        menuItemApplicationService.deleteMenuItem(menuItemId, currentUserId, isAdmin);

        verify(deleteMenuItemUseCase).execute(menuItemId, currentUserId, isAdmin);
    }

    @Test
    void shouldGetMenuItemById() {
        MenuItemId menuItemId = MenuItemId.of(1L);
        MenuItem expectedMenuItem = mock(MenuItem.class);

        when(findMenuItemByIdUseCase.execute(menuItemId)).thenReturn(expectedMenuItem);

        MenuItem result = menuItemApplicationService.getMenuItemById(menuItemId);

        assertEquals(expectedMenuItem, result);
        verify(findMenuItemByIdUseCase).execute(menuItemId);
    }

    @Test
    void shouldGetAllMenuItems() {
        RestaurantId restaurantId = RestaurantId.of(1L);
        List<MenuItem> expectedMenuItems = Arrays.asList(mock(MenuItem.class), mock(MenuItem.class));

        when(findAllMenuItemUseCase.execute(restaurantId)).thenReturn(expectedMenuItems);

        List<MenuItem> result = menuItemApplicationService.getAllMenuItems(restaurantId);

        assertEquals(expectedMenuItems, result);
        verify(findAllMenuItemUseCase).execute(restaurantId);
    }
}


