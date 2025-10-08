package tech.challenge.establishment.manager.domain.usecases.menuItem;

import tech.challenge.establishment.manager.domain.entities.MenuItem;
import tech.challenge.establishment.manager.domain.entities.Restaurant;
import tech.challenge.establishment.manager.domain.exceptions.AccessDeniedException;
import tech.challenge.establishment.manager.domain.exceptions.MenuItemAlreadyExistsException;
import tech.challenge.establishment.manager.domain.repositories.MenuItemRepository;
import tech.challenge.establishment.manager.domain.repositories.RestaurantRepository;
import tech.challenge.establishment.manager.domain.valueobjects.RestaurantId;
import tech.challenge.establishment.manager.domain.valueobjects.UserId;

public class CreateMenuItemUseCase {

    protected final MenuItemRepository menuItemsRepository;
    protected final RestaurantRepository restaurantRepository;

    public CreateMenuItemUseCase(MenuItemRepository menuItemsRepository, RestaurantRepository restaurantRepository) {
        this.menuItemsRepository = menuItemsRepository;
        this.restaurantRepository = restaurantRepository;
    }

    public MenuItem execute(String name, String description, Double price,
                            String photoUrl, RestaurantId restaurantId, UserId currentUserId, boolean isAdmin) {

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new IllegalArgumentException("Restaurant not found: " + restaurantId.value()));

        // Verificar se o usuário atual é o dono do restaurante ou ADMIN
        if (!isAdmin && !restaurant.getOwnerId().equals(currentUserId)) {
            throw new AccessDeniedException("Access denied: You can only create menu items for your own restaurant");
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

