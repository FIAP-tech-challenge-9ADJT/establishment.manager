package tech.challenge.establishment.manager.domain.usecases.restaurant;

import tech.challenge.establishment.manager.domain.entities.Address;
import tech.challenge.establishment.manager.domain.entities.Restaurant;
import tech.challenge.establishment.manager.domain.entities.RestaurantAddress;
import tech.challenge.establishment.manager.domain.exceptions.RestaurantAlreadyExistsException;
import tech.challenge.establishment.manager.domain.repositories.RestaurantRepository;
import tech.challenge.establishment.manager.domain.repositories.UserRepository;
import tech.challenge.establishment.manager.domain.valueobjects.KitchenType;
import tech.challenge.establishment.manager.domain.valueobjects.Name;
import tech.challenge.establishment.manager.domain.valueobjects.UserId;

import java.time.LocalTime;

public class CreateRestaurantUseCase {

    protected final RestaurantRepository restaurantRepository;
    protected final UserRepository userRepository;

    public CreateRestaurantUseCase(RestaurantRepository restaurantRepository, UserRepository userRepository) {
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
    }

    public Restaurant execute(String name, RestaurantAddress restaurantAddress, KitchenType kitchenType,
                              LocalTime startOperation, LocalTime endOperation, UserId ownerId) {
        // Valida se o dono existe
        if (!userRepository.existsById(ownerId)) {
            throw new IllegalArgumentException("Owner user not found: " + ownerId.value());
        }

        // Valida se já existe restaurante igual
        if (restaurantRepository.existsByNameAndRestaurantAddress(Name.of(name), restaurantAddress)) {
            throw new RestaurantAlreadyExistsException(name, restaurantAddress);
        }

        // Cria entidade
        Restaurant restaurant = Restaurant.create(name, restaurantAddress, kitchenType, startOperation, endOperation, ownerId);

        return restaurantRepository.save(restaurant);
    }
}
