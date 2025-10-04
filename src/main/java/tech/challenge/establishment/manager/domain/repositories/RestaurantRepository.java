package tech.challenge.establishment.manager.domain.repositories;

import tech.challenge.establishment.manager.domain.entities.Address;
import tech.challenge.establishment.manager.domain.entities.Restaurant;
import tech.challenge.establishment.manager.domain.entities.RestaurantAddress;
import tech.challenge.establishment.manager.domain.valueobjects.Name;
import tech.challenge.establishment.manager.domain.valueobjects.RestaurantId;

import java.util.List;
import java.util.Optional;

public interface RestaurantRepository {
    Restaurant save(Restaurant restaurant);
    Optional<Restaurant> findById(RestaurantId id);
    Optional<Restaurant> findByName(Name name);
    List<Restaurant> findAll();
    boolean existsById(RestaurantId id);
    void deleteById(RestaurantId id);
    List<Restaurant> findByNameContainingIgnoreCase(String name);
    boolean existsByNameAndRestaurantAddress(Name name, RestaurantAddress restaurantAddress);
    Optional<Restaurant> findByNameAndRestaurantAddress(Name name, RestaurantAddress restaurantAddress);
}
