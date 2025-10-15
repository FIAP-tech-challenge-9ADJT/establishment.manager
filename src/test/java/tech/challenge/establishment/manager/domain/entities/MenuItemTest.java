package tech.challenge.establishment.manager.domain.entities;

import org.junit.jupiter.api.Test;
import tech.challenge.establishment.manager.domain.valueobjects.MenuItemId;
import tech.challenge.establishment.manager.domain.valueobjects.RestaurantId;

import static org.junit.jupiter.api.Assertions.*;

class MenuItemTest {

    @Test
    void shouldCreateMenuItemSuccessfully() {
        String name = "Pizza Margherita";
        String description = "Traditional Italian pizza";
        Double price = 25.99;
        String photoUrl = "http://example.com/pizza.jpg";
        RestaurantId restaurantId = RestaurantId.of(1L);

        MenuItem menuItem = MenuItem.create(name, description, price, photoUrl, restaurantId);

        assertNotNull(menuItem);
        assertEquals(name, menuItem.getName().value());
        assertEquals(description, menuItem.getDescription());
        assertEquals(price, menuItem.getPrice());
        assertEquals(photoUrl, menuItem.getPhotoUrl());
        assertEquals(restaurantId, menuItem.getRestaurantId());
        assertNull(menuItem.getId());
    }

    @Test
    void shouldCreateMenuItemWithIdSuccessfully() {
        Long id = 1L;
        String name = "Pizza Margherita";
        String description = "Traditional Italian pizza";
        Double price = 25.99;
        String photoUrl = "http://example.com/pizza.jpg";
        RestaurantId restaurantId = RestaurantId.of(1L);

        MenuItem menuItem = MenuItem.of(id, name, description, price, photoUrl, restaurantId);

        assertNotNull(menuItem);
        assertEquals(MenuItemId.of(id), menuItem.getId());
        assertEquals(name, menuItem.getName().value());
        assertEquals(description, menuItem.getDescription());
        assertEquals(price, menuItem.getPrice());
        assertEquals(photoUrl, menuItem.getPhotoUrl());
        assertEquals(restaurantId, menuItem.getRestaurantId());
    }

    @Test
    void shouldThrowExceptionWhenNameIsBlank() {
        String description = "Traditional Italian pizza";
        Double price = 25.99;
        String photoUrl = "http://example.com/pizza.jpg";
        RestaurantId restaurantId = RestaurantId.of(1L);

        assertThrows(IllegalArgumentException.class, () -> {
            MenuItem.create("", description, price, photoUrl, restaurantId);
        });
    }

    @Test
    void shouldThrowExceptionWhenDescriptionIsNull() {
        String name = "Pizza Margherita";
        Double price = 25.99;
        String photoUrl = "http://example.com/pizza.jpg";
        RestaurantId restaurantId = RestaurantId.of(1L);

        assertThrows(IllegalArgumentException.class, () -> {
            MenuItem.create(name, null, price, photoUrl, restaurantId);
        });
    }

    @Test
    void shouldThrowExceptionWhenDescriptionIsBlank() {
        String name = "Pizza Margherita";
        Double price = 25.99;
        String photoUrl = "http://example.com/pizza.jpg";
        RestaurantId restaurantId = RestaurantId.of(1L);

        assertThrows(IllegalArgumentException.class, () -> {
            MenuItem.create(name, "", price, photoUrl, restaurantId);
        });
    }

    @Test
    void shouldThrowExceptionWhenPriceIsNull() {
        String name = "Pizza Margherita";
        String description = "Traditional Italian pizza";
        String photoUrl = "http://example.com/pizza.jpg";
        RestaurantId restaurantId = RestaurantId.of(1L);

        assertThrows(IllegalArgumentException.class, () -> {
            MenuItem.create(name, description, null, photoUrl, restaurantId);
        });
    }

    @Test
    void shouldThrowExceptionWhenPriceIsZero() {
        String name = "Pizza Margherita";
        String description = "Traditional Italian pizza";
        Double price = 0.0;
        String photoUrl = "http://example.com/pizza.jpg";
        RestaurantId restaurantId = RestaurantId.of(1L);

        assertThrows(IllegalArgumentException.class, () -> {
            MenuItem.create(name, description, price, photoUrl, restaurantId);
        });
    }

