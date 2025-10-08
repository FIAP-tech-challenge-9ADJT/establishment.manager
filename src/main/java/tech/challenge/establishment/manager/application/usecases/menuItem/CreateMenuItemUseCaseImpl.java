package tech.challenge.establishment.manager.application.usecases.menuItem;

import org.springframework.stereotype.Service;
import tech.challenge.establishment.manager.domain.entities.MenuItem;
import tech.challenge.establishment.manager.domain.repositories.MenuItemRepository;
import tech.challenge.establishment.manager.domain.repositories.RestaurantRepository;
import tech.challenge.establishment.manager.domain.usecases.menuItem.CreateMenuItemUseCase;
import tech.challenge.establishment.manager.domain.valueobjects.RestaurantId;
import tech.challenge.establishment.manager.domain.valueobjects.UserId;

@Service
public class CreateMenuItemUseCaseImpl extends CreateMenuItemUseCase {

    public CreateMenuItemUseCaseImpl(MenuItemRepository menuItemRepository,
                                     RestaurantRepository restaurantRepository) {
        super(menuItemRepository, restaurantRepository);
    }

    @Override
    public MenuItem execute(String name,
                            String description,
                            Double price,
                            String photoUrl,
                            RestaurantId restaurantId,
                            UserId currentUserId,
                            boolean isAdmin) {
        return super.execute(name, description, price, photoUrl, restaurantId, currentUserId, isAdmin);
    }
}

