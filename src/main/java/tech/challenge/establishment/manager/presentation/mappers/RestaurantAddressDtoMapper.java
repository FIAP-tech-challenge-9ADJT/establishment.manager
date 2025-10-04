package tech.challenge.establishment.manager.presentation.mappers;

import tech.challenge.establishment.manager.domain.entities.RestaurantAddress;
import tech.challenge.establishment.manager.domain.valueobjects.PostalCode;
import tech.challenge.establishment.manager.domain.valueobjects.RestaurantId;
import tech.challenge.establishment.manager.presentation.dtos.restaurantAddress.CreateRestaurantAddressDTO;
import tech.challenge.establishment.manager.presentation.dtos.restaurantAddress.RestaurantAddressResponseDTO;
import tech.challenge.establishment.manager.presentation.dtos.restaurantAddress.UpdateRestaurantAddressDTO;

public class RestaurantAddressDtoMapper {

    public static RestaurantAddress fromCreateDto(CreateRestaurantAddressDTO dto) {
        return RestaurantAddress.create(
                dto.street(),
                dto.city(),
                dto.postalCode(),
                dto.number(),
                null // UserId será definido posteriormente
        );
    }

    public static RestaurantAddress fromCreateDto(CreateRestaurantAddressDTO dto, RestaurantId restaurantId) {
        return RestaurantAddress.create(
                dto.street(),
                dto.city(),
                dto.postalCode(),
                dto.number(),
                restaurantId
        );
    }

    public static RestaurantAddress fromUpdateDto(UpdateRestaurantAddressDTO dto, RestaurantAddress existingRestaurantAddress) {
        if (dto == null) return existingRestaurantAddress;

        return new RestaurantAddress(
                existingRestaurantAddress.getId(),
                dto.street() != null && !dto.street().trim().isEmpty() ? dto.street().trim() : existingRestaurantAddress.getStreet(),
                dto.city() != null && !dto.city().trim().isEmpty() ? dto.city().trim() : existingRestaurantAddress.getCity(),
                dto.postalCode() != null && !dto.postalCode().trim().isEmpty() ? new PostalCode(dto.postalCode().trim()) : existingRestaurantAddress.getPostalCode(),
                dto.number() != null && !dto.number().trim().isEmpty() ? dto.number().trim() : existingRestaurantAddress.getNumber(),
                existingRestaurantAddress.getRestaurantId()
        );
    }

    public static RestaurantAddressResponseDTO toResponseDto(RestaurantAddress restaurantAddress) {
        return new RestaurantAddressResponseDTO(
                restaurantAddress.getId() != null ? restaurantAddress.getId().value() : null,
                restaurantAddress.getStreet(),
                restaurantAddress.getCity(),
                restaurantAddress.getPostalCode().value(),
                restaurantAddress.getNumber()
        );
    }
}
