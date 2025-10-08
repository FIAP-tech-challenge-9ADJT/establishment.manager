package tech.challenge.establishment.manager.presentation.dtos.menuItem;

public record MenuItemResponseDTO(
        Long id,
        String name,
        String description,
        double price,
        boolean onlyInRestaurant,
        String photoPath,
        Long restaurantId
) {}
