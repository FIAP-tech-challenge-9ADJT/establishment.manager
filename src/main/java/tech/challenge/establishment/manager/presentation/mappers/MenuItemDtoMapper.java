package tech.challenge.establishment.manager.presentation.mappers;

import tech.challenge.establishment.manager.domain.entities.MenuItem;
import tech.challenge.establishment.manager.domain.valueobjects.RestaurantId;
import tech.challenge.establishment.manager.presentation.dtos.menuItem.CreateMenuItemDTO;
import tech.challenge.establishment.manager.presentation.dtos.menuItem.MenuItemResponseDTO;
import tech.challenge.establishment.manager.presentation.dtos.menuItem.UpdateMenuItemDTO;

public class MenuItemDtoMapper {

    public static MenuItem fromCreateDto(CreateMenuItemDTO dto) {
        return MenuItem.create(
                dto.name(),
                dto.description(),
                dto.price(),
                dto.photoUrl(),
                RestaurantId.of(dto.restaurantId())
        );
    }

    public static MenuItem fromUpdateDto(UpdateMenuItemDTO dto, MenuItem existingMenuItem) {
        return MenuItem.of(
                existingMenuItem.getId() != null ? existingMenuItem.getId().value() : null,
                dto.name(),
                dto.description(),
                dto.price(),
                dto.photoUrl(),
                existingMenuItem.getRestaurantId()
        );
    }

    public static MenuItemResponseDTO toResponseDto(MenuItem menuItem) {
        if (menuItem == null) return null;

        return new MenuItemResponseDTO(
                menuItem.getId() != null ? menuItem.getId().value() : null,
                menuItem.getName().value(),
                menuItem.getDescription(),
                menuItem.getPrice(),
                menuItem.getPhotoUrl(),
                menuItem.getRestaurantId() != null ? menuItem.getRestaurantId().value() : null
        );
    }
}
