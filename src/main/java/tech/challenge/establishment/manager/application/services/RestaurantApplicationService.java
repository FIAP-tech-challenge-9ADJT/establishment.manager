package tech.challenge.establishment.manager.application.services;

import org.springframework.stereotype.Service;
import tech.challenge.establishment.manager.application.usecases.restaurant.*;
import tech.challenge.establishment.manager.domain.entities.Restaurant;
import tech.challenge.establishment.manager.domain.entities.RestaurantAddress;
import tech.challenge.establishment.manager.domain.valueobjects.KitchenType;
import tech.challenge.establishment.manager.domain.valueobjects.RestaurantId;
import tech.challenge.establishment.manager.domain.valueobjects.UserId;

import java.time.LocalTime;
import java.util.List;

@Service
public class RestaurantApplicationService {

    private final CreateRestaurantUseCaseImpl createRestaurantUseCase;
    private final UpdateRestaurantUseCaseImpl updateRestaurantUseCase;
    private final DeleteRestaurantUseCaseImpl deleteRestaurantUseCase;
    private final FindAllRestaurantsUseCaseImpl findAllRestaurantsUseCase;
    private final FindRestaurantByIdUseCaseImpl findRestaurantByIdUseCase;
    private final FindRestaurantByNameUseCaseImpl findRestaurantByNameUseCase;

    public RestaurantApplicationService(
            CreateRestaurantUseCaseImpl createRestaurantUseCase,
            UpdateRestaurantUseCaseImpl updateRestaurantUseCase,
            DeleteRestaurantUseCaseImpl deleteRestaurantUseCase,
            FindAllRestaurantsUseCaseImpl findAllRestaurantsUseCase,
            FindRestaurantByIdUseCaseImpl findRestaurantByIdUseCase,
            FindRestaurantByNameUseCaseImpl findRestaurantByNameUseCase
    ) {
        this.createRestaurantUseCase = createRestaurantUseCase;
        this.updateRestaurantUseCase = updateRestaurantUseCase;
        this.deleteRestaurantUseCase = deleteRestaurantUseCase;
        this.findAllRestaurantsUseCase = findAllRestaurantsUseCase;
        this.findRestaurantByIdUseCase = findRestaurantByIdUseCase;
        this.findRestaurantByNameUseCase = findRestaurantByNameUseCase;
    }

    public Restaurant createRestaurant(String name, RestaurantAddress restaurantAddress, KitchenType kitchenType,
                                       LocalTime startOperation, LocalTime endOperation, UserId ownerId) {
        return createRestaurantUseCase.execute(name, restaurantAddress, kitchenType, startOperation, endOperation, ownerId);
    }

    public Restaurant updateRestaurant(RestaurantId restaurantId,
                                       String name,
                                       RestaurantAddress restaurantAddress,
                                       LocalTime startOperation,
                                       LocalTime endOperation,
                                       KitchenType kitchenType) {
        return updateRestaurantUseCase.execute(restaurantId, name, restaurantAddress, startOperation, endOperation, kitchenType);
    }

    public void deleteRestaurant(RestaurantId restaurantId) {
        deleteRestaurantUseCase.execute(restaurantId);
    }

    public List<Restaurant> getAllRestaurants() {
        return findAllRestaurantsUseCase.execute();
    }

    public Restaurant getRestaurantById(RestaurantId restaurantId) {
        return findRestaurantByIdUseCase.execute(restaurantId);
    }

    public List<Restaurant> getRestaurantsByName(String name) {
        return findRestaurantByNameUseCase.execute(name);
    }
}

