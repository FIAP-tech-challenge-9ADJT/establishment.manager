package tech.challenge.establishment.manager.application.usecases.restaurant;

import org.springframework.stereotype.Service;
import tech.challenge.establishment.manager.domain.entities.Address;
import tech.challenge.establishment.manager.domain.entities.Restaurant;
import tech.challenge.establishment.manager.domain.entities.RestaurantAddress;
import tech.challenge.establishment.manager.domain.repositories.RestaurantRepository;
import tech.challenge.establishment.manager.domain.repositories.UserRepository;
import tech.challenge.establishment.manager.domain.usecases.restaurant.CreateRestaurantUseCase;
import tech.challenge.establishment.manager.domain.valueobjects.KitchenType;
import tech.challenge.establishment.manager.domain.valueobjects.UserId;

import java.time.LocalTime;

@Service
public class CreateRestaurantUseCaseImpl extends CreateRestaurantUseCase {

    public CreateRestaurantUseCaseImpl(RestaurantRepository restaurantRepository, UserRepository userRepository) {
        super(restaurantRepository, userRepository);
    }

    public Restaurant execute(String name, RestaurantAddress restaurantAddress, KitchenType kitchenType,
                              LocalTime startOperation, LocalTime endOperation, UserId ownerId) {
        return super.execute(name, restaurantAddress, kitchenType, startOperation, endOperation, ownerId);
    }
}
