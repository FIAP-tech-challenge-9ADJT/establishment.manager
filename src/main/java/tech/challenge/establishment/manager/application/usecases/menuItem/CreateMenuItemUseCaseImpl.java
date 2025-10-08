package tech.challenge.establishment.manager.application.usecases.menuItem;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import tech.challenge.establishment.manager.domain.entities.MenuItem;
import tech.challenge.establishment.manager.domain.repositories.MenuItemRepository;
import tech.challenge.establishment.manager.domain.repositories.RestaurantRepository;
import tech.challenge.establishment.manager.domain.usecases.menuItem.CreateMenuItemUseCase;
import tech.challenge.establishment.manager.domain.valueobjects.Price;
import tech.challenge.establishment.manager.domain.valueobjects.RestaurantId;
import tech.challenge.establishment.manager.domain.exceptions.RestaurantNotFoundException;

@Service
public class CreateMenuItemUseCaseImpl extends CreateMenuItemUseCase {

    public CreateMenuItemUseCaseImpl(MenuItemRepository menuItemRepository,
                                     RestaurantRepository restaurantRepository) {
        super(menuItemRepository);
    }

    public MenuItem execute(String name, String description, Price price,
                            boolean onlyInRestaurant, String photoPath, RestaurantId restaurantId) {

        return super.execute(name, description, price, onlyInRestaurant, photoPath, restaurantId);
    }
}
