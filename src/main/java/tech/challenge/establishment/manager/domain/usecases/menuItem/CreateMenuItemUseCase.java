package tech.challenge.establishment.manager.domain.usecases.menuItem;

import tech.challenge.establishment.manager.domain.entities.MenuItem;
import tech.challenge.establishment.manager.domain.exceptions.MenuItemAlreadyExistsException;
import tech.challenge.establishment.manager.domain.repositories.MenuItemRepository;
import tech.challenge.establishment.manager.domain.repositories.RestaurantRepository;
import tech.challenge.establishment.manager.domain.valueobjects.RestaurantId;

public class CreateMenuItemUseCase {

    protected final MenuItemRepository menuItemsRepository;
    protected final RestaurantRepository restaurantRepository;

    public CreateMenuItemUseCase(MenuItemRepository menuItemsRepository, RestaurantRepository restaurantRepository) {
        this.menuItemsRepository = menuItemsRepository;
        this.restaurantRepository = restaurantRepository;
    }

    public MenuItem execute(String name, String description, Double price,
                            String photoUrl, RestaurantId restaurantId) {

        if (!restaurantRepository.existsById(restaurantId)) {
            throw new IllegalArgumentException("Restaurant not found: " + restaurantId.value());
        }

        if (menuItemsRepository.existsByNameAndRestaurant(name, restaurantId)) {
            throw new MenuItemAlreadyExistsException(name, restaurantId);
        }

        MenuItem menuItem = MenuItem.create(
                name,
                description,
                price,
                photoUrl,
                restaurantId
        );

        return menuItemsRepository.save(menuItem);
    }
}

