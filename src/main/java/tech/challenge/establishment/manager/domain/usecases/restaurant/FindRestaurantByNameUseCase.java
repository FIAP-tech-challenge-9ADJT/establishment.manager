package tech.challenge.establishment.manager.domain.usecases.restaurant;

import tech.challenge.establishment.manager.domain.entities.Restaurant;
import tech.challenge.establishment.manager.domain.repositories.RestaurantRepository;

import java.util.List;

public class FindRestaurantByNameUseCase {
    private final RestaurantRepository restaurantRepository;

    public FindRestaurantByNameUseCase(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    public List<Restaurant> execute(String name) {
        return restaurantRepository.findByNameContainingIgnoreCase(name);
    }
}
