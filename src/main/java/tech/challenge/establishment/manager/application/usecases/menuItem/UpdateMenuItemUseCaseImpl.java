package tech.challenge.establishment.manager.application.usecases.menuItem;

import org.springframework.stereotype.Service;
import tech.challenge.establishment.manager.domain.entities.MenuItem;
import tech.challenge.establishment.manager.domain.repositories.MenuItemRepository;
import tech.challenge.establishment.manager.domain.repositories.RestaurantRepository;
import tech.challenge.establishment.manager.domain.usecases.menuItem.UpdateMenuItemUseCase;
import tech.challenge.establishment.manager.domain.valueobjects.MenuItemId;
import tech.challenge.establishment.manager.domain.valueobjects.RestaurantId;
import tech.challenge.establishment.manager.domain.valueobjects.UserId;

@Service
public class UpdateMenuItemUseCaseImpl extends UpdateMenuItemUseCase {

    public UpdateMenuItemUseCaseImpl(MenuItemRepository menuItemRepository, RestaurantRepository restaurantRepository) {
        super(menuItemRepository, restaurantRepository);
    }

    @Override
    public MenuItem execute(MenuItemId menuItemId,
                            String name,
                            String description,
                            Double price,
                            String photoUrl,
                            RestaurantId restaurantId,
                            UserId currentUserId,
                            boolean isAdmin) {
        return super.execute(menuItemId, name, description, price, photoUrl, restaurantId, currentUserId, isAdmin);
    }
}