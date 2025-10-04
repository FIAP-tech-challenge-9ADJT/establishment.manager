package tech.challenge.establishment.manager.domain.entities;

import tech.challenge.establishment.manager.domain.valueobjects.*;

import java.time.LocalTime;
import java.util.Objects;

public class Restaurant {

    private final RestaurantId id;
    private final Name name;
    private final RestaurantAddress restaurantAddress;
    private final KitchenType kitchenType;
    private final LocalTime startOperation;
    private final LocalTime endOperation;
    private final UserId ownerId;

    private Restaurant(RestaurantId id, Name name, RestaurantAddress restaurantAddress, KitchenType kitchenType,
                       LocalTime startOperation, LocalTime endOperation, UserId ownerId) {
        if (name == null) throw new IllegalArgumentException("Name cannot be null");
        if (restaurantAddress == null) throw new IllegalArgumentException("Address cannot be null");
        if (kitchenType == null) throw new IllegalArgumentException("CuisineType cannot be null");
        if (startOperation == null) throw new IllegalArgumentException("StartOperation cannot be null");
        if (endOperation == null) throw new IllegalArgumentException("EndOperation cannot be null");
        if (ownerId == null) throw new IllegalArgumentException("OwnerId cannot be null");

        this.id = id;
        this.name = name;
        this.restaurantAddress = restaurantAddress;
        this.kitchenType = kitchenType;
        this.startOperation = startOperation;
        this.endOperation = endOperation;
        this.ownerId = ownerId;
    }

    public static Restaurant create(String name, RestaurantAddress restaurantAddress, KitchenType kitchenType,
                                    LocalTime startOperation, LocalTime endOperation, UserId ownerId) {
        var restaurant = new Restaurant(
                null,
                Name.of(name),
                restaurantAddress,
                kitchenType,
                startOperation,
                endOperation,
                ownerId
        );
        return restaurant;
    }

    public static Restaurant of(Long id, String name, RestaurantAddress restaurantAddress, KitchenType kitchenType,
                                LocalTime startOperation, LocalTime endOperation, UserId ownerId) {
        if (id == null) {
            throw new IllegalArgumentException("Id cannot be null when reconstructing a restaurant");
        }
        return new Restaurant(
                RestaurantId.of(id),
                Name.of(name),
                restaurantAddress,
                kitchenType,
                startOperation,
                endOperation,
                ownerId
        );
    }

    // Métodos de negócio
    public Restaurant updateName(String newName) {
        ensurePersisted();
        return new Restaurant(this.id, Name.of(newName), this.restaurantAddress,
                this.kitchenType, this.startOperation, this.endOperation, this.ownerId);
    }

    public Restaurant updateRestaurantAddress(RestaurantAddress newRestaurantAddress) {
        ensurePersisted();
        return new Restaurant(this.id, this.name, newRestaurantAddress,
                this.kitchenType, this.startOperation, this.endOperation, this.ownerId);
    }

    public Restaurant updateOperatingHours(LocalTime newStartOperation, LocalTime newEndOperation) {
        ensurePersisted();
        return new Restaurant(this.id, this.name, this.restaurantAddress,
                this.kitchenType, newStartOperation, newEndOperation, this.ownerId);
    }

    public Restaurant updateKitchenType(KitchenType newKitchenType) {
        ensurePersisted();
        return new Restaurant(this.id, this.name, this.restaurantAddress, newKitchenType,
                this.startOperation, this.endOperation, this.ownerId);
    }

    private void ensurePersisted() {
        if (this.id == null) {
            throw new IllegalStateException("Cannot update a restaurant without an ID");
        }
    }

    // Getters
    public RestaurantId getId() {
        return id;
    }
    public Name getName() {
        return name;
    }
    public RestaurantAddress getRestaurantAddress() {
        return restaurantAddress;
    }
    public KitchenType getKitchenType() {
        return kitchenType;
    }
    public LocalTime getStartOperation() {
        return startOperation;
    }
    public LocalTime getEndOperation() {
        return endOperation;
    }
    public UserId getOwnerId() {
        return ownerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Restaurant restaurant = (Restaurant) o;
        return Objects.equals(id, restaurant.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
