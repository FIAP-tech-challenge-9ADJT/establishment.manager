package tech.challenge.establishment.manager.domain.entities;

import tech.challenge.establishment.manager.domain.valueobjects.AddressId;
import tech.challenge.establishment.manager.domain.valueobjects.PostalCode;
import tech.challenge.establishment.manager.domain.valueobjects.UserId;

import java.util.Objects;

public class Address {
    
    private final AddressId id;
    private final String street;
    private final String city;
    private final PostalCode postalCode;
    private final String number;
    private final UserId userId;
    
    public Address(AddressId id, String street, String city, PostalCode postalCode, String number, UserId userId) {
        this.id = id;
        this.street = Objects.requireNonNull(street, "Street cannot be null");
        this.city = Objects.requireNonNull(city, "City cannot be null");
        this.postalCode = Objects.requireNonNull(postalCode, "Postal code cannot be null");
        this.number = Objects.requireNonNull(number, "Number cannot be null");
        this.userId = userId; // Allow null during creation, will be set after user is saved
    }
    
    public static Address create(String street, String city, String postalCode, String number, UserId userId) {
        return new Address(null, street, city, PostalCode.of(postalCode), number, userId);
    }
    
    public static Address of(Long id, String street, String city, String postalCode, String number, UserId userId) {
        return new Address(AddressId.of(id), street, city, PostalCode.of(postalCode), number, userId);
    }
    
    public Address updateDetails(String street, String city, String postalCode, String number) {
        return new Address(this.id, street, city, PostalCode.of(postalCode), number, this.userId);
    }
    
    public Address withUserId(UserId userId) {
        return new Address(this.id, this.street, this.city, this.postalCode, this.number, userId);
    }
    
    // Getters
    public AddressId getId() { return id; }
    public String getStreet() { return street; }
    public String getCity() { return city; }
    public PostalCode getPostalCode() { return postalCode; }
    public String getNumber() { return number; }
    public UserId getUserId() { return userId; }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(id, address.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return street + ", " + number + " - " + city + " (" + postalCode.value() + ")";
    }
}