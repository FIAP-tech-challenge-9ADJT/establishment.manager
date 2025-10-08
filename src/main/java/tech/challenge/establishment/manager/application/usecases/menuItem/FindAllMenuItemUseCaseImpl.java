package tech.challenge.establishment.manager.application.usecases.menuItem;

import org.springframework.stereotype.Service;
import tech.challenge.establishment.manager.domain.entities.MenuItem;
import tech.challenge.establishment.manager.domain.repositories.MenuItemRepository;
import tech.challenge.establishment.manager.domain.usecases.menuItem.FindAllMenuItemUseCase;
import tech.challenge.establishment.manager.domain.valueobjects.RestaurantId;

import java.util.List;

@Service
public class FindAllMenuItemUseCaseImpl extends FindAllMenuItemUseCase {

    public FindAllMenuItemUseCaseImpl(MenuItemRepository menuItemRepository) {
        super(menuItemRepository);
    }

    @Override
    public List<MenuItem> execute(RestaurantId restaurantId) {
        return super.execute(restaurantId);
    }
}
