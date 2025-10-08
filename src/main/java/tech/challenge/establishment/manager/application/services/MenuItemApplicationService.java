package tech.challenge.establishment.manager.application.services;

import org.springframework.stereotype.Service;
import tech.challenge.establishment.manager.application.usecases.menuItem.*;
import tech.challenge.establishment.manager.domain.entities.MenuItem;
import tech.challenge.establishment.manager.domain.valueobjects.*;

import java.util.List;

@Service
public class MenuItemApplicationService {

    private final CreateMenuItemUseCaseImpl createMenuItemUseCase;
    private final UpdateMenuItemUseCaseImpl updateMenuItemUseCase;
    private final FindAllMenuItemsByRestaurantUseCaseImpl findAllMenuItemsByRestaurantUseCase;
    private final FindMenuItemByIdUseCaseImpl findMenuItemByIdUseCase;
    private final DeleteMenuItemUseCaseImpl deleteMenuItemUseCase;

    public MenuItemApplicationService(
            CreateMenuItemUseCaseImpl createMenuItemUseCase,
            UpdateMenuItemUseCaseImpl updateMenuItemUseCase,
            FindAllMenuItemsByRestaurantUseCaseImpl findAllMenuItemsByRestaurantUseCase,
            FindMenuItemByIdUseCaseImpl findMenuItemByIdUseCase,
            DeleteMenuItemUseCaseImpl deleteMenuItemUseCase
    ) {
        this.createMenuItemUseCase = createMenuItemUseCase;
        this.updateMenuItemUseCase = updateMenuItemUseCase;
        this.findAllMenuItemsByRestaurantUseCase = findAllMenuItemsByRestaurantUseCase;
        this.findMenuItemByIdUseCase = findMenuItemByIdUseCase;
        this.deleteMenuItemUseCase = deleteMenuItemUseCase;
    }

    public MenuItem createMenuItem(String name, String description, Price price,
                                   boolean onlyInRestaurant, String photoPath, RestaurantId restaurantId) {
        return createMenuItemUseCase.execute(name, description, price, onlyInRestaurant, photoPath, restaurantId);
    }

    public MenuItem updateMenuItem(MenuItemId menuItemId, String name, String description,
                                   Price price, Boolean onlyInRestaurant, String photoPath) {
        return updateMenuItemUseCase.execute(menuItemId, name, description, price, onlyInRestaurant, photoPath);
    }

    public void deleteMenuItem(MenuItemId menuItemId) {
        deleteMenuItemUseCase.execute(menuItemId);
    }

    public List<MenuItem> getAllMenuItemsByRestaurant(RestaurantId restaurantId) {
        return findAllMenuItemsByRestaurantUseCase.execute(restaurantId);
    }

    public MenuItem getMenuItemById(MenuItemId menuItemId) {
        return findMenuItemByIdUseCase.execute(menuItemId);
    }
}
