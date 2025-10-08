package tech.challenge.establishment.manager.application.usecases.restaurant;

import org.springframework.stereotype.Service;
import tech.challenge.establishment.manager.domain.entities.Restaurant;
import tech.challenge.establishment.manager.domain.repositories.RestaurantRepository;
import tech.challenge.establishment.manager.domain.usecases.restaurant.FindRestaurantByIdUseCase;
import tech.challenge.establishment.manager.domain.valueobjects.RestaurantId;

@Service
public class FindRestaurantByIdUseCaseImpl extends FindRestaurantByIdUseCase {

    public FindRestaurantByIdUseCaseImpl(RestaurantRepository restaurantRepository) {
        super(restaurantRepository);
    }

    public Restaurant execute(RestaurantId restaurantId) {
        return super.execute(restaurantId);
    }
}
