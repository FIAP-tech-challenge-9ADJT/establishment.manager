package tech.challenge.establishment.manager.domain.usecases.menuItem;

import tech.challenge.establishment.manager.domain.entities.MenuItem;
import tech.challenge.establishment.manager.domain.exceptions.MenuItemNotFoundException;
import tech.challenge.establishment.manager.domain.repositories.MenuItemRepository;
import tech.challenge.establishment.manager.domain.valueobjects.MenuItemId;

public class FindMenuItemByIdUseCase {

    protected final MenuItemRepository menuItemsRepository;

    public FindMenuItemByIdUseCase(MenuItemRepository menuItemRepository) {
        this.menuItemsRepository = menuItemRepository;
    }

    public MenuItem execute(MenuItemId id) {
        return menuItemsRepository.findById(id)
                .orElseThrow(() -> new MenuItemNotFoundException(id));
    }
}