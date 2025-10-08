package tech.challenge.establishment.manager.domain.exceptions;

import tech.challenge.establishment.manager.domain.valueobjects.RestaurantId;

public class MenuItemAlreadyExistsException extends DomainException{

    public MenuItemAlreadyExistsException(String name, RestaurantId restaurantId) {
        super("Menu item '" + name + "' already exists for restaurant ID: " + restaurantId.value());
    }
}
