package tech.challenge.establishment.manager.application.usecases.restaurant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tech.challenge.establishment.manager.domain.entities.Restaurant;
import tech.challenge.establishment.manager.domain.entities.RestaurantAddress;
import tech.challenge.establishment.manager.domain.repositories.RestaurantRepository;
import tech.challenge.establishment.manager.domain.repositories.UserRepository;
import tech.challenge.establishment.manager.domain.valueobjects.KitchenType;
import tech.challenge.establishment.manager.domain.valueobjects.Name;
import tech.challenge.establishment.manager.domain.valueobjects.RestaurantId;
import tech.challenge.establishment.manager.domain.valueobjects.UserId;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RestaurantUseCaseImplTests {

    @Mock
    private RestaurantRepository restaurantRepository;
    
    @Mock
    private UserRepository userRepository;

    private CreateRestaurantUseCaseImpl createRestaurantUseCaseImpl;
    private FindAllRestaurantsUseCaseImpl findAllRestaurantsUseCaseImpl;
    private FindRestaurantByIdUseCaseImpl findRestaurantByIdUseCaseImpl;
    private FindRestaurantByNameUseCaseImpl findRestaurantByNameUseCaseImpl;
    private UpdateRestaurantUseCaseImpl updateRestaurantUseCaseImpl;
    private DeleteRestaurantUseCaseImpl deleteRestaurantUseCaseImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        createRestaurantUseCaseImpl = new CreateRestaurantUseCaseImpl(restaurantRepository, userRepository);
        findAllRestaurantsUseCaseImpl = new FindAllRestaurantsUseCaseImpl(restaurantRepository);
        findRestaurantByIdUseCaseImpl = new FindRestaurantByIdUseCaseImpl(restaurantRepository);
        findRestaurantByNameUseCaseImpl = new FindRestaurantByNameUseCaseImpl(restaurantRepository);
        updateRestaurantUseCaseImpl = new UpdateRestaurantUseCaseImpl(restaurantRepository);
        deleteRestaurantUseCaseImpl = new DeleteRestaurantUseCaseImpl(restaurantRepository);
    }

    @Test
    void shouldCreateRestaurant() {
        RestaurantAddress address = RestaurantAddress.create("Rua A", "São Paulo", "01234567", "100", null);
        UserId ownerId = UserId.of(1L);

        when(userRepository.existsById(ownerId)).thenReturn(true);
        when(restaurantRepository.existsByNameAndRestaurantAddress(any(), any())).thenReturn(false);
        when(restaurantRepository.save(any(Restaurant.class))).thenAnswer(i -> i.getArgument(0));

        Restaurant result = createRestaurantUseCaseImpl.execute("Restaurant", address, 
            KitchenType.of("Italian"), LocalTime.of(11, 0), LocalTime.of(22, 0), ownerId);

        assertNotNull(result);
        verify(restaurantRepository).save(any(Restaurant.class));
    }

    @Test
    void shouldFindAllRestaurants() {
        Restaurant restaurant = mock(Restaurant.class);
        when(restaurantRepository.findAll()).thenReturn(Arrays.asList(restaurant));

        List<Restaurant> result = findAllRestaurantsUseCaseImpl.execute();

        assertEquals(1, result.size());
        verify(restaurantRepository).findAll();
    }

    @Test
    void shouldFindRestaurantById() {
        RestaurantId id = RestaurantId.of(1L);
        Restaurant restaurant = mock(Restaurant.class);

        when(restaurantRepository.findById(id)).thenReturn(Optional.of(restaurant));

        Restaurant result = findRestaurantByIdUseCaseImpl.execute(id);

        assertNotNull(result);
        verify(restaurantRepository).findById(id);
    }

    @Test
    void shouldFindRestaurantByName() {
        String name = "Pizza";
        Restaurant restaurant = mock(Restaurant.class);

        when(restaurantRepository.findByNameContainingIgnoreCase(name)).thenReturn(Arrays.asList(restaurant));

        List<Restaurant> result = findRestaurantByNameUseCaseImpl.execute(name);

        assertEquals(1, result.size());
        verify(restaurantRepository).findByNameContainingIgnoreCase(name);
    }

    @Test
    void shouldUpdateRestaurant() {
        RestaurantId id = RestaurantId.of(1L);
        RestaurantAddress address = RestaurantAddress.create("Rua B", "Rio", "12345678", "200", null);
        Restaurant existing = Restaurant.of(1L, "Old", address, KitchenType.of("Italian"), 
            LocalTime.of(11, 0), LocalTime.of(22, 0), UserId.of(1L));

        when(restaurantRepository.findById(id)).thenReturn(Optional.of(existing));
        when(restaurantRepository.findByNameAndRestaurantAddress(any(), any())).thenReturn(Optional.empty());
        when(restaurantRepository.save(any(Restaurant.class))).thenAnswer(i -> i.getArgument(0));

        Restaurant result = updateRestaurantUseCaseImpl.execute(id, "New", address, 
            LocalTime.of(12, 0), LocalTime.of(23, 0), KitchenType.of("Japanese"));

        assertNotNull(result);
        verify(restaurantRepository).save(any(Restaurant.class));
    }

    @Test
    void shouldDeleteRestaurant() {
        RestaurantId id = RestaurantId.of(1L);

        when(restaurantRepository.existsById(id)).thenReturn(true);

        assertDoesNotThrow(() -> deleteRestaurantUseCaseImpl.execute(id));

        verify(restaurantRepository).deleteById(id);
    }
}



