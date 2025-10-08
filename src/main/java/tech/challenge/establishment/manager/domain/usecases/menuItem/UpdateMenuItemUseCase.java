package tech.challenge.establishment.manager.domain.usecases.menuItem;

import tech.challenge.establishment.manager.domain.entities.MenuItem;
import tech.challenge.establishment.manager.domain.exceptions.MenuItemAlreadyExistsException;
import tech.challenge.establishment.manager.domain.exceptions.MenuItemNotFoundException;
import tech.challenge.establishment.manager.domain.repositories.MenuItemRepository;
import tech.challenge.establishment.manager.domain.valueobjects.MenuItemId;
import tech.challenge.establishment.manager.domain.valueobjects.Name;
import tech.challenge.establishment.manager.domain.valueobjects.RestaurantId;

public class UpdateMenuItemUseCase {

    protected final MenuItemRepository menuItemsRepository;

    public UpdateMenuItemUseCase(MenuItemRepository menuItemsRepository) {
        this.menuItemsRepository = menuItemsRepository;
    }

    public MenuItem execute(MenuItemId id, String newName, String newDescription, Double newPrice,
                            String newPhotoUrl, RestaurantId restaurantId) {

        MenuItem existing = menuItemsRepository.findById(id)
                .orElseThrow(() -> new MenuItemNotFoundException(id));

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

