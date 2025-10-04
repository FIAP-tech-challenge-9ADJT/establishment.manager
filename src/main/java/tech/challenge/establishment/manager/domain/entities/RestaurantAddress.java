package tech.challenge.establishment.manager.domain.entities;

import tech.challenge.establishment.manager.domain.valueobjects.PostalCode;
import tech.challenge.establishment.manager.domain.valueobjects.RestaurantAddressId;
import tech.challenge.establishment.manager.domain.valueobjects.RestaurantId;

import java.util.Objects;

public class RestaurantAddress {

    private final RestaurantAddressId id;
    private final String street;
    private final String city;
    private final PostalCode postalCode;
    private final String number;
    private final RestaurantId restaurantId;

    public RestaurantAddress(RestaurantAddressId id, String street, String city, PostalCode postalCode, String number, RestaurantId restaurantId) {
        this.id = id;
        this.street = Objects.requireNonNull(street, "Street cannot be null");
        this.city = Objects.requireNonNull(city, "City cannot be null");
        this.postalCode = Objects.requireNonNull(postalCode, "Postal code cannot be null");
        this.number = Objects.requireNonNull(number, "Number cannot be null");
        this.restaurantId = restaurantId;
    }

    public static RestaurantAddress create(String street, String city, String postalCode, String number, RestaurantId restaurantId) {
        return new RestaurantAddress(null, street, city, PostalCode.of(postalCode), number, restaurantId);
    }

    public static RestaurantAddress of(Long id, String street, String city, String postalCode, String number, RestaurantId restaurantId) {
        return new RestaurantAddress(RestaurantAddressId.of(id), street, city, PostalCode.of(postalCode), number, restaurantId);
    }

    public RestaurantAddress updateDetails(String street, String city, String postalCode, String number) {
        return new RestaurantAddress(this.id, street, city, PostalCode.of(postalCode), number, this.restaurantId);
    }

    public RestaurantAddress withUserId(RestaurantId restaurantId) {
        return new RestaurantAddress(this.id, this.street, this.city, this.postalCode, this.number, restaurantId);
    }

    public RestaurantAddressId getId() {
        return id;
    }
    public String getStreet() {
        return street;
    }
    public String getCity() {
        return city;
    }
    public PostalCode getPostalCode() {
        return postalCode;
    }
    public String getNumber() {
        return number;
    }
    public RestaurantId getRestaurantId() {
        return restaurantId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RestaurantAddress that = (RestaurantAddress) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(street, that.street) &&
                Objects.equals(city, that.city) &&
                Objects.equals(postalCode, that.postalCode) &&
                Objects.equals(number, that.number) &&
                Objects.equals(restaurantId, that.restaurantId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, street, city, postalCode, number, restaurantId);
    }

    @Override
    public String toString() {
        return street + ", " + number + " - " + city + " (" + postalCode.value() + ")";
    }
}
