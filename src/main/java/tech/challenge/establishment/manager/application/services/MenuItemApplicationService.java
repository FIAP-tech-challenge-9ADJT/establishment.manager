package tech.challenge.establishment.manager.application.services;

import org.springframework.stereotype.Service;
import tech.challenge.establishment.manager.application.usecases.menuItem.*;
import tech.challenge.establishment.manager.domain.entities.MenuItem;
import tech.challenge.establishment.manager.domain.valueobjects.MenuItemId;
import tech.challenge.establishment.manager.domain.valueobjects.RestaurantId;

import java.util.List;

@Service
public class MenuItemApplicationService {

    private final CreateMenuItemUseCaseImpl createMenuItemUseCase;
    private final UpdateMenuItemUseCaseImpl updateMenuItemUseCase;
    private final DeleteMenuItemUseCaseImpl deleteMenuItemUseCase;
    private final FindMenuItemByIdUseCaseImpl findMenuItemByIdUseCase;
    private final FindAllMenuItemUseCaseImpl findAllMenuItemUseCase;

    public MenuItemApplicationService(
            CreateMenuItemUseCaseImpl createMenuItemUseCase,
            UpdateMenuItemUseCaseImpl updateMenuItemUseCase,
            DeleteMenuItemUseCaseImpl deleteMenuItemUseCase,
            FindMenuItemByIdUseCaseImpl findMenuItemByIdUseCase,
            FindAllMenuItemUseCaseImpl findAllMenuItemUseCase
    ) {
        this.createMenuItemUseCase = createMenuItemUseCase;
        this.updateMenuItemUseCase = updateMenuItemUseCase;
        this.deleteMenuItemUseCase = deleteMenuItemUseCase;
        this.findMenuItemByIdUseCase = findMenuItemByIdUseCase;
        this.findAllMenuItemUseCase = findAllMenuItemUseCase;
    }

    public MenuItem createMenuItem(String name, String description, Double price,
                                   String photoUrl, RestaurantId restaurantId) {
        return createMenuItemUseCase.execute(name, description, price, photoUrl, restaurantId);
    }

    public MenuItem updateMenuItem(MenuItemId menuItemId, String name, String description,
                                   Double price, String photoUrl, RestaurantId restaurantId) {
        return updateMenuItemUseCase.execute(menuItemId, name, description, price, photoUrl, restaurantId);
    }

    public void deleteMenuItem(MenuItemId menuItemId) {
        deleteMenuItemUseCase.execute(menuItemId);
    }

    public MenuItem getMenuItemById(MenuItemId menuItemId) {
        return findMenuItemByIdUseCase.execute(menuItemId);
    }

    public List<MenuItem> getAllMenuItems(RestaurantId restaurantId) {
        return findAllMenuItemUseCase.execute(restaurantId);
    }
}
