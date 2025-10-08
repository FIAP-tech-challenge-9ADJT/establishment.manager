package tech.challenge.establishment.manager.infrastructure.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.challenge.establishment.manager.domain.valueobjects.RestaurantId;
import tech.challenge.establishment.manager.infrastructure.persistence.entities.MenuItemJpaEntity;

import java.util.List;

public interface MenuItemJpaRepository extends JpaRepository<MenuItemJpaEntity, Long> {

    List<MenuItemJpaEntity> findByRestaurantId(Long restaurantId);

}
