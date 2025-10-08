package tech.challenge.establishment.manager.presentation.dtos.menuItem;

public record MenuItemResponseDTO(
        Long id,
        String name,
        String description,
        Double price,
        String photoUrl,
        Long restaurantId
) {}
