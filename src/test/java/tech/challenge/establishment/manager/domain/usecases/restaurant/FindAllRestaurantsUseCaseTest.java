package tech.challenge.establishment.manager.domain.usecases.restaurant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tech.challenge.establishment.manager.domain.entities.Restaurant;
import tech.challenge.establishment.manager.domain.repositories.RestaurantRepository;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindAllRestaurantsUseCaseTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private Restaurant restaurant1;

    @Mock
    private Restaurant restaurant2;

    private FindAllRestaurantsUseCase findAllRestaurantsUseCase;

    @BeforeEach
    void setUp() {
        findAllRestaurantsUseCase = new FindAllRestaurantsUseCase(restaurantRepository);
    }

    @Test
    void shouldReturnAllRestaurants() {
        List<Restaurant> expectedRestaurants = Arrays.asList(restaurant1, restaurant2);

        when(restaurantRepository.findAll()).thenReturn(expectedRestaurants);

        List<Restaurant> result = findAllRestaurantsUseCase.execute();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(expectedRestaurants, result);

        verify(restaurantRepository).findAll();
    }

    @Test
    void shouldReturnEmptyListWhenNoRestaurants() {
        List<Restaurant> expectedRestaurants = Arrays.asList();

        when(restaurantRepository.findAll()).thenReturn(expectedRestaurants);

        List<Restaurant> result = findAllRestaurantsUseCase.execute();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(restaurantRepository).findAll();
    }
}

