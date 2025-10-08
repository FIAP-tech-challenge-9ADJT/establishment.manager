package tech.challenge.establishment.manager.domain.exceptions;

import tech.challenge.establishment.manager.domain.valueobjects.MenuItemId;

public class MenuItemNotFoundException  extends DomainException {

    public MenuItemNotFoundException(MenuItemId menuItemId) {
        super("Menu Item not found with ID: " + menuItemId.value());
    }
}
