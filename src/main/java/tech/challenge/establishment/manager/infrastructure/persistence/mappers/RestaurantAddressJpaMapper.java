package tech.challenge.establishment.manager.infrastructure.persistence.mappers;

import tech.challenge.establishment.manager.domain.entities.RestaurantAddress;
import tech.challenge.establishment.manager.domain.valueobjects.RestaurantId;
import tech.challenge.establishment.manager.infrastructure.persistence.entities.RestaurantAddressJpaEntity;
import tech.challenge.establishment.manager.infrastructure.persistence.entities.RestaurantJpaEntity;

public class RestaurantAddressJpaMapper {

    public static RestaurantAddressJpaEntity toJpaEntity(RestaurantAddress restaurantAddress) {
        return toJpaEntity(restaurantAddress, null);
    }

    public static RestaurantAddressJpaEntity toJpaEntity(RestaurantAddress restaurantAddress, RestaurantJpaEntity restaurantJpaEntity) {
        return toJpaEntity(restaurantAddress, restaurantJpaEntity, null);
    }

    public static RestaurantAddressJpaEntity toJpaEntity(RestaurantAddress restaurantAddress, RestaurantJpaEntity restaurantJpaEntity, RestaurantAddressJpaEntity existingEntity) {
        if (restaurantAddress == null) return null;

        RestaurantAddressJpaEntity jpaEntity = existingEntity != null ? existingEntity : new RestaurantAddressJpaEntity();

        if (restaurantAddress.getId() != null) {
            jpaEntity.setId(restaurantAddress.getId().value());
        }
        jpaEntity.setStreet(restaurantAddress.getStreet());
        jpaEntity.setCity(restaurantAddress.getCity());
        jpaEntity.setPostalCode(restaurantAddress.getPostalCode().value());
        jpaEntity.setNumber(restaurantAddress.getNumber());

        if (restaurantJpaEntity != null) {
            jpaEntity.setRestaurant(restaurantJpaEntity);
        }

        return jpaEntity;
    }

    public static RestaurantAddress toDomainEntity(RestaurantAddressJpaEntity jpaEntity) {
        if (jpaEntity == null) return null;

        RestaurantId restaurantId = jpaEntity.getRestaurant() != null ?
                RestaurantId.of(jpaEntity.getRestaurant().getId()) : null;

        if (jpaEntity.getId() != null) {
            return RestaurantAddress.of(
                    jpaEntity.getId(),
                    jpaEntity.getStreet(),
                    jpaEntity.getCity(),
                    jpaEntity.getPostalCode(),
                    jpaEntity.getNumber(),
                    restaurantId
            );
        } else {
            return RestaurantAddress.create(
                    jpaEntity.getStreet(),
                    jpaEntity.getCity(),
                    jpaEntity.getPostalCode(),
                    jpaEntity.getNumber(),
                    restaurantId
            );
        }
    }
}
