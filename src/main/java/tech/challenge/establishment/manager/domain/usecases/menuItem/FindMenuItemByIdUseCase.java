package tech.challenge.establishment.manager.domain.usecases.menuItem;

import tech.challenge.establishment.manager.domain.entities.MenuItem;
import tech.challenge.establishment.manager.domain.exceptions.MenuItemNotFoundException;
import tech.challenge.establishment.manager.domain.repositories.MenuItemRepository;
import tech.challenge.establishment.manager.domain.valueobjects.MenuItemId;

public class FindMenuItemByIdUseCase {

    protected MenuItemRepository menuItemRepository;

    public FindMenuItemByIdUseCase(MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }

    public MenuItem execute(MenuItemId menuItemId) {
        return menuItemRepository.findById(menuItemId)
                .orElseThrow(() -> new MenuItemNotFoundException(menuItemId));
    }
}
