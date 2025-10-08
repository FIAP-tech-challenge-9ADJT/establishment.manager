package tech.challenge.establishment.manager.domain.usecases.menuItem;

import tech.challenge.establishment.manager.domain.entities.MenuItem;
import tech.challenge.establishment.manager.domain.entities.Restaurant;
import tech.challenge.establishment.manager.domain.exceptions.AccessDeniedException;
import tech.challenge.establishment.manager.domain.exceptions.MenuItemAlreadyExistsException;
import tech.challenge.establishment.manager.domain.exceptions.MenuItemNotFoundException;
import tech.challenge.establishment.manager.domain.repositories.MenuItemRepository;
import tech.challenge.establishment.manager.domain.repositories.RestaurantRepository;
import tech.challenge.establishment.manager.domain.valueobjects.MenuItemId;
import tech.challenge.establishment.manager.domain.valueobjects.Name;
import tech.challenge.establishment.manager.domain.valueobjects.RestaurantId;
import tech.challenge.establishment.manager.domain.valueobjects.UserId;

public class UpdateMenuItemUseCase {

    protected final MenuItemRepository menuItemsRepository;
    protected final RestaurantRepository restaurantRepository;

    public UpdateMenuItemUseCase(MenuItemRepository menuItemsRepository, RestaurantRepository restaurantRepository) {
        this.menuItemsRepository = menuItemsRepository;
        this.restaurantRepository = restaurantRepository;
    }

    public MenuItem execute(MenuItemId id, String newName, String newDescription, Double newPrice,
                            String newPhotoUrl, RestaurantId restaurantId, UserId currentUserId, boolean isAdmin) {

        MenuItem existing = menuItemsRepository.findById(id)
                .orElseThrow(() -> new MenuItemNotFoundException(id));

        // Verificar se o usuário atual é o dono do restaurante do item ou ADMIN
        if (!isAdmin) {
            Restaurant restaurant = restaurantRepository.findById(existing.getRestaurantId())
                    .orElseThrow(() -> new IllegalArgumentException("Restaurant not found: " + existing.getRestaurantId().value()));

            if (!restaurant.getOwnerId().equals(currentUserId)) {
                throw new AccessDeniedException("Access denied: You can only update menu items from your own restaurant");
            }
        }

        final MenuItemId existingId = existing.getId();

        Name finalName = newName != null ? Name.of(newName) : existing.getName();
        String finalDescription = newDescription != null ? newDescription : existing.getDescription();
        Double finalPrice = newPrice != null ? newPrice : existing.getPrice();
        String finalPhotoUrl = newPhotoUrl != null ? newPhotoUrl : existing.getPhotoUrl();
        RestaurantId finalRestaurantId = restaurantId != null ? restaurantId : existing.getRestaurantId();

        if (menuItemsRepository.existsByNameAndRestaurant(finalName.value(), finalRestaurantId)) {
            menuItemsRepository.findAllByRestaurant(finalRestaurantId)
                    .stream()
                    .filter(item -> item.getName().equals(finalName) && !item.getId().equals(existingId))
                    .findAny()
                    .ifPresent(conflict -> {
                        throw new MenuItemAlreadyExistsException(finalName.value(), finalRestaurantId);
                    });
        }

        MenuItem updated = MenuItem.of(
                existing.getId().value(),
                finalName.value(),
                finalDescription,
                finalPrice,
                finalPhotoUrl,
                finalRestaurantId
        );

        return menuItemsRepository.save(updated);
    }
}

