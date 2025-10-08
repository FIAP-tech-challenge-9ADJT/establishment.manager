package tech.challenge.establishment.manager.application.usecases.restaurant;

import org.springframework.stereotype.Service;
import tech.challenge.establishment.manager.domain.entities.Restaurant;
import tech.challenge.establishment.manager.domain.repositories.RestaurantRepository;
import tech.challenge.establishment.manager.domain.usecases.restaurant.FindAllRestaurantsUseCase;

import java.util.List;

@Service
public class FindAllRestaurantsUseCaseImpl extends FindAllRestaurantsUseCase {

    public FindAllRestaurantsUseCaseImpl(RestaurantRepository restaurantRepository) {
        super(restaurantRepository);
    }

    public List<Restaurant> execute() {
        return super.execute();
    }
}

