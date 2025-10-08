package tech.challenge.establishment.manager.domain.exceptions;

import tech.challenge.establishment.manager.domain.valueobjects.MenuItemId;

public class MenuItemNotFoundException extends DomainException {

    public MenuItemNotFoundException(MenuItemId id) {
        super("MenuItem not found with ID: " + id.value());
    }

}
