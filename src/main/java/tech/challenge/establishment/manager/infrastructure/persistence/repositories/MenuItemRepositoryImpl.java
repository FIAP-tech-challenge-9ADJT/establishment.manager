package tech.challenge.establishment.manager.infrastructure.persistence.repositories;

import org.springframework.stereotype.Repository;
import tech.challenge.establishment.manager.domain.entities.MenuItem;
import tech.challenge.establishment.manager.domain.repositories.MenuItemRepository;
import tech.challenge.establishment.manager.domain.valueobjects.MenuItemId;
import tech.challenge.establishment.manager.domain.valueobjects.RestaurantId;
import tech.challenge.establishment.manager.infrastructure.persistence.entities.RestaurantJpaEntity;
import tech.challenge.establishment.manager.infrastructure.persistence.mappers.MenuItemJpaMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class MenuItemRepositoryImpl implements MenuItemRepository {

    private final MenuItemJpaRepository menuItemJpaRepository;

    public MenuItemRepositoryImpl(MenuItemJpaRepository menuItemJpaRepository) {
        this.menuItemJpaRepository = menuItemJpaRepository;
    }

    @Override
    public MenuItem save(MenuItem menuItem) {
        var existingEntity = menuItem.getId() != null
                ? menuItemJpaRepository.findById(menuItem.getId().value()).orElse(null)
                : null;

        RestaurantJpaEntity restaurantEntity = new RestaurantJpaEntity();
        restaurantEntity.setId(menuItem.getRestaurantId().value());

        var jpaEntity = MenuItemJpaMapper.toJpaEntity(menuItem, restaurantEntity, existingEntity);

        var savedEntity = menuItemJpaRepository.save(jpaEntity);

        return MenuItemJpaMapper.toDomainEntity(savedEntity);
    }

    @Override
    public Optional<MenuItem> findById(MenuItemId id) {
        return menuItemJpaRepository.findById(id.value())
                .map(MenuItemJpaMapper::toDomainEntity);
    }

    @Override
    public List<MenuItem> findAllByRestaurant(RestaurantId restaurantId) {
        return menuItemJpaRepository.findAllByRestaurant_Id(restaurantId.value())
                .stream()
                .map(MenuItemJpaMapper::toDomainEntity)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsByNameAndRestaurant(String name, RestaurantId restaurantId) {
        return menuItemJpaRepository.existsByNameAndRestaurant_Id(name, restaurantId.value());
    }

    @Override
    public void deleteById(MenuItemId id) {
        menuItemJpaRepository.deleteById(id.value());
    }
}