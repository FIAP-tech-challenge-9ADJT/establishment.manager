package tech.challenge.establishment.manager.domain.usecases.menuItem;

import tech.challenge.establishment.manager.domain.entities.Restaurant;
import tech.challenge.establishment.manager.domain.exceptions.AccessDeniedException;
import tech.challenge.establishment.manager.domain.exceptions.MenuItemNotFoundException;
import tech.challenge.establishment.manager.domain.repositories.MenuItemRepository;
import tech.challenge.establishment.manager.domain.repositories.RestaurantRepository;
import tech.challenge.establishment.manager.domain.valueobjects.MenuItemId;
import tech.challenge.establishment.manager.domain.valueobjects.UserId;

public class DeleteMenuItemUseCase {

    protected final MenuItemRepository menuItemRepository;
    protected final RestaurantRepository restaurantRepository;

    public DeleteMenuItemUseCase(MenuItemRepository menuItemRepository, RestaurantRepository restaurantRepository) {
        this.menuItemRepository = menuItemRepository;
        this.restaurantRepository = restaurantRepository;
    }

    public void execute(MenuItemId id, UserId currentUserId, boolean isAdmin) {
        var existing = menuItemRepository.findById(id)
                .orElseThrow(() -> new MenuItemNotFoundException(id));

        // Verificar se o usuário atual é o dono do restaurante do item ou ADMIN
        if (!isAdmin) {
            Restaurant restaurant = restaurantRepository.findById(existing.getRestaurantId())
                    .orElseThrow(() -> new IllegalArgumentException("Restaurant not found: " + existing.getRestaurantId().value()));

            if (!restaurant.getOwnerId().equals(currentUserId)) {
                throw new AccessDeniedException("Access denied: You can only delete menu items from your own restaurant");
            }
        }

        menuItemRepository.deleteById(existing.getId());
    }
}