    @Test
    void shouldThrowExceptionWhenPriceIsNegative() {
        String name = "Pizza Margherita";
        String description = "Traditional Italian pizza";
        Double price = -10.0;
        String photoUrl = "http://example.com/pizza.jpg";
        RestaurantId restaurantId = RestaurantId.of(1L);

        assertThrows(IllegalArgumentException.class, () -> {
            MenuItem.create(name, description, price, photoUrl, restaurantId);
        });
    }

    @Test
    void shouldThrowExceptionWhenPhotoUrlIsNull() {
        String name = "Pizza Margherita";
        String description = "Traditional Italian pizza";
        Double price = 25.99;
        RestaurantId restaurantId = RestaurantId.of(1L);

        assertThrows(IllegalArgumentException.class, () -> {
            MenuItem.create(name, description, price, null, restaurantId);
        });
    }

    @Test
    void shouldThrowExceptionWhenPhotoUrlIsInvalid() {
        String name = "Pizza Margherita";
        String description = "Traditional Italian pizza";
        Double price = 25.99;
        String photoUrl = "invalid-url";
        RestaurantId restaurantId = RestaurantId.of(1L);

        assertThrows(IllegalArgumentException.class, () -> {
            MenuItem.create(name, description, price, photoUrl, restaurantId);
        });
    }

    @Test
    void shouldThrowExceptionWhenRestaurantIdIsNull() {
        String name = "Pizza Margherita";
        String description = "Traditional Italian pizza";
        Double price = 25.99;
        String photoUrl = "http://example.com/pizza.jpg";

        assertThrows(IllegalArgumentException.class, () -> {
            MenuItem.create(name, description, price, photoUrl, null);
        });
    }

    @Test
    void shouldThrowExceptionWhenIdIsNullInOfMethod() {
        String name = "Pizza Margherita";
        String description = "Traditional Italian pizza";
        Double price = 25.99;
        String photoUrl = "http://example.com/pizza.jpg";
        RestaurantId restaurantId = RestaurantId.of(1L);

        assertThrows(IllegalArgumentException.class, () -> {
            MenuItem.of(null, name, description, price, photoUrl, restaurantId);
        });
    }

    @Test
    void shouldBeEqualWhenSameId() {
        Long id = 1L;
        String name = "Pizza Margherita";
        String description = "Traditional Italian pizza";
        Double price = 25.99;
        String photoUrl = "http://example.com/pizza.jpg";
        RestaurantId restaurantId = RestaurantId.of(1L);

        MenuItem menuItem1 = MenuItem.of(id, name, description, price, photoUrl, restaurantId);
        MenuItem menuItem2 = MenuItem.of(id, "Different Name", "Different description", 30.99, "http://example.com/different.jpg", restaurantId);

        assertEquals(menuItem1, menuItem2);
        assertEquals(menuItem1.hashCode(), menuItem2.hashCode());
    }

    @Test
    void shouldNotBeEqualWhenDifferentId() {
        String name = "Pizza Margherita";
        String description = "Traditional Italian pizza";
        Double price = 25.99;
        String photoUrl = "http://example.com/pizza.jpg";
        RestaurantId restaurantId = RestaurantId.of(1L);

        MenuItem menuItem1 = MenuItem.of(1L, name, description, price, photoUrl, restaurantId);
        MenuItem menuItem2 = MenuItem.of(2L, name, description, price, photoUrl, restaurantId);

        assertNotEquals(menuItem1, menuItem2);
    }

    @Test
    void shouldNotBeEqualWhenComparedWithNull() {
        String name = "Pizza Margherita";
        String description = "Traditional Italian pizza";
        Double price = 25.99;
        String photoUrl = "http://example.com/pizza.jpg";
        RestaurantId restaurantId = RestaurantId.of(1L);

        MenuItem menuItem = MenuItem.of(1L, name, description, price, photoUrl, restaurantId);

        assertNotEquals(menuItem, null);
    }

    @Test
    void shouldNotBeEqualWhenComparedWithDifferentClass() {
        String name = "Pizza Margherita";
        String description = "Traditional Italian pizza";
        Double price = 25.99;
        String photoUrl = "http://example.com/pizza.jpg";
        RestaurantId restaurantId = RestaurantId.of(1L);

        MenuItem menuItem = MenuItem.of(1L, name, description, price, photoUrl, restaurantId);

        assertNotEquals(menuItem, "not a menu item");
    }
}
