package tech.challenge.establishment.manager.infrastructure.persistence.mappers;

import tech.challenge.establishment.manager.domain.entities.Restaurant;
import tech.challenge.establishment.manager.domain.valueobjects.KitchenType;
import tech.challenge.establishment.manager.domain.valueobjects.UserId;
import tech.challenge.establishment.manager.infrastructure.persistence.entities.RestaurantJpaEntity;
import tech.challenge.establishment.manager.infrastructure.persistence.entities.RestaurantAddressJpaEntity;

public class RestaurantJpaMapper {

    public static RestaurantJpaEntity toJpaEntity(Restaurant restaurant) {
        if (restaurant == null) return null;

        RestaurantJpaEntity jpaEntity = new RestaurantJpaEntity();

        if (restaurant.getId() != null) {
            jpaEntity.setId(restaurant.getId().value());
        }

        jpaEntity.setName(restaurant.getName().value());
        jpaEntity.setKitchenType(restaurant.getKitchenType().value());
        jpaEntity.setStartOperation(restaurant.getStartOperation());
        jpaEntity.setEndOperation(restaurant.getEndOperation());
        jpaEntity.setOwnerId(restaurant.getOwnerId().value());

        if (restaurant.getRestaurantAddress() != null) {
            jpaEntity.setRestaurantAddress(
                    RestaurantAddressJpaMapper.toJpaEntity(restaurant.getRestaurantAddress(), jpaEntity)
            );
        }

        return jpaEntity;
    }

    public static RestaurantJpaEntity toJpaEntity(Restaurant restaurant, RestaurantJpaEntity existingEntity) {
        if (restaurant == null) return null;

        RestaurantJpaEntity jpaEntity = existingEntity != null ? existingEntity : new RestaurantJpaEntity();

        if (restaurant.getId() != null) {
            jpaEntity.setId(restaurant.getId().value());
        }

        jpaEntity.setName(restaurant.getName().value());
        jpaEntity.setKitchenType(restaurant.getKitchenType().value());
        jpaEntity.setStartOperation(restaurant.getStartOperation());
        jpaEntity.setEndOperation(restaurant.getEndOperation());
        jpaEntity.setOwnerId(restaurant.getOwnerId().value());

        if (restaurant.getRestaurantAddress() != null) {
            RestaurantAddressJpaEntity existingAddress = existingEntity != null ? existingEntity.getRestaurantAddress() : null;
            jpaEntity.setRestaurantAddress(
                    RestaurantAddressJpaMapper.toJpaEntity(restaurant.getRestaurantAddress(), jpaEntity, existingAddress)
            );
        }

        return jpaEntity;
    }

    public static Restaurant toDomainEntity(RestaurantJpaEntity entity) {
        if (entity == null) return null;

        return Restaurant.of(
                entity.getId(),
                entity.getName(),
                RestaurantAddressJpaMapper.toDomainEntity(entity.getRestaurantAddress()),
                KitchenType.of(entity.getKitchenType()),
                entity.getStartOperation(),
                entity.getEndOperation(),
                UserId.of(entity.getOwnerId())
        );
    }
}
