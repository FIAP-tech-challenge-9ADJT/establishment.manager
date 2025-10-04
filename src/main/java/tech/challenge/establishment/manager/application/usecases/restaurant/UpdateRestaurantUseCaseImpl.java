package tech.challenge.establishment.manager.application.usecases.restaurant;

import org.springframework.stereotype.Service;
import tech.challenge.establishment.manager.domain.entities.Restaurant;
import tech.challenge.establishment.manager.domain.entities.RestaurantAddress;
import tech.challenge.establishment.manager.domain.repositories.RestaurantRepository;
import tech.challenge.establishment.manager.domain.usecases.restaurant.UpdateRestaurantUseCase;
import tech.challenge.establishment.manager.domain.valueobjects.KitchenType;
import tech.challenge.establishment.manager.domain.valueobjects.RestaurantId;

import java.time.LocalTime;

@Service
public class UpdateRestaurantUseCaseImpl extends UpdateRestaurantUseCase {

    public UpdateRestaurantUseCaseImpl(RestaurantRepository restaurantRepository) {
        super(restaurantRepository);
    }

    public Restaurant execute(RestaurantId restaurantId,
                              String name,
                              RestaurantAddress restaurantAddress,
                              LocalTime startOperation,
                              LocalTime endOperation,
                              KitchenType kitchenType) {
        return super.execute(restaurantId, name, restaurantAddress, startOperation, endOperation, kitchenType);
    }
}
