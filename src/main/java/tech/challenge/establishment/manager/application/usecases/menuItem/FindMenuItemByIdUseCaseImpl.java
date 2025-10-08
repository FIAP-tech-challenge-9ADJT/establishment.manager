package tech.challenge.establishment.manager.application.usecases.menuItem;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import tech.challenge.establishment.manager.domain.entities.MenuItem;
import tech.challenge.establishment.manager.domain.repositories.MenuItemRepository;
import tech.challenge.establishment.manager.domain.usecases.menuItem.FindMenuItemByIdUseCase;
import tech.challenge.establishment.manager.domain.valueobjects.MenuItemId;

@Service
public class FindMenuItemByIdUseCaseImpl extends FindMenuItemByIdUseCase {

    public FindMenuItemByIdUseCaseImpl(MenuItemRepository menuItemRepository) {
        super(menuItemRepository);
    }

    public MenuItem execute (MenuItemId menuItemId) {
        return super.execute(menuItemId);
    }

}
