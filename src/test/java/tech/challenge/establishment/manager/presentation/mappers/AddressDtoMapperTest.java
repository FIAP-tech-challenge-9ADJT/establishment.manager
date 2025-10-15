package tech.challenge.establishment.manager.presentation.mappers;

import org.junit.jupiter.api.Test;
import tech.challenge.establishment.manager.domain.entities.Address;
import tech.challenge.establishment.manager.domain.valueobjects.AddressId;
import tech.challenge.establishment.manager.domain.valueobjects.PostalCode;
import tech.challenge.establishment.manager.domain.valueobjects.UserId;
import tech.challenge.establishment.manager.presentation.dtos.address.AddressResponseDTO;
import tech.challenge.establishment.manager.presentation.dtos.address.CreateAddressDTO;
import tech.challenge.establishment.manager.presentation.dtos.address.UpdateAddressDTO;

import static org.assertj.core.api.Assertions.assertThat;

class AddressDtoMapperTest {

    @Test
    void shouldMapFromCreateDtoWithoutUserId() {
        // Given
        CreateAddressDTO dto = new CreateAddressDTO(
                "Test Street",
                "Test City",
                "12345678",
                "100"
        );

        // When
        Address result = AddressDtoMapper.fromCreateDto(dto);

        // Then
        assertThat(result.getStreet()).isEqualTo("Test Street");
        assertThat(result.getCity()).isEqualTo("Test City");
        assertThat(result.getPostalCode().value()).isEqualTo("12345678");
        assertThat(result.getNumber()).isEqualTo("100");
        assertThat(result.getUserId()).isNull();
    }

    @Test
    void shouldMapFromCreateDtoWithUserId() {
        // Given
        CreateAddressDTO dto = new CreateAddressDTO(
                "User Street",
                "User City",
                "87654321",
                "200"
        );
        UserId userId = UserId.of(1L);

        // When
        Address result = AddressDtoMapper.fromCreateDto(dto, userId);

        // Then
        assertThat(result.getStreet()).isEqualTo("User Street");
        assertThat(result.getCity()).isEqualTo("User City");
        assertThat(result.getPostalCode().value()).isEqualTo("87654321");
        assertThat(result.getNumber()).isEqualTo("200");
        assertThat(result.getUserId()).isEqualTo(userId);
    }

    @Test
    void shouldReturnExistingAddressWhenUpdateDtoIsNull() {
        // Given
        Address existingAddress = new Address(
                AddressId.of(1L),
                "Existing Street",
                "Existing City",
                new PostalCode("11111111"),
                "300",
                UserId.of(1L)
        );
        UpdateAddressDTO dto = null;

        // When
        Address result = AddressDtoMapper.fromUpdateDto(dto, existingAddress);

        // Then
        assertThat(result).isSameAs(existingAddress);
    }

    @Test
    void shouldMapFromUpdateDtoWithAllFieldsUpdated() {
        // Given
        Address existingAddress = new Address(
                AddressId.of(1L),
                "Old Street",
                "Old City",
                new PostalCode("11111111"),
                "300",
                UserId.of(1L)
        );
        UpdateAddressDTO dto = new UpdateAddressDTO(
                "New Street",
                "New City",
                "22222222",
                "400"
        );

        // When
        Address result = AddressDtoMapper.fromUpdateDto(dto, existingAddress);

        // Then
        assertThat(result.getId()).isEqualTo(existingAddress.getId());
        assertThat(result.getStreet()).isEqualTo("New Street");
        assertThat(result.getCity()).isEqualTo("New City");
        assertThat(result.getPostalCode().value()).isEqualTo("22222222");
        assertThat(result.getNumber()).isEqualTo("400");
        assertThat(result.getUserId()).isEqualTo(existingAddress.getUserId());
    }

    @Test
    void shouldMapFromUpdateDtoWithPartialFieldsUpdated() {
        // Given
        Address existingAddress = new Address(
                AddressId.of(1L),
                "Old Street",
                "Old City",
                new PostalCode("11111111"),
                "300",
                UserId.of(1L)
        );
        UpdateAddressDTO dto = new UpdateAddressDTO(
                "New Street",
                null, // city não será atualizada
                "22222222",
                null  // number não será atualizado
        );

        // When
        Address result = AddressDtoMapper.fromUpdateDto(dto, existingAddress);

        // Then
        assertThat(result.getId()).isEqualTo(existingAddress.getId());
        assertThat(result.getStreet()).isEqualTo("New Street");
        assertThat(result.getCity()).isEqualTo("Old City"); // manteve o valor anterior
        assertThat(result.getPostalCode().value()).isEqualTo("22222222");
        assertThat(result.getNumber()).isEqualTo("300"); // manteve o valor anterior
        assertThat(result.getUserId()).isEqualTo(existingAddress.getUserId());
    }

    @Test
    void shouldMapFromUpdateDtoWithNoFieldsUpdated() {
        // Given
        Address existingAddress = new Address(
                AddressId.of(1L),
                "Old Street",
                "Old City",
                new PostalCode("11111111"),
                "300",
                UserId.of(1L)
        );
        UpdateAddressDTO dto = new UpdateAddressDTO(null, null, null, null);

        // When
        Address result = AddressDtoMapper.fromUpdateDto(dto, existingAddress);

        // Then
        assertThat(result.getId()).isEqualTo(existingAddress.getId());
        assertThat(result.getStreet()).isEqualTo("Old Street");
        assertThat(result.getCity()).isEqualTo("Old City");
        assertThat(result.getPostalCode().value()).isEqualTo("11111111");
        assertThat(result.getNumber()).isEqualTo("300");
        assertThat(result.getUserId()).isEqualTo(existingAddress.getUserId());
    }

    @Test
    void shouldMapToResponseDtoWithId() {
        // Given
        Address address = new Address(
                AddressId.of(1L),
                "Response Street",
                "Response City",
                new PostalCode("33333333"),
                "500",
                UserId.of(1L)
        );

        // When
        AddressResponseDTO result = AddressDtoMapper.toResponseDto(address);

        // Then
        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.street()).isEqualTo("Response Street");
        assertThat(result.city()).isEqualTo("Response City");
        assertThat(result.postalCode()).isEqualTo("33333333");
        assertThat(result.number()).isEqualTo("500");
    }

    @Test
    void shouldMapToResponseDtoWithoutId() {
        // Given
        Address address = new Address(
                null, // ID nulo
                "Response Street",
                "Response City",
                new PostalCode("33333333"),
                "500",
                UserId.of(1L)
        );

        // When
        AddressResponseDTO result = AddressDtoMapper.toResponseDto(address);

        // Then
        assertThat(result.id()).isNull();
        assertThat(result.street()).isEqualTo("Response Street");
        assertThat(result.city()).isEqualTo("Response City");
        assertThat(result.postalCode()).isEqualTo("33333333");
        assertThat(result.number()).isEqualTo("500");
    }
}

