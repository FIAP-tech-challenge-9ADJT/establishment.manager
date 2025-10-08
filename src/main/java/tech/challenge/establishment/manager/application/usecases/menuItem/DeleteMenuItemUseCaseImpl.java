package tech.challenge.establishment.manager.application.usecases.menuItem;

import org.springframework.stereotype.Service;
import tech.challenge.establishment.manager.domain.repositories.MenuItemRepository;
import tech.challenge.establishment.manager.domain.repositories.RestaurantRepository;
import tech.challenge.establishment.manager.domain.usecases.menuItem.DeleteMenuItemUseCase;
import tech.challenge.establishment.manager.domain.valueobjects.MenuItemId;
import tech.challenge.establishment.manager.domain.valueobjects.UserId;

@Service
public class DeleteMenuItemUseCaseImpl extends DeleteMenuItemUseCase {

    public DeleteMenuItemUseCaseImpl(MenuItemRepository menuItemRepository, RestaurantRepository restaurantRepository) {
        super(menuItemRepository, restaurantRepository);
    }

    @Override
    public void execute(MenuItemId menuItemId, UserId currentUserId, boolean isAdmin) {
        super.execute(menuItemId, currentUserId, isAdmin);
    }
}
