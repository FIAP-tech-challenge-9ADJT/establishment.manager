package tech.challenge.establishment.manager.presentation.mappers;

import org.junit.jupiter.api.Test;
import tech.challenge.establishment.manager.domain.entities.Restaurant;
import tech.challenge.establishment.manager.domain.entities.RestaurantAddress;
import tech.challenge.establishment.manager.domain.valueobjects.*;
import tech.challenge.establishment.manager.presentation.dtos.restaurant.CreateRestaurantDTO;
import tech.challenge.establishment.manager.presentation.dtos.restaurant.RestaurantResponseDTO;
import tech.challenge.establishment.manager.presentation.dtos.restaurant.UpdateRestaurantDTO;
import tech.challenge.establishment.manager.presentation.dtos.restaurantAddress.CreateRestaurantAddressDTO;
import tech.challenge.establishment.manager.presentation.dtos.restaurantAddress.UpdateRestaurantAddressDTO;

import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

class RestaurantDtoMapperTest {

    @Test
    void shouldMapFromCreateDtoSuccessfully() {
        // Given
        CreateRestaurantAddressDTO addressDto = new CreateRestaurantAddressDTO(
                "Restaurant Street",
                "Restaurant City",
                "12345678",
                "100"
        );
        CreateRestaurantDTO dto = new CreateRestaurantDTO(
                "Test Restaurant",
                addressDto,
                "Italian",
                LocalTime.of(9, 0),
                LocalTime.of(22, 0)
        );
        Long ownerId = 1L;

        // When
        Restaurant restaurant = RestaurantDtoMapper.fromCreateDto(dto, ownerId);

        // Then
        assertThat(restaurant).isNotNull();
        assertThat(restaurant.getName().value()).isEqualTo("Test Restaurant");
        assertThat(restaurant.getKitchenType().value()).isEqualTo("Italian");
        assertThat(restaurant.getStartOperation()).isEqualTo(LocalTime.of(9, 0));
        assertThat(restaurant.getEndOperation()).isEqualTo(LocalTime.of(22, 0));
        assertThat(restaurant.getOwnerId().value()).isEqualTo(ownerId);
        assertThat(restaurant.getRestaurantAddress()).isNotNull();
        assertThat(restaurant.getRestaurantAddress().getStreet()).isEqualTo("Restaurant Street");
    }

