package tech.challenge.establishment.manager.application.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tech.challenge.establishment.manager.application.usecases.restaurant.*;
import tech.challenge.establishment.manager.domain.entities.Restaurant;
import tech.challenge.establishment.manager.domain.entities.RestaurantAddress;
import tech.challenge.establishment.manager.domain.valueobjects.KitchenType;
import tech.challenge.establishment.manager.domain.valueobjects.RestaurantId;
import tech.challenge.establishment.manager.domain.valueobjects.UserId;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RestaurantApplicationServiceTest {

    @Mock
    private CreateRestaurantUseCaseImpl createRestaurantUseCase;
    @Mock
    private UpdateRestaurantUseCaseImpl updateRestaurantUseCase;
    @Mock
    private DeleteRestaurantUseCaseImpl deleteRestaurantUseCase;
    @Mock
    private FindAllRestaurantsUseCaseImpl findAllRestaurantsUseCase;
    @Mock
    private FindRestaurantByIdUseCaseImpl findRestaurantByIdUseCase;
    @Mock
    private FindRestaurantByNameUseCaseImpl findRestaurantByNameUseCase;

    private RestaurantApplicationService restaurantApplicationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        restaurantApplicationService = new RestaurantApplicationService(
                createRestaurantUseCase,
                updateRestaurantUseCase,
                deleteRestaurantUseCase,
                findAllRestaurantsUseCase,
                findRestaurantByIdUseCase,
                findRestaurantByNameUseCase
        );
    }

    @Test
    void shouldCreateRestaurant() {
        String name = "Test Restaurant";
        RestaurantAddress address = RestaurantAddress.create("Street", "City", "12345678", "100", null);
        KitchenType kitchenType = KitchenType.of("Italian");
        LocalTime start = LocalTime.of(10, 0);
        LocalTime end = LocalTime.of(22, 0);
        UserId ownerId = UserId.of(1L);
        Restaurant expectedRestaurant = mock(Restaurant.class);

        when(createRestaurantUseCase.execute(name, address, kitchenType, start, end, ownerId))
                .thenReturn(expectedRestaurant);

        Restaurant result = restaurantApplicationService.createRestaurant(name, address, kitchenType, start, end, ownerId);

        assertEquals(expectedRestaurant, result);
        verify(createRestaurantUseCase).execute(name, address, kitchenType, start, end, ownerId);
    }

    @Test
    void shouldUpdateRestaurant() {
        RestaurantId restaurantId = RestaurantId.of(1L);
        String name = "Updated Restaurant";
        RestaurantAddress address = RestaurantAddress.create("New Street", "New City", "87654321", "200", null);
        LocalTime start = LocalTime.of(11, 0);
        LocalTime end = LocalTime.of(23, 0);
        KitchenType kitchenType = KitchenType.of("Brazilian");
        Restaurant expectedRestaurant = mock(Restaurant.class);

        when(updateRestaurantUseCase.execute(restaurantId, name, address, start, end, kitchenType))
                .thenReturn(expectedRestaurant);

        Restaurant result = restaurantApplicationService.updateRestaurant(restaurantId, name, address, start, end, kitchenType);

        assertEquals(expectedRestaurant, result);
        verify(updateRestaurantUseCase).execute(restaurantId, name, address, start, end, kitchenType);
    }

    @Test
    void shouldDeleteRestaurant() {
        RestaurantId restaurantId = RestaurantId.of(1L);

        restaurantApplicationService.deleteRestaurant(restaurantId);

        verify(deleteRestaurantUseCase).execute(restaurantId);
    }

    @Test
    void shouldGetAllRestaurants() {
        List<Restaurant> expectedRestaurants = Arrays.asList(mock(Restaurant.class), mock(Restaurant.class));

        when(findAllRestaurantsUseCase.execute()).thenReturn(expectedRestaurants);

        List<Restaurant> result = restaurantApplicationService.getAllRestaurants();

        assertEquals(expectedRestaurants, result);
        verify(findAllRestaurantsUseCase).execute();
    }

    @Test
    void shouldGetRestaurantById() {
        RestaurantId restaurantId = RestaurantId.of(1L);
        Restaurant expectedRestaurant = mock(Restaurant.class);

        when(findRestaurantByIdUseCase.execute(restaurantId)).thenReturn(expectedRestaurant);

        Restaurant result = restaurantApplicationService.getRestaurantById(restaurantId);

        assertEquals(expectedRestaurant, result);
        verify(findRestaurantByIdUseCase).execute(restaurantId);
    }

    @Test
    void shouldGetRestaurantsByName() {
        String name = "Test Restaurant";
        List<Restaurant> expectedRestaurants = Arrays.asList(mock(Restaurant.class));

        when(findRestaurantByNameUseCase.execute(name)).thenReturn(expectedRestaurants);

        List<Restaurant> result = restaurantApplicationService.getRestaurantsByName(name);

        assertEquals(expectedRestaurants, result);
        verify(findRestaurantByNameUseCase).execute(name);
    }
}