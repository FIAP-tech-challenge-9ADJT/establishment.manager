package tech.challenge.establishment.manager.application.usecases.menuItem;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import tech.challenge.establishment.manager.domain.entities.MenuItem;
import tech.challenge.establishment.manager.domain.repositories.MenuItemRepository;
import tech.challenge.establishment.manager.domain.usecases.menuItem.FindAllMenuItemsByRestaurantUseCase;
import tech.challenge.establishment.manager.domain.valueobjects.RestaurantId;

import java.util.List;

@Service
public class FindAllMenuItemsByRestaurantUseCaseImpl extends FindAllMenuItemsByRestaurantUseCase {

    public FindAllMenuItemsByRestaurantUseCaseImpl(MenuItemRepository menuItemRepository) {
        super(menuItemRepository);
    }

    public List<MenuItem> execute(RestaurantId restaurantId) {
        return super.execute(restaurantId);
    }
}
