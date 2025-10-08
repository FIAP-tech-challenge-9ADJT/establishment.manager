package tech.challenge.establishment.manager.domain.usecases.menuItem;

import tech.challenge.establishment.manager.domain.entities.MenuItem;
import tech.challenge.establishment.manager.domain.repositories.MenuItemRepository;
import tech.challenge.establishment.manager.domain.valueobjects.Price;
import tech.challenge.establishment.manager.domain.valueobjects.RestaurantId;

public class CreateMenuItemUseCase {

    protected final MenuItemRepository menuItemRepository;

    public CreateMenuItemUseCase(MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }

    public MenuItem execute(String name, String description, Price price,
                            boolean onlyInRestaurant, String photoPath, RestaurantId restaurantId) {

        MenuItem menuItem = MenuItem.create(name, description, price, onlyInRestaurant, photoPath, restaurantId);
        return menuItemRepository.save(menuItem);
    }
}
