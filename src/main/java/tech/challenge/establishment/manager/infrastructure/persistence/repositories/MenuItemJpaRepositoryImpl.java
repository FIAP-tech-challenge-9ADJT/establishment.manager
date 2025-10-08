package tech.challenge.establishment.manager.infrastructure.persistence.repositories;

import org.springframework.stereotype.Repository;
import tech.challenge.establishment.manager.domain.entities.MenuItem;
import tech.challenge.establishment.manager.domain.repositories.MenuItemRepository;
import tech.challenge.establishment.manager.domain.valueobjects.MenuItemId;
import tech.challenge.establishment.manager.domain.valueobjects.RestaurantId;
import tech.challenge.establishment.manager.infrastructure.persistence.entities.MenuItemJpaEntity;
import tech.challenge.establishment.manager.infrastructure.persistence.mappers.MenuItemJpaMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class MenuItemJpaRepositoryImpl implements MenuItemRepository {

    private final MenuItemJpaRepository menuItemJpaRepository;

    public MenuItemJpaRepositoryImpl(MenuItemJpaRepository menuItemJpaRepository) {
        this.menuItemJpaRepository = menuItemJpaRepository;
    }

    @Override
    public MenuItem save(MenuItem menuItem) {
        MenuItemJpaEntity existingEntity = menuItem.getId() != null
                ? menuItemJpaRepository.findById(menuItem.getId().value()).orElse(null)
                : null;


        var jpaEntity = MenuItemJpaMapper.toJpaEntity(menuItem, existingEntity);
        var savedEntity = menuItemJpaRepository.save(jpaEntity);
        return MenuItemJpaMapper.toDomainEntity(savedEntity);
    }

    @Override
    public Optional<MenuItem> findById(MenuItemId id) {
        return menuItemJpaRepository.findById(id.value())
                .map(MenuItemJpaMapper::toDomainEntity);
    }

    @Override
    public List<MenuItem> findAll(RestaurantId restaurantId) {
        return menuItemJpaRepository.findByRestaurantId(restaurantId.value())
                .stream()
                .map(MenuItemJpaMapper::toDomainEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(MenuItemId id) {
        menuItemJpaRepository.deleteById(id.value());
    }

    @Override
    public boolean existsById(MenuItemId id) {
        return menuItemJpaRepository.existsById(id.value());
    }

}
