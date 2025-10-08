package tech.challenge.establishment.manager.application.usecases.menuItem;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import tech.challenge.establishment.manager.domain.repositories.MenuItemRepository;
import tech.challenge.establishment.manager.domain.usecases.menuItem.DeleteMenuItemUseCase;
import tech.challenge.establishment.manager.domain.valueobjects.MenuItemId;

@Service
public class DeleteMenuItemUseCaseImpl extends DeleteMenuItemUseCase {

    public DeleteMenuItemUseCaseImpl(MenuItemRepository menuItemRepository) {
        super(menuItemRepository);
    }

    public void execute (MenuItemId menuItemId) {
        super.execute(menuItemId);
    }
}
