package tech.challenge.establishment.manager.domain.usecases.restaurant;

import tech.challenge.establishment.manager.domain.exceptions.RestaurantNotFoundException;
import tech.challenge.establishment.manager.domain.repositories.RestaurantRepository;
import tech.challenge.establishment.manager.domain.valueobjects.RestaurantId;

public class DeleteRestaurantUseCase {

    private final RestaurantRepository restaurantRepository;

    public DeleteRestaurantUseCase(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    public void execute(RestaurantId id) {
        if (!restaurantRepository.existsById(id)) {
            throw new RestaurantNotFoundException(id);
        }

        restaurantRepository.deleteById(id);
    }
}
