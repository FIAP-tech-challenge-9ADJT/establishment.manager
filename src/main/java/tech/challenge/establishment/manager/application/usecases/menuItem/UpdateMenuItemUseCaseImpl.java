package tech.challenge.establishment.manager.application.usecases.menuItem;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import tech.challenge.establishment.manager.domain.entities.MenuItem;
import tech.challenge.establishment.manager.domain.repositories.MenuItemRepository;
import tech.challenge.establishment.manager.domain.usecases.menuItem.UpdateMenuItemUseCase;
import tech.challenge.establishment.manager.domain.valueobjects.MenuItemId;
import tech.challenge.establishment.manager.domain.valueobjects.Price;

@Service
public class UpdateMenuItemUseCaseImpl extends UpdateMenuItemUseCase {

    public UpdateMenuItemUseCaseImpl(MenuItemRepository menuItemRepository) {
        super(menuItemRepository);
    }

    public MenuItem execute (MenuItemId menuItemId, String name, String description, Price price, boolean onlyInRestaurant, String photoPath) {
        return super.execute(menuItemId, name, description, price, onlyInRestaurant, photoPath);
    }
}
