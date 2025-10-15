package tech.challenge.establishment.manager.domain.usecases.restaurant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tech.challenge.establishment.manager.domain.entities.Restaurant;
import tech.challenge.establishment.manager.domain.repositories.RestaurantRepository;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FindRestaurantByNameUseCaseTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    private FindRestaurantByNameUseCase findRestaurantByNameUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        findRestaurantByNameUseCase = new FindRestaurantByNameUseCase(restaurantRepository);
    }

    @Test
    void shouldFindRestaurantsByName() {
        String name = "Pizza Palace";
        Restaurant restaurant1 = mock(Restaurant.class);
        Restaurant restaurant2 = mock(Restaurant.class);
        List<Restaurant> restaurants = Arrays.asList(restaurant1, restaurant2);

        when(restaurantRepository.findByNameContainingIgnoreCase(name)).thenReturn(restaurants);

        List<Restaurant> result = findRestaurantByNameUseCase.execute(name);

        assertEquals(2, result.size());
        assertTrue(result.contains(restaurant1));
        assertTrue(result.contains(restaurant2));
        verify(restaurantRepository).findByNameContainingIgnoreCase(name);
    }

    @Test
    void shouldReturnEmptyListWhenNoRestaurantsFound() {
        String name = "Nonexistent Restaurant";

        when(restaurantRepository.findByNameContainingIgnoreCase(name)).thenReturn(Arrays.asList());

        List<Restaurant> result = findRestaurantByNameUseCase.execute(name);

        assertTrue(result.isEmpty());
        verify(restaurantRepository).findByNameContainingIgnoreCase(name);
    }
}
