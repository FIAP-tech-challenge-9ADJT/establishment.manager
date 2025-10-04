package tech.challenge.establishment.manager.application.usecases.restaurant;

import org.springframework.stereotype.Service;
import tech.challenge.establishment.manager.domain.entities.Restaurant;
import tech.challenge.establishment.manager.domain.repositories.RestaurantRepository;
import tech.challenge.establishment.manager.domain.usecases.restaurant.FindRestaurantByNameUseCase;

import java.util.List;

@Service
public class FindRestaurantByNameUseCaseImpl extends FindRestaurantByNameUseCase {

    public FindRestaurantByNameUseCaseImpl(RestaurantRepository restaurantRepository) {
        super(restaurantRepository);
    }

    public List<Restaurant> execute(String name) {
        return super.execute(name);
    }
}
