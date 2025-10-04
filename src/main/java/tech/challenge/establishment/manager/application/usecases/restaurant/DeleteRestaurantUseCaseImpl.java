package tech.challenge.establishment.manager.application.usecases.restaurant;

import org.springframework.stereotype.Service;
import tech.challenge.establishment.manager.domain.repositories.RestaurantRepository;
import tech.challenge.establishment.manager.domain.usecases.restaurant.DeleteRestaurantUseCase;
import tech.challenge.establishment.manager.domain.valueobjects.RestaurantId;

@Service
public class DeleteRestaurantUseCaseImpl extends DeleteRestaurantUseCase {

    public DeleteRestaurantUseCaseImpl(RestaurantRepository restaurantRepository) {
        super(restaurantRepository);
    }

    public void execute(RestaurantId restaurantId) {
        super.execute(restaurantId);
    }
}
