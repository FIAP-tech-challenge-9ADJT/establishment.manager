package tech.challenge.establishment.manager.infrastructure.persistence.mappers;

import tech.challenge.establishment.manager.domain.entities.MenuItem;
import tech.challenge.establishment.manager.domain.valueobjects.Price;
import tech.challenge.establishment.manager.domain.valueobjects.RestaurantId;
import tech.challenge.establishment.manager.infrastructure.persistence.entities.MenuItemJpaEntity;
import tech.challenge.establishment.manager.infrastructure.persistence.entities.RestaurantJpaEntity;

public class MenuItemJpaMapper {

    /**
     * Converte do domínio para a entidade JPA
     */
    public static MenuItemJpaEntity toJpaEntity(MenuItem menuItem, MenuItemJpaEntity existingEntity) {
        if (menuItem == null) return null;

        MenuItemJpaEntity jpaEntity = existingEntity != null ? existingEntity : new MenuItemJpaEntity();

        if (menuItem.getId() != null) {
            jpaEntity.setId(menuItem.getId().value());
        }

        jpaEntity.setName(menuItem.getName());
        jpaEntity.setDescription(menuItem.getDescription());
        jpaEntity.setPrice(menuItem.getPrice().value());
        jpaEntity.setOnlyInRestaurant(menuItem.isOnlyInRestaurant());
        jpaEntity.setPhotoPath(menuItem.getPhotoPath());

        // Associa apenas o ID do restaurante
        if (menuItem.getRestaurantId() != null) {
            RestaurantJpaEntity restaurantEntity = new RestaurantJpaEntity();
            restaurantEntity.setId(menuItem.getRestaurantId().value());
            jpaEntity.setRestaurant(restaurantEntity);
        }

        return jpaEntity;
    }

    /**
     * Converte da entidade JPA para o domínio
     */
    public static MenuItem toDomainEntity(MenuItemJpaEntity jpaEntity) {
        if (jpaEntity == null) return null;

        Price price = Price.of(jpaEntity.getPrice());
        RestaurantId restaurantId = null;

        if (jpaEntity.getRestaurant() != null && jpaEntity.getRestaurant().getId() != null) {
            restaurantId = new RestaurantId(jpaEntity.getRestaurant().getId());
        }

        return MenuItem.of(
                jpaEntity.getId(),
                jpaEntity.getName(),
                jpaEntity.getDescription(),
                price,
                jpaEntity.isOnlyInRestaurant(),
                jpaEntity.getPhotoPath(),
                restaurantId
        );
    }
}
