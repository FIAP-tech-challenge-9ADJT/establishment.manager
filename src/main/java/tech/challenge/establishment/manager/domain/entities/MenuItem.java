package tech.challenge.establishment.manager.domain.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import tech.challenge.establishment.manager.domain.valueobjects.MenuItemId;
import tech.challenge.establishment.manager.domain.valueobjects.Price;
import tech.challenge.establishment.manager.domain.valueobjects.RestaurantId;

@Data
@NoArgsConstructor
public class MenuItem {

    private MenuItemId id;
    private String name;
    private String description;
    private Price price;
    private boolean onlyInRestaurant;
    private String photoPath;
    private RestaurantId restaurantId;

    private MenuItem(MenuItemId id, String name, String description, Price price,
                     boolean onlyInRestaurant, String photoPath, RestaurantId restaurantId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.onlyInRestaurant = onlyInRestaurant;
        this.photoPath = photoPath;
        this.restaurantId = restaurantId;
    }

    public static MenuItem create(String name, String description, Price price,
                                  boolean onlyInRestaurant, String photoPath, RestaurantId restaurantId) {
        return new MenuItem(
                null,
                name,
                description,
                price,
                onlyInRestaurant,
                photoPath,
                restaurantId);
    }

    public static MenuItem of(Long id, String name, String description, Price price,
                              boolean onlyInRestaurant, String photoPath, RestaurantId restaurantId) {
        return new MenuItem(
                MenuItemId.of(id),
                name,
                description,
                price,
                onlyInRestaurant,
                photoPath,
                restaurantId);
    }

    public MenuItem updateName(String newName) {
        this.name = newName;
        return this;
    }

    public MenuItem updateDescription(String newDescription) {
        this.description = newDescription;
        return this;
    }

    public MenuItem updatePrice(Price newPrice) {
        this.price = newPrice;
        return this;
    }

    public MenuItem updateOnlyInRestaurant(boolean onlyInRestaurant) {
        this.onlyInRestaurant = onlyInRestaurant;
        return this;
    }

    public MenuItem updatePhotoPath(String photoPath) {
        this.photoPath = photoPath;
        return this;
    }
}
