package tech.challenge.establishment.manager.infrastructure.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.challenge.establishment.manager.infrastructure.persistence.entities.AddressJpaEntity;
import tech.challenge.establishment.manager.infrastructure.persistence.entities.RestaurantAddressJpaEntity;
import tech.challenge.establishment.manager.infrastructure.persistence.entities.RestaurantJpaEntity;

import java.util.List;

public interface RestaurantJpaRepository extends JpaRepository<RestaurantJpaEntity, Long> {

    List<RestaurantJpaEntity> findByName(String name);

    boolean existsById(Long id);

    List<RestaurantJpaEntity> findByNameContainingIgnoreCase(String name);

    List<RestaurantJpaEntity> findByNameAndRestaurantAddress(String name, RestaurantAddressJpaEntity restaurantAddress);

    boolean existsByNameAndRestaurantAddress(String name, RestaurantAddressJpaEntity restaurantAddress);
}
