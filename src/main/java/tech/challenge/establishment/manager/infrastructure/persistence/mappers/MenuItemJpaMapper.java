package tech.challenge.establishment.manager.infrastructure.persistence.mappers;

import tech.challenge.establishment.manager.domain.entities.MenuItem;
import tech.challenge.establishment.manager.domain.valueobjects.RestaurantId;
import tech.challenge.establishment.manager.infrastructure.persistence.entities.MenuItemJpaEntity;
import tech.challenge.establishment.manager.infrastructure.persistence.entities.RestaurantJpaEntity;

public class MenuItemJpaMapper {

    public static MenuItemJpaEntity toJpaEntity(MenuItem menuItem, RestaurantJpaEntity restaurantEntity, MenuItemJpaEntity existingEntity) {
        if (menuItem == null) return null;

        MenuItemJpaEntity jpaEntity = existingEntity != null ? existingEntity : new MenuItemJpaEntity();

        if (menuItem.getId() != null) {
            jpaEntity.setId(menuItem.getId().value());
        }

        jpaEntity.setName(menuItem.getName().value());
        jpaEntity.setDescription(menuItem.getDescription());
        jpaEntity.setPrice(menuItem.getPrice());
        jpaEntity.setPhotoUrl(menuItem.getPhotoUrl());
        jpaEntity.setRestaurant(restaurantEntity);

        return jpaEntity;
    }

    public static MenuItem toDomainEntity(MenuItemJpaEntity entity) {
        if (entity == null) return null;

        return MenuItem.of(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getPrice(),
                entity.getPhotoUrl(),
                RestaurantId.of(entity.getRestaurant().getId())
        );
    }
}

