package tech.challenge.establishment.manager.domain.usecases.menuItem;

import tech.challenge.establishment.manager.domain.entities.MenuItem;
import tech.challenge.establishment.manager.domain.exceptions.MenuItemNotFoundException;
import tech.challenge.establishment.manager.domain.repositories.MenuItemRepository;
import tech.challenge.establishment.manager.domain.valueobjects.MenuItemId;
import tech.challenge.establishment.manager.domain.valueobjects.Price;

public class UpdateMenuItemUseCase {

    protected MenuItemRepository menuItemRepository;

    public UpdateMenuItemUseCase(MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }

    public MenuItem execute(MenuItemId menuItemId, String name, String description, Price price, boolean onlyInRestaurant, String photoPath) {
        MenuItem existing = menuItemRepository.findById(menuItemId)
                .orElseThrow(() -> new MenuItemNotFoundException(menuItemId));

        String finalName = name != null ? name : existing.getName();
        String finalDescription = description != null ? description : existing.getDescription();
        Price finalPrice = price != null ? price : existing.getPrice();
        boolean finalOnlyInRestaurant = onlyInRestaurant;
        String finalPhotoPath = photoPath != null ? photoPath : existing.getPhotoPath();

        if (finalName != null && !existing.getName().equals(finalName)) {
            existing = existing.updateName(finalName);
        }


        if (finalDescription != null && !existing.getDescription().equals(finalDescription)) {
            existing = existing.updateDescription(finalDescription);
        }

        if (finalPrice != null && !existing.getPrice().equals(finalPrice)) {
            existing = existing.updatePrice(finalPrice);
        }

        if (existing.isOnlyInRestaurant() != finalOnlyInRestaurant) {
            existing = existing.updateOnlyInRestaurant(finalOnlyInRestaurant);
        }

        if (finalPhotoPath != null && !existing.getPhotoPath().equals(finalPhotoPath)) {
            existing = existing.updatePhotoPath(finalPhotoPath);
        }

        return menuItemRepository.save(existing);
    }

}
