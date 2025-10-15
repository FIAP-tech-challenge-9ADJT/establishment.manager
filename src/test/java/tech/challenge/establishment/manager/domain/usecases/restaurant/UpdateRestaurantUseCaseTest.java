package tech.challenge.establishment.manager.domain.usecases.restaurant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tech.challenge.establishment.manager.domain.entities.Restaurant;
import tech.challenge.establishment.manager.domain.entities.RestaurantAddress;
import tech.challenge.establishment.manager.domain.exceptions.RestaurantAlreadyExistsException;
import tech.challenge.establishment.manager.domain.exceptions.RestaurantNotFoundException;
import tech.challenge.establishment.manager.domain.repositories.RestaurantRepository;
import tech.challenge.establishment.manager.domain.valueobjects.*;

import java.time.LocalTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateRestaurantUseCaseTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    private UpdateRestaurantUseCase useCase;

    private Restaurant existingRestaurant;
    private RestaurantAddress existingAddress;

    @BeforeEach
    void setUp() {
        useCase = new UpdateRestaurantUseCase(restaurantRepository);
        
        existingAddress = RestaurantAddress.create(
                "Original Street",
                "Original City",
                "12345678",
                "100",
                RestaurantId.of(1L)
        );
        
        existingRestaurant = Restaurant.create(
                "Original Restaurant",
                existingAddress,
                KitchenType.of("Italian"),
                LocalTime.of(9, 0),
                LocalTime.of(22, 0),
                UserId.of(1L)
        );
        existingRestaurant = Restaurant.of(
                1L,
                existingRestaurant.getName().value(),
                existingRestaurant.getRestaurantAddress(),
                existingRestaurant.getKitchenType(),
                existingRestaurant.getStartOperation(),
                existingRestaurant.getEndOperation(),
                existingRestaurant.getOwnerId()
        );
    }

    @Test
    void shouldUpdateRestaurantNameSuccessfully() {
        // Given
        RestaurantId restaurantId = RestaurantId.of(1L);
        String newName = "Updated Restaurant";
        
        Restaurant updatedRestaurant = existingRestaurant.updateName(newName);

        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(existingRestaurant));
        when(restaurantRepository.findByNameAndRestaurantAddress(any(Name.class), any(RestaurantAddress.class)))
                .thenReturn(Optional.empty());
        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(updatedRestaurant);

        // When
        Restaurant result = useCase.execute(
                restaurantId,
                newName,
                null, // address unchanged
                null, // start time unchanged
                null, // end time unchanged
                null  // kitchen type unchanged
        );

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getName().value()).isEqualTo(newName);
        verify(restaurantRepository).save(any(Restaurant.class));
    }

    @Test
    void shouldUpdateRestaurantAddressSuccessfully() {
        // Given
        RestaurantId restaurantId = RestaurantId.of(1L);
        RestaurantAddress newAddress = RestaurantAddress.create(
                "New Street",
                "New City",
                "87654321",
                "200",
                RestaurantId.of(1L)
        );
        
        Restaurant updatedRestaurant = existingRestaurant.updateRestaurantAddress(newAddress);

        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(existingRestaurant));
        when(restaurantRepository.findByNameAndRestaurantAddress(any(Name.class), eq(newAddress)))
                .thenReturn(Optional.empty());
        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(updatedRestaurant);

        // When
        Restaurant result = useCase.execute(
                restaurantId,
                null, // name unchanged
                newAddress,
                null, // start time unchanged
                null, // end time unchanged
                null  // kitchen type unchanged
        );

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getRestaurantAddress().getStreet()).isEqualTo("New Street");
        assertThat(result.getRestaurantAddress().getCity()).isEqualTo("New City");
        verify(restaurantRepository).save(any(Restaurant.class));
    }

    @Test
    void shouldUpdateOperatingHoursSuccessfully() {
        // Given
        RestaurantId restaurantId = RestaurantId.of(1L);
        LocalTime newStartTime = LocalTime.of(8, 0);
        LocalTime newEndTime = LocalTime.of(23, 0);
        
        Restaurant updatedRestaurant = existingRestaurant.updateOperatingHours(newStartTime, newEndTime);

        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(existingRestaurant));
        when(restaurantRepository.findByNameAndRestaurantAddress(any(Name.class), any(RestaurantAddress.class)))
                .thenReturn(Optional.empty());
        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(updatedRestaurant);

        // When
        Restaurant result = useCase.execute(
                restaurantId,
                null, // name unchanged
                null, // address unchanged
                newStartTime,
                newEndTime,
                null  // kitchen type unchanged
        );

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getStartOperation()).isEqualTo(newStartTime);
        assertThat(result.getEndOperation()).isEqualTo(newEndTime);
        verify(restaurantRepository).save(any(Restaurant.class));
    }

    @Test
    void shouldUpdateKitchenTypeSuccessfully() {
        // Given
        RestaurantId restaurantId = RestaurantId.of(1L);
        KitchenType newKitchenType = KitchenType.of("Japanese");
        
        Restaurant updatedRestaurant = existingRestaurant.updateKitchenType(newKitchenType);

        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(existingRestaurant));
        when(restaurantRepository.findByNameAndRestaurantAddress(any(Name.class), any(RestaurantAddress.class)))
                .thenReturn(Optional.empty());
        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(updatedRestaurant);

        // When
        Restaurant result = useCase.execute(
                restaurantId,
                null, // name unchanged
                null, // address unchanged
                null, // start time unchanged
                null, // end time unchanged
                newKitchenType
        );

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getKitchenType().value()).isEqualTo("Japanese");
        verify(restaurantRepository).save(any(Restaurant.class));
    }

    @Test
    void shouldUpdateAllFieldsSuccessfully() {
        // Given
        RestaurantId restaurantId = RestaurantId.of(1L);
        String newName = "Completely Updated Restaurant";
        RestaurantAddress newAddress = RestaurantAddress.create(
                "Completely New Street",
                "Completely New City",
                "99999999",
                "999",
                RestaurantId.of(1L)
        );
        LocalTime newStartTime = LocalTime.of(7, 0);
        LocalTime newEndTime = LocalTime.of(0, 0);
        KitchenType newKitchenType = KitchenType.of("Chinese");

        Restaurant updatedRestaurant = existingRestaurant
                .updateName(newName)
                .updateRestaurantAddress(newAddress)
                .updateOperatingHours(newStartTime, newEndTime)
                .updateKitchenType(newKitchenType);

        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(existingRestaurant));
        when(restaurantRepository.findByNameAndRestaurantAddress(any(Name.class), eq(newAddress)))
                .thenReturn(Optional.empty());
        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(updatedRestaurant);

        // When
        Restaurant result = useCase.execute(
                restaurantId,
                newName,
                newAddress,
                newStartTime,
                newEndTime,
                newKitchenType
        );

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getName().value()).isEqualTo(newName);
        assertThat(result.getRestaurantAddress().getStreet()).isEqualTo("Completely New Street");
        assertThat(result.getStartOperation()).isEqualTo(newStartTime);
        assertThat(result.getEndOperation()).isEqualTo(newEndTime);
        assertThat(result.getKitchenType().value()).isEqualTo("Chinese");
        verify(restaurantRepository).save(any(Restaurant.class));
    }

    @Test
    void shouldThrowExceptionWhenRestaurantNotFound() {
        // Given
        RestaurantId restaurantId = RestaurantId.of(999L);
        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> useCase.execute(
                restaurantId,
                "New Name",
                null,
                null,
                null,
                null
        )).isInstanceOf(RestaurantNotFoundException.class);

        verify(restaurantRepository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionWhenRestaurantAlreadyExistsWithSameNameAndAddress() {
        // Given
        RestaurantId restaurantId = RestaurantId.of(1L);
        String conflictingName = "Conflicting Restaurant";
        RestaurantAddress conflictingAddress = RestaurantAddress.create(
                "Conflicting Street",
                "Conflicting City",
                "11111111",
                "111",
                RestaurantId.of(2L) // Different restaurant ID
        );
        
        Restaurant conflictingRestaurant = Restaurant.of(
                2L, // Different ID
                conflictingName,
                conflictingAddress,
                KitchenType.of("Mexican"),
                LocalTime.of(10, 0),
                LocalTime.of(21, 0),
                UserId.of(2L)
        );

        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(existingRestaurant));
        when(restaurantRepository.findByNameAndRestaurantAddress(Name.of(conflictingName), conflictingAddress))
                .thenReturn(Optional.of(conflictingRestaurant));

        // When & Then
        assertThatThrownBy(() -> useCase.execute(
                restaurantId,
                conflictingName,
                conflictingAddress,
                null,
                null,
                null
        )).isInstanceOf(RestaurantAlreadyExistsException.class);

        verify(restaurantRepository, never()).save(any());
    }

    @Test
    void shouldAllowUpdatingToSameNameAndAddressForSameRestaurant() {
        // Given
        RestaurantId restaurantId = RestaurantId.of(1L);
        String sameName = existingRestaurant.getName().value();
        RestaurantAddress sameAddress = existingRestaurant.getRestaurantAddress();

        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(existingRestaurant));
        when(restaurantRepository.findByNameAndRestaurantAddress(Name.of(sameName), sameAddress))
                .thenReturn(Optional.of(existingRestaurant)); // Same restaurant
        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(existingRestaurant);

        // When
        Restaurant result = useCase.execute(
                restaurantId,
                sameName,
                sameAddress,
                null,
                null,
                null
        );

        // Then
        assertThat(result).isNotNull();
        verify(restaurantRepository).save(any(Restaurant.class));
    }

    @Test
    void shouldNotUpdateWhenNoChangesProvided() {
        // Given
        RestaurantId restaurantId = RestaurantId.of(1L);

        when(restaurantRepository.findById(restaurantId)).thenReturn(Optional.of(existingRestaurant));
        when(restaurantRepository.findByNameAndRestaurantAddress(any(Name.class), any(RestaurantAddress.class)))
                .thenReturn(Optional.empty());
        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(existingRestaurant);

        // When - no changes provided
        Restaurant result = useCase.execute(
                restaurantId,
                null, // no name change
                null, // no address change
                null, // no start time change
                null, // no end time change
                null  // no kitchen type change
        );

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getName().value()).isEqualTo("Original Restaurant");
        verify(restaurantRepository).save(any(Restaurant.class));
    }
}