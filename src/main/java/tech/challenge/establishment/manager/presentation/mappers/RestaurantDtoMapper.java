package tech.challenge.establishment.manager.presentation.mappers;

import tech.challenge.establishment.manager.domain.entities.Restaurant;
import tech.challenge.establishment.manager.domain.valueobjects.KitchenType;
import tech.challenge.establishment.manager.domain.valueobjects.UserId;
import tech.challenge.establishment.manager.presentation.dtos.restaurant.CreateRestaurantDTO;
import tech.challenge.establishment.manager.presentation.dtos.restaurant.RestaurantResponseDTO;
import tech.challenge.establishment.manager.presentation.dtos.restaurant.UpdateRestaurantDTO;

public class RestaurantDtoMapper {

    public static Restaurant fromCreateDto(CreateRestaurantDTO dto, Long ownerId) {
        return Restaurant.create(
                dto.name(),
                RestaurantAddressDtoMapper.fromCreateDto(dto.restaurantAddress()),
                KitchenType.of(dto.kitchenType()),
                dto.startOperation(),
                dto.endOperation(),
                new UserId(ownerId)
        );
    }

    public static Restaurant fromUpdateDto(UpdateRestaurantDTO dto, Restaurant existingRestaurant) {
        return Restaurant.of(
                existingRestaurant.getId().value(),
                dto.name(),
                RestaurantAddressDtoMapper.fromUpdateDto(dto.restaurantAddress(), existingRestaurant.getRestaurantAddress()),
                KitchenType.of(dto.kitchenType()),
                dto.startOperation(),
                dto.endOperation(),
                existingRestaurant.getOwnerId()
        );
    }

    public static RestaurantResponseDTO toResponseDto(Restaurant restaurant) {
        return new RestaurantResponseDTO(
                restaurant.getId() != null ? restaurant.getId().value() : null,
                restaurant.getName().value(),
                RestaurantAddressDtoMapper.toResponseDto(restaurant.getRestaurantAddress()),
                restaurant.getKitchenType().value(),
                restaurant.getStartOperation(),
                restaurant.getEndOperation(),
                restaurant.getOwnerId().value()
        );
    }
}
