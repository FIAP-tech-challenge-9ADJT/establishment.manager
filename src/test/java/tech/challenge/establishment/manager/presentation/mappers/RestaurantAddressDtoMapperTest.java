package tech.challenge.establishment.manager.presentation.mappers;

import org.junit.jupiter.api.Test;
import tech.challenge.establishment.manager.domain.entities.RestaurantAddress;
import tech.challenge.establishment.manager.domain.valueobjects.PostalCode;
import tech.challenge.establishment.manager.domain.valueobjects.RestaurantAddressId;
import tech.challenge.establishment.manager.domain.valueobjects.RestaurantId;
import tech.challenge.establishment.manager.presentation.dtos.restaurantAddress.CreateRestaurantAddressDTO;
import tech.challenge.establishment.manager.presentation.dtos.restaurantAddress.RestaurantAddressResponseDTO;
import tech.challenge.establishment.manager.presentation.dtos.restaurantAddress.UpdateRestaurantAddressDTO;

import static org.assertj.core.api.Assertions.assertThat;

class RestaurantAddressDtoMapperTest {

    @Test
    void shouldMapFromCreateDtoWithoutRestaurantId() {
        // Given
        CreateRestaurantAddressDTO dto = new CreateRestaurantAddressDTO(
                "Test Street",
                "Test City",
                "12345678",
                "100"
        );

        // When
        RestaurantAddress address = RestaurantAddressDtoMapper.fromCreateDto(dto);

        // Then
        assertThat(address).isNotNull();
        assertThat(address.getStreet()).isEqualTo("Test Street");
        assertThat(address.getCity()).isEqualTo("Test City");
        assertThat(address.getPostalCode().value()).isEqualTo("12345678");
        assertThat(address.getNumber()).isEqualTo("100");
        assertThat(address.getRestaurantId()).isNull();
    }

    @Test
    void shouldMapFromCreateDtoWithRestaurantId() {
        // Given
        CreateRestaurantAddressDTO dto = new CreateRestaurantAddressDTO(
                "Restaurant Street",
                "Restaurant City",
                "87654321",
                "200"
        );
        RestaurantId restaurantId = RestaurantId.of(1L);

        // When
        RestaurantAddress address = RestaurantAddressDtoMapper.fromCreateDto(dto, restaurantId);

        // Then
        assertThat(address).isNotNull();
        assertThat(address.getStreet()).isEqualTo("Restaurant Street");
        assertThat(address.getCity()).isEqualTo("Restaurant City");
        assertThat(address.getPostalCode().value()).isEqualTo("87654321");
        assertThat(address.getNumber()).isEqualTo("200");
        assertThat(address.getRestaurantId()).isEqualTo(restaurantId);
    }

    @Test
    void shouldMapFromUpdateDtoWithAllFieldsUpdated() {
        // Given
        RestaurantAddress existingAddress = new RestaurantAddress(
                RestaurantAddressId.of(1L),
                "Old Street",
                "Old City",
                new PostalCode("11111111"),
                "50",
                RestaurantId.of(1L)
        );

        UpdateRestaurantAddressDTO dto = new UpdateRestaurantAddressDTO(
                "New Street",
                "New City",
                "22222222",
                "150"
        );

        // When
        RestaurantAddress result = RestaurantAddressDtoMapper.fromUpdateDto(dto, existingAddress);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(existingAddress.getId());
        assertThat(result.getStreet()).isEqualTo("New Street");
        assertThat(result.getCity()).isEqualTo("New City");
        assertThat(result.getPostalCode().value()).isEqualTo("22222222");
        assertThat(result.getNumber()).isEqualTo("150");
        assertThat(result.getRestaurantId()).isEqualTo(existingAddress.getRestaurantId());
    }

    @Test
    void shouldMapFromUpdateDtoWithPartialUpdates() {
        // Given
        RestaurantAddress existingAddress = new RestaurantAddress(
                RestaurantAddressId.of(2L),
                "Existing Street",
                "Existing City",
                new PostalCode("33333333"),
                "75",
                RestaurantId.of(2L)
        );

        UpdateRestaurantAddressDTO dto = new UpdateRestaurantAddressDTO(
                "Updated Street",
                null, // city not updated
                null, // postal code not updated
                "175"
        );

        // When
        RestaurantAddress result = RestaurantAddressDtoMapper.fromUpdateDto(dto, existingAddress);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(existingAddress.getId());
        assertThat(result.getStreet()).isEqualTo("Updated Street");
        assertThat(result.getCity()).isEqualTo("Existing City"); // unchanged
        assertThat(result.getPostalCode().value()).isEqualTo("33333333"); // unchanged
        assertThat(result.getNumber()).isEqualTo("175");
        assertThat(result.getRestaurantId()).isEqualTo(existingAddress.getRestaurantId());
    }

