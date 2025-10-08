package tech.challenge.establishment.manager.domain.usecases.menuItem;

import tech.challenge.establishment.manager.domain.exceptions.MenuItemNotFoundException;
import tech.challenge.establishment.manager.domain.repositories.MenuItemRepository;
import tech.challenge.establishment.manager.domain.valueobjects.MenuItemId;

public class DeleteMenuItemUseCase {

    protected final MenuItemRepository menuItemRepository;

    public DeleteMenuItemUseCase(MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }

    public void execute(MenuItemId id) {

        if (!menuItemRepository.existsById(id)) {
            throw new MenuItemNotFoundException(id);

        }

        menuItemRepository.deleteById(id);
    }
}
