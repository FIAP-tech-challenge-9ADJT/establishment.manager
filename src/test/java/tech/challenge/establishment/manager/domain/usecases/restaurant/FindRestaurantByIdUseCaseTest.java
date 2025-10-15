package tech.challenge.establishment.manager.domain.usecases.restaurant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tech.challenge.establishment.manager.domain.entities.Restaurant;
import tech.challenge.establishment.manager.domain.exceptions.RestaurantNotFoundException;
import tech.challenge.establishment.manager.domain.repositories.RestaurantRepository;
import tech.challenge.establishment.manager.domain.valueobjects.RestaurantId;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FindRestaurantByIdUseCaseTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    private FindRestaurantByIdUseCase findRestaurantByIdUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        findRestaurantByIdUseCase = new FindRestaurantByIdUseCase(restaurantRepository);
    }

    @Test
    void shouldFindRestaurantById() {
        RestaurantId restaurantId = RestaurantId.of(1L);
        Restaurant restaurant = mock(Restaurant.class);

        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(restaurant));

        Restaurant result = findRestaurantByIdUseCase.execute(restaurantId);

        assertNotNull(result);
        assertEquals(restaurant, result);
        verify(restaurantRepository).findById(restaurantId);
    }

    @Test
    void shouldThrowExceptionWhenRestaurantNotFound() {
        RestaurantId restaurantId = RestaurantId.of(1L);

        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.empty());

        assertThrows(RestaurantNotFoundException.class, () -> {
            findRestaurantByIdUseCase.execute(restaurantId);
        });

        verify(restaurantRepository).findById(restaurantId);
    }
}
