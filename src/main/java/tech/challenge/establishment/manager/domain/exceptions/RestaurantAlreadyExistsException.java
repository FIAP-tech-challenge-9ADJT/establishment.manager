package tech.challenge.establishment.manager.domain.exceptions;

import tech.challenge.establishment.manager.domain.entities.RestaurantAddress;

public class RestaurantAlreadyExistsException extends DomainException{

    public RestaurantAlreadyExistsException(String name, RestaurantAddress restaurantAddress) {
        super("Restaurant already exists with name: " + name + " and restaurantAddress: " + restaurantAddress);
    }
}
