package tech.challenge.establishment.manager.domain.repositories;

import tech.challenge.establishment.manager.domain.entities.RestaurantAddress;
import tech.challenge.establishment.manager.domain.valueobjects.RestaurantAddressId;
import tech.challenge.establishment.manager.domain.valueobjects.RestaurantId;

import java.util.Optional;

public interface RestaurantAddressRepository {

    Optional<RestaurantAddress> findById(RestaurantAddressId id);

    Optional<RestaurantAddress> findByRestaurantId(RestaurantId restaurantId);

    RestaurantAddress save(RestaurantAddress restaurantAddress);

    void delete(RestaurantAddressId id);

}
