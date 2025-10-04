package tech.challenge.establishment.manager.domain.usecases.restaurant;

import tech.challenge.establishment.manager.domain.entities.Restaurant;
import tech.challenge.establishment.manager.domain.repositories.RestaurantRepository;

import java.util.List;

public class FindAllRestaurantsUseCase {

    private final RestaurantRepository restaurantRepository;

    public FindAllRestaurantsUseCase(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    public List<Restaurant> execute() {
        return restaurantRepository.findAll();
    }
}
