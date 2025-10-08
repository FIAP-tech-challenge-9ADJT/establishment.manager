package tech.challenge.establishment.manager.domain.usecases.menuItem;

import tech.challenge.establishment.manager.domain.entities.MenuItem;
import tech.challenge.establishment.manager.domain.repositories.MenuItemRepository;
import tech.challenge.establishment.manager.domain.valueobjects.RestaurantId;

import java.util.List;

public class FindAllMenuItemsByRestaurantUseCase {

    protected final MenuItemRepository menuItemRepository;

    public FindAllMenuItemsByRestaurantUseCase(MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }

    public List<MenuItem> execute(RestaurantId restaurantId) {
        return menuItemRepository.findAll(restaurantId);
    }
}
