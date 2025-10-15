package tech.challenge.establishment.manager.domain.usecases.restaurant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tech.challenge.establishment.manager.domain.exceptions.RestaurantNotFoundException;
import tech.challenge.establishment.manager.domain.repositories.RestaurantRepository;
import tech.challenge.establishment.manager.domain.valueobjects.RestaurantId;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DeleteRestaurantUseCaseTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    private DeleteRestaurantUseCase deleteRestaurantUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        deleteRestaurantUseCase = new DeleteRestaurantUseCase(restaurantRepository);
    }

    @Test
    void shouldDeleteRestaurantWhenExists() {
        RestaurantId restaurantId = RestaurantId.of(1L);

        when(restaurantRepository.existsById(restaurantId)).thenReturn(true);

        assertDoesNotThrow(() -> deleteRestaurantUseCase.execute(restaurantId));

        verify(restaurantRepository).existsById(restaurantId);
        verify(restaurantRepository).deleteById(restaurantId);
    }

    @Test
    void shouldThrowExceptionWhenRestaurantNotExists() {
        RestaurantId restaurantId = RestaurantId.of(1L);

        when(restaurantRepository.existsById(restaurantId)).thenReturn(false);

        assertThrows(RestaurantNotFoundException.class, () -> {
            deleteRestaurantUseCase.execute(restaurantId);
        });

        verify(restaurantRepository).existsById(restaurantId);
        verify(restaurantRepository, never()).deleteById(restaurantId);
    }

    @Test
    void shouldDeleteMultipleRestaurants() {
        RestaurantId id1 = RestaurantId.of(1L);
        RestaurantId id2 = RestaurantId.of(2L);
        RestaurantId id3 = RestaurantId.of(3L);

        when(restaurantRepository.existsById(id1)).thenReturn(true);
        when(restaurantRepository.existsById(id2)).thenReturn(true);
        when(restaurantRepository.existsById(id3)).thenReturn(true);

        assertDoesNotThrow(() -> deleteRestaurantUseCase.execute(id1));
        assertDoesNotThrow(() -> deleteRestaurantUseCase.execute(id2));
        assertDoesNotThrow(() -> deleteRestaurantUseCase.execute(id3));

        verify(restaurantRepository).deleteById(id1);
        verify(restaurantRepository).deleteById(id2);
        verify(restaurantRepository).deleteById(id3);
    }
}
