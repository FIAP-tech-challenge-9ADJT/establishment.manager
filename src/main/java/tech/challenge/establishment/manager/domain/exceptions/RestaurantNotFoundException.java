package tech.challenge.establishment.manager.domain.exceptions;

import tech.challenge.establishment.manager.domain.valueobjects.RestaurantId;

public class RestaurantNotFoundException extends DomainException {

    public RestaurantNotFoundException(RestaurantId restaurantId) {
        super("Restaurant not found with ID: " + restaurantId.value());
    }
}