    @Test
    void shouldMapFromUpdateDtoSuccessfully() {
        // Given
        RestaurantAddress existingAddress = RestaurantAddress.create(
                "Old Street",
                "Old City",
                "11111111",
                "50",
                RestaurantId.of(1L)
        );
        Restaurant existingRestaurant = Restaurant.of(
                1L,
                "Old Restaurant",
                existingAddress,
                KitchenType.of("Mexican"),
                LocalTime.of(10, 0),
                LocalTime.of(21, 0),
                UserId.of(1L)
        );

        UpdateRestaurantAddressDTO addressDto = new UpdateRestaurantAddressDTO(
                "Updated Street",
                "Updated City",
                "87654321",
                "200"
        );
        UpdateRestaurantDTO dto = new UpdateRestaurantDTO(
                "Updated Restaurant",
                addressDto,
                "Japanese",
                LocalTime.of(8, 0),
                LocalTime.of(23, 0)
        );

        // When
        Restaurant result = RestaurantDtoMapper.fromUpdateDto(dto, existingRestaurant);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId().value()).isEqualTo(1L);
        assertThat(result.getName().value()).isEqualTo("Updated Restaurant");
        assertThat(result.getKitchenType().value()).isEqualTo("Japanese");
        assertThat(result.getStartOperation()).isEqualTo(LocalTime.of(8, 0));
        assertThat(result.getEndOperation()).isEqualTo(LocalTime.of(23, 0));
        assertThat(result.getOwnerId()).isEqualTo(existingRestaurant.getOwnerId());
        assertThat(result.getRestaurantAddress().getStreet()).isEqualTo("Updated Street");
    }

    @Test
    void shouldMapToResponseDtoSuccessfully() {
        // Given
        RestaurantAddress address = RestaurantAddress.create(
                "Response Street",
                "Response City",
                "33333333",
                "300",
                RestaurantId.of(2L)
        );
        Restaurant restaurant = Restaurant.of(
                2L,
                "Response Restaurant",
                address,
                KitchenType.of("Chinese"),
                LocalTime.of(11, 0),
                LocalTime.of(20, 0),
                UserId.of(2L)
        );

        // When
        RestaurantResponseDTO dto = RestaurantDtoMapper.toResponseDto(restaurant);

        // Then
        assertThat(dto).isNotNull();
        assertThat(dto.id()).isEqualTo(2L);
        assertThat(dto.name()).isEqualTo("Response Restaurant");
        assertThat(dto.kitchenType()).isEqualTo("Chinese");
        assertThat(dto.openingTime()).isEqualTo(LocalTime.of(11, 0));
        assertThat(dto.closingTime()).isEqualTo(LocalTime.of(20, 0));
        assertThat(dto.ownerId()).isEqualTo(2L);
        assertThat(dto.restaurantAddress()).isNotNull();
        assertThat(dto.restaurantAddress().street()).isEqualTo("Response Street");
    }

    @Test
    void shouldMapToResponseDtoWithNullId() {
        // Given
        RestaurantAddress address = RestaurantAddress.create(
                "New Restaurant Street",
                "New Restaurant City",
                "44444444",
                "400",
                RestaurantId.of(3L)
        );
        Restaurant restaurant = Restaurant.create(
                "New Restaurant",
                address,
                KitchenType.of("Brazilian"),
                LocalTime.of(12, 0),
                LocalTime.of(19, 0),
                UserId.of(3L)
        );

        // When
        RestaurantResponseDTO dto = RestaurantDtoMapper.toResponseDto(restaurant);

        // Then
        assertThat(dto).isNotNull();
        assertThat(dto.id()).isNull(); // New restaurant has no ID yet
        assertThat(dto.name()).isEqualTo("New Restaurant");
        assertThat(dto.kitchenType()).isEqualTo("Brazilian");
        assertThat(dto.openingTime()).isEqualTo(LocalTime.of(12, 0));
        assertThat(dto.closingTime()).isEqualTo(LocalTime.of(19, 0));
        assertThat(dto.ownerId()).isEqualTo(3L);
    }

    @Test
    void shouldMapFromCreateDtoWithDifferentKitchenType() {
        // Given
        CreateRestaurantAddressDTO addressDto = new CreateRestaurantAddressDTO(
                "French Street",
                "French City",
                "55555555",
                "500"
        );
        CreateRestaurantDTO dto = new CreateRestaurantDTO(
                "French Restaurant",
                addressDto,
                "French",
                LocalTime.of(18, 0),
                LocalTime.of(1, 0)
        );
        Long ownerId = 5L;

        // When
        Restaurant restaurant = RestaurantDtoMapper.fromCreateDto(dto, ownerId);

        // Then
        assertThat(restaurant).isNotNull();
        assertThat(restaurant.getName().value()).isEqualTo("French Restaurant");
        assertThat(restaurant.getKitchenType().value()).isEqualTo("French");
        assertThat(restaurant.getStartOperation()).isEqualTo(LocalTime.of(18, 0));
        assertThat(restaurant.getEndOperation()).isEqualTo(LocalTime.of(1, 0));
        assertThat(restaurant.getOwnerId().value()).isEqualTo(ownerId);
    }

    @Test
    void shouldMapFromUpdateDtoWithPartialUpdates() {
        // Given
        RestaurantAddress existingAddress = RestaurantAddress.create(
                "Existing Street",
                "Existing City",
                "66666666",
                "600",
                RestaurantId.of(6L)
        );
        Restaurant existingRestaurant = Restaurant.of(
                6L,
                "Existing Restaurant",
                existingAddress,
                KitchenType.of("Indian"),
                LocalTime.of(9, 30),
                LocalTime.of(22, 30),
                UserId.of(6L)
        );

        UpdateRestaurantDTO dto = new UpdateRestaurantDTO(
                "Partially Updated Restaurant",
                null, // address not updated
                "Indian",
                LocalTime.of(10, 30),
                LocalTime.of(21, 30)
        );

        // When
        Restaurant result = RestaurantDtoMapper.fromUpdateDto(dto, existingRestaurant);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId().value()).isEqualTo(6L);
        assertThat(result.getName().value()).isEqualTo("Partially Updated Restaurant");
        assertThat(result.getKitchenType().value()).isEqualTo("Indian");
        assertThat(result.getStartOperation()).isEqualTo(LocalTime.of(10, 30));
        assertThat(result.getEndOperation()).isEqualTo(LocalTime.of(21, 30));
        assertThat(result.getOwnerId()).isEqualTo(existingRestaurant.getOwnerId());
        // Address should remain from existing restaurant since it wasn't updated
        assertThat(result.getRestaurantAddress().getStreet()).isEqualTo("Existing Street");
    }
}
