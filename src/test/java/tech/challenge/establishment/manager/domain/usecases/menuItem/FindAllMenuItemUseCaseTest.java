package tech.challenge.establishment.manager.domain.usecases.menuItem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tech.challenge.establishment.manager.domain.entities.MenuItem;
import tech.challenge.establishment.manager.domain.repositories.MenuItemRepository;
import tech.challenge.establishment.manager.domain.valueobjects.RestaurantId;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FindAllMenuItemUseCaseTest {

    @Mock
    private MenuItemRepository menuItemRepository;

    private FindAllMenuItemUseCase findAllMenuItemUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        findAllMenuItemUseCase = new FindAllMenuItemUseCase(menuItemRepository);
    }

    @Test
    void shouldFindAllMenuItemsByRestaurant() {
        RestaurantId restaurantId = RestaurantId.of(1L);
        MenuItem menuItem1 = mock(MenuItem.class);
        MenuItem menuItem2 = mock(MenuItem.class);
        List<MenuItem> menuItems = Arrays.asList(menuItem1, menuItem2);

        when(menuItemRepository.findAllByRestaurant(restaurantId)).thenReturn(menuItems);

        List<MenuItem> result = findAllMenuItemUseCase.execute(restaurantId);

        assertEquals(2, result.size());
        assertTrue(result.contains(menuItem1));
        assertTrue(result.contains(menuItem2));
        verify(menuItemRepository).findAllByRestaurant(restaurantId);
    }

    @Test
    void shouldReturnEmptyListWhenNoMenuItemsFound() {
        RestaurantId restaurantId = RestaurantId.of(1L);

        when(menuItemRepository.findAllByRestaurant(restaurantId)).thenReturn(Arrays.asList());

        List<MenuItem> result = findAllMenuItemUseCase.execute(restaurantId);

        assertTrue(result.isEmpty());
        verify(menuItemRepository).findAllByRestaurant(restaurantId);
    }

    @Test
    void shouldFindMenuItemsForDifferentRestaurants() {
        RestaurantId restaurant1 = RestaurantId.of(1L);
        RestaurantId restaurant2 = RestaurantId.of(2L);
        
        MenuItem item1 = mock(MenuItem.class);
        MenuItem item2 = mock(MenuItem.class);
        MenuItem item3 = mock(MenuItem.class);

        when(menuItemRepository.findAllByRestaurant(restaurant1)).thenReturn(Arrays.asList(item1, item2));
        when(menuItemRepository.findAllByRestaurant(restaurant2)).thenReturn(Arrays.asList(item3));

        List<MenuItem> result1 = findAllMenuItemUseCase.execute(restaurant1);
        List<MenuItem> result2 = findAllMenuItemUseCase.execute(restaurant2);

        assertEquals(2, result1.size());
        assertEquals(1, result2.size());
    }

    @Test
    void shouldReturnSingleMenuItem() {
        RestaurantId restaurantId = RestaurantId.of(1L);
        MenuItem menuItem = mock(MenuItem.class);

        when(menuItemRepository.findAllByRestaurant(restaurantId)).thenReturn(Arrays.asList(menuItem));

        List<MenuItem> result = findAllMenuItemUseCase.execute(restaurantId);

        assertEquals(1, result.size());
        assertEquals(menuItem, result.get(0));
    }
}
