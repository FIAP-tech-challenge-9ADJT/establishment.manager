package tech.challenge.establishment.manager.infrastructure.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.challenge.establishment.manager.infrastructure.persistence.entities.RestaurantAddressJpaEntity;

import java.util.Optional;

public interface RestaurantAddressJpaRepository extends JpaRepository<RestaurantAddressJpaEntity, Long> {

    Optional<RestaurantAddressJpaEntity> findByRestaurantId(Long restaurantId);
}
