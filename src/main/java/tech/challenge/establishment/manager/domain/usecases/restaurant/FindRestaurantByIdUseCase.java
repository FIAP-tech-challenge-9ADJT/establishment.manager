package tech.challenge.establishment.manager.domain.usecases.restaurant;

import tech.challenge.establishment.manager.domain.entities.Restaurant;
import tech.challenge.establishment.manager.domain.exceptions.RestaurantNotFoundException;
import tech.challenge.establishment.manager.domain.repositories.RestaurantRepository;
import tech.challenge.establishment.manager.domain.valueobjects.RestaurantId;

public class FindRestaurantByIdUseCase {

    private final RestaurantRepository restaurantRepository;

    public FindRestaurantByIdUseCase(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    public Restaurant execute(RestaurantId id) {
        return restaurantRepository.findById(id)
                .orElseThrow(() -> new RestaurantNotFoundException(id));
    }
}
