package tech.challenge.establishment.manager.presentation.mappers;

import org.springframework.stereotype.Component;
import tech.challenge.establishment.manager.domain.entities.MenuItem;
import tech.challenge.establishment.manager.domain.valueobjects.MenuItemId;
import tech.challenge.establishment.manager.domain.valueobjects.Price;
import tech.challenge.establishment.manager.domain.valueobjects.RestaurantId;
import tech.challenge.establishment.manager.presentation.dtos.menuItem.MenuItemRequestDTO;
import tech.challenge.establishment.manager.presentation.dtos.menuItem.MenuItemResponseDTO;

@Component
public class MenuItemMapper {

    public MenuItem toDomain(MenuItemRequestDTO dto) {
        return MenuItem.create(
                dto.name(),
                dto.description(),
                new Price(dto.price()),
                dto.onlyInRestaurant(),
                dto.photoPath(),
                RestaurantId.of(dto.restaurantId())
        );
    }

    public MenuItemResponseDTO toResponseDTO(MenuItem domain) {
        return new MenuItemResponseDTO(
                domain.getId() != null ? domain.getId().value() : null,
                domain.getName(),
                domain.getDescription(),
                domain.getPrice() != null ? domain.getPrice().value() : 0.0,
                domain.isOnlyInRestaurant(),
                domain.getPhotoPath(),
                domain.getRestaurantId() != null ? domain.getRestaurantId().value() : null
        );
    }

    public MenuItem toDomain(Long id, MenuItemRequestDTO dto) {
        return MenuItem.of(
                id,
                dto.name(),
                dto.description(),
                new Price(dto.price()),
                dto.onlyInRestaurant(),
                dto.photoPath(),
                RestaurantId.of(dto.restaurantId())
        );
    }
}
