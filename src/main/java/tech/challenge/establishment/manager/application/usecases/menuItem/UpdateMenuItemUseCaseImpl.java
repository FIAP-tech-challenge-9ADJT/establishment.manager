package tech.challenge.establishment.manager.application.usecases.menuItem;

import org.springframework.stereotype.Service;
import tech.challenge.establishment.manager.domain.entities.MenuItem;
import tech.challenge.establishment.manager.domain.repositories.MenuItemRepository;
import tech.challenge.establishment.manager.domain.usecases.menuItem.UpdateMenuItemUseCase;
import tech.challenge.establishment.manager.domain.valueobjects.MenuItemId;
import tech.challenge.establishment.manager.domain.valueobjects.RestaurantId;

@Service
public class UpdateMenuItemUseCaseImpl extends UpdateMenuItemUseCase {

    public UpdateMenuItemUseCaseImpl(MenuItemRepository menuItemRepository) {
        super(menuItemRepository);
    }

    @Override
    public MenuItem execute(MenuItemId menuItemId,
                            String name,
                            String description,
                            Double price,
                            String photoUrl,
                            RestaurantId restaurantId) {
        return super.execute(menuItemId, name, description, price, photoUrl, restaurantId);
    }
}