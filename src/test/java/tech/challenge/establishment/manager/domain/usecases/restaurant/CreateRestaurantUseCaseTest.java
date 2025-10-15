package tech.challenge.establishment.manager.domain.usecases.restaurant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tech.challenge.establishment.manager.domain.entities.Restaurant;
import tech.challenge.establishment.manager.domain.entities.RestaurantAddress;
import tech.challenge.establishment.manager.domain.exceptions.RestaurantAlreadyExistsException;
import tech.challenge.establishment.manager.domain.repositories.RestaurantRepository;
import tech.challenge.establishment.manager.domain.repositories.UserRepository;
import tech.challenge.establishment.manager.domain.valueobjects.KitchenType;
import tech.challenge.establishment.manager.domain.valueobjects.Name;
import tech.challenge.establishment.manager.domain.valueobjects.UserId;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateRestaurantUseCaseTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @Mock
    private UserRepository userRepository;

    private CreateRestaurantUseCase createRestaurantUseCase;

    @BeforeEach
    void setUp() {
        createRestaurantUseCase = new CreateRestaurantUseCase(restaurantRepository, userRepository);
    }

    @Test
    void shouldCreateRestaurantSuccessfully() {
        String name = "Pizzaria do João";
        RestaurantAddress address = RestaurantAddress.create("Rua das Pizzas", "São Paulo", "01234-567", "100", null);
        KitchenType kitchenType = KitchenType.of("Italian");
        LocalTime startOperation = LocalTime.of(18, 0);
        LocalTime endOperation = LocalTime.of(23, 0);
        UserId ownerId = UserId.of(1L);

        Restaurant expectedRestaurant = Restaurant.create(name, address, kitchenType, startOperation, endOperation, ownerId);

        when(userRepository.existsById(ownerId)).thenReturn(true);
        when(restaurantRepository.existsByNameAndRestaurantAddress(Name.of(name), address)).thenReturn(false);
        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(expectedRestaurant);

        Restaurant result = createRestaurantUseCase.execute(name, address, kitchenType, startOperation, endOperation, ownerId);

        assertNotNull(result);
        assertEquals(name, result.getName().value());
        assertEquals(ownerId, result.getOwnerId());

        verify(userRepository).existsById(ownerId);
        verify(restaurantRepository).existsByNameAndRestaurantAddress(Name.of(name), address);
        verify(restaurantRepository).save(any(Restaurant.class));
    }

    @Test
    void shouldThrowExceptionWhenOwnerNotFound() {
        UserId ownerId = UserId.of(999L);
        
        when(userRepository.existsById(ownerId)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () ->
            createRestaurantUseCase.execute("Restaurant", null, null, null, null, ownerId)
        );

        verify(userRepository).existsById(ownerId);
        verify(restaurantRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenRestaurantAlreadyExists() {
        String name = "Existing Restaurant";
        RestaurantAddress address = RestaurantAddress.create("Rua A", "São Paulo", "01234-567", "100", null);
        UserId ownerId = UserId.of(1L);
        
        when(userRepository.existsById(ownerId)).thenReturn(true);
        when(restaurantRepository.existsByNameAndRestaurantAddress(Name.of(name), address)).thenReturn(true);

        assertThrows(RestaurantAlreadyExistsException.class, () ->
            createRestaurantUseCase.execute(name, address, null, null, null, ownerId)
        );

        verify(restaurantRepository, never()).save(any());
    }

    @Test
    void shouldCreateRestaurantWithDifferentKitchenTypes() {
        UserId ownerId = UserId.of(1L);
        RestaurantAddress address = RestaurantAddress.create("Rua B", "Rio", "12345-678", "200", null);
        
        when(userRepository.existsById(ownerId)).thenReturn(true);
        when(restaurantRepository.existsByNameAndRestaurantAddress(any(), any())).thenReturn(false);
        when(restaurantRepository.save(any(Restaurant.class))).thenAnswer(i -> i.getArgument(0));

        assertDoesNotThrow(() -> createRestaurantUseCase.execute("Restaurant", address, 
            KitchenType.of("Japanese"), LocalTime.of(11, 0), LocalTime.of(22, 0), ownerId));
        
        assertDoesNotThrow(() -> createRestaurantUseCase.execute("Restaurant", address, 
            KitchenType.of("Brazilian"), LocalTime.of(11, 0), LocalTime.of(22, 0), ownerId));
        
        assertDoesNotThrow(() -> createRestaurantUseCase.execute("Restaurant", address, 
            KitchenType.of("Mexican"), LocalTime.of(11, 0), LocalTime.of(22, 0), ownerId));
    }
}
