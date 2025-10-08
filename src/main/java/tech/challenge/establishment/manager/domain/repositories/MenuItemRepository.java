package tech.challenge.establishment.manager.domain.repositories;

import tech.challenge.establishment.manager.domain.entities.MenuItem;
import tech.challenge.establishment.manager.domain.valueobjects.MenuItemId;
import tech.challenge.establishment.manager.domain.valueobjects.RestaurantId;

import java.util.List;
import java.util.Optional;

public interface MenuItemRepository {

    MenuItem save(MenuItem menuItem);

    Optional<MenuItem> findById(MenuItemId id);

    List<MenuItem> findAll(RestaurantId restaurantId);

    void deleteById(MenuItemId id);

    boolean existsById(MenuItemId id);
}