    @Test
    void shouldMapFromUpdateDtoWithEmptyStrings() {
        // Given
        RestaurantAddress existingAddress = new RestaurantAddress(
                RestaurantAddressId.of(3L),
                "Original Street",
                "Original City",
                new PostalCode("44444444"),
                "25",
                RestaurantId.of(3L)
        );

        UpdateRestaurantAddressDTO dto = new UpdateRestaurantAddressDTO(
                "", // empty string should keep original
                "   ", // whitespace only should keep original
                null, // null should keep original
                "125"
        );

        // When
        RestaurantAddress result = RestaurantAddressDtoMapper.fromUpdateDto(dto, existingAddress);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getStreet()).isEqualTo("Original Street"); // empty string kept original
        assertThat(result.getCity()).isEqualTo("Original City"); // whitespace only kept original
        assertThat(result.getPostalCode().value()).isEqualTo("44444444"); // null kept original
        assertThat(result.getNumber()).isEqualTo("125");
    }

    @Test
    void shouldMapFromUpdateDtoWithWhitespaceHandling() {
        // Given
        RestaurantAddress existingAddress = new RestaurantAddress(
                RestaurantAddressId.of(4L),
                "Trimmed Street",
                "Trimmed City",
                new PostalCode("55555555"),
                "300",
                RestaurantId.of(4L)
        );

        UpdateRestaurantAddressDTO dto = new UpdateRestaurantAddressDTO(
                "  New Street  ", // should be trimmed
                "  New City  ", // should be trimmed
                "  66666666  ", // should be trimmed
                "  400  " // should be trimmed
        );

        // When
        RestaurantAddress result = RestaurantAddressDtoMapper.fromUpdateDto(dto, existingAddress);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getStreet()).isEqualTo("New Street");
        assertThat(result.getCity()).isEqualTo("New City");
        assertThat(result.getPostalCode().value()).isEqualTo("66666666");
        assertThat(result.getNumber()).isEqualTo("400");
    }

    @Test
    void shouldReturnExistingAddressWhenDtoIsNull() {
        // Given
        RestaurantAddress existingAddress = new RestaurantAddress(
                RestaurantAddressId.of(5L),
                "Unchanged Street",
                "Unchanged City",
                new PostalCode("77777777"),
                "500",
                RestaurantId.of(5L)
        );

        // When
        RestaurantAddress result = RestaurantAddressDtoMapper.fromUpdateDto(null, existingAddress);

        // Then
        assertThat(result).isSameAs(existingAddress);
    }

    @Test
    void shouldMapToResponseDtoSuccessfully() {
        // Given
        RestaurantAddress address = new RestaurantAddress(
                RestaurantAddressId.of(6L),
                "Response Street",
                "Response City",
                new PostalCode("88888888"),
                "600",
                RestaurantId.of(6L)
        );

        // When
        RestaurantAddressResponseDTO dto = RestaurantAddressDtoMapper.toResponseDto(address);

        // Then
        assertThat(dto).isNotNull();
        assertThat(dto.id()).isEqualTo(6L);
        assertThat(dto.street()).isEqualTo("Response Street");
        assertThat(dto.city()).isEqualTo("Response City");
        assertThat(dto.postalCode()).isEqualTo("88888888");
        assertThat(dto.number()).isEqualTo("600");
    }

    @Test
    void shouldMapToResponseDtoWithNullId() {
        // Given
        RestaurantAddress address = RestaurantAddress.create(
                "New Response Street",
                "New Response City",
                "99999999",
                "700",
                RestaurantId.of(7L)
        );

        // When
        RestaurantAddressResponseDTO dto = RestaurantAddressDtoMapper.toResponseDto(address);

        // Then
        assertThat(dto).isNotNull();
        assertThat(dto.id()).isNull(); // New address has no ID yet
        assertThat(dto.street()).isEqualTo("New Response Street");
        assertThat(dto.city()).isEqualTo("New Response City");
        assertThat(dto.postalCode()).isEqualTo("99999999");
        assertThat(dto.number()).isEqualTo("700");
    }
}

