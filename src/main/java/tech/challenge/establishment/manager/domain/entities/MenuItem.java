package tech.challenge.establishment.manager.domain.entities;

import tech.challenge.establishment.manager.domain.valueobjects.MenuItemId;
import tech.challenge.establishment.manager.domain.valueobjects.Name;
import tech.challenge.establishment.manager.domain.valueobjects.RestaurantId;

import java.util.Objects;

public class MenuItem {

    private final MenuItemId id;
    private final Name name;
    private final String description;
    private final Double price;
    private final String photoUrl;
    private final RestaurantId restaurantId;

    private MenuItem(MenuItemId id, Name name, String description, Double price,
                     String photoUrl, RestaurantId restaurantId) {

        if (name == null || name.value().isBlank())
            throw new IllegalArgumentException("Name cannot be empty");
        if (description == null || description.isBlank())
            throw new IllegalArgumentException("Description cannot be empty");
        if (price == null || price <= 0)
            throw new IllegalArgumentException("Price must be positive");
        if (photoUrl == null || !photoUrl.startsWith("http"))
            throw new IllegalArgumentException("PhotoUrl must be a valid URL");
        if (restaurantId == null)
            throw new IllegalArgumentException("RestaurantId cannot be null");

        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.photoUrl = photoUrl;
        this.restaurantId = restaurantId;
    }

    public static MenuItem create(String name, String description, Double price,
                                  String photoUrl, RestaurantId restaurantId) {
        var menuItems = new MenuItem(
                null,
                Name.of(name),
                description,
                price,
                photoUrl,
                restaurantId
        );
        return menuItems;
    }

    public static MenuItem of(Long id, String name, String description, Double price,
                              String photoUrl, RestaurantId restaurantId) {
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null when reconstructing a menuItem");
        }

        return new MenuItem(
                MenuItemId.of(id),
                Name.of(name),
                description,
                price,
                photoUrl,
                restaurantId);
    }

    // Getters

    public MenuItemId getId() {
        return id;
    }
    public Name getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public Double getPrice() {
        return price;
    }
    public String getPhotoUrl() {
        return photoUrl;
    }
    public RestaurantId getRestaurantId() {
        return restaurantId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuItem menuItems = (MenuItem) o;
        return Objects.equals(id, menuItems.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
