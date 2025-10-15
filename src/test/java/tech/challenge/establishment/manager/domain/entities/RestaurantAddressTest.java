package tech.challenge.establishment.manager.domain.entities;

import org.junit.jupiter.api.Test;
import tech.challenge.establishment.manager.domain.valueobjects.PostalCode;
import tech.challenge.establishment.manager.domain.valueobjects.RestaurantAddressId;
import tech.challenge.establishment.manager.domain.valueobjects.RestaurantId;

import static org.junit.jupiter.api.Assertions.*;

class RestaurantAddressTest {

    @Test
    void shouldCreateRestaurantAddressSuccessfully() {
        String street = "Rua das Flores";
        String city = "São Paulo";
        String postalCode = "01234567";
        String number = "123";
        RestaurantId restaurantId = RestaurantId.of(1L);

        RestaurantAddress address = RestaurantAddress.create(street, city, postalCode, number, restaurantId);

        assertNotNull(address);
        assertEquals(street, address.getStreet());
        assertEquals(city, address.getCity());
        assertEquals(PostalCode.of(postalCode), address.getPostalCode());
        assertEquals(number, address.getNumber());
        assertEquals(restaurantId, address.getRestaurantId());
        assertNull(address.getId());
    }

    @Test
    void shouldCreateRestaurantAddressWithIdSuccessfully() {
        Long id = 1L;
        String street = "Rua das Flores";
        String city = "São Paulo";
        String postalCode = "01234567";
        String number = "123";
        RestaurantId restaurantId = RestaurantId.of(1L);

        RestaurantAddress address = RestaurantAddress.of(id, street, city, postalCode, number, restaurantId);

        assertNotNull(address);
        assertEquals(RestaurantAddressId.of(id), address.getId());
        assertEquals(street, address.getStreet());
        assertEquals(city, address.getCity());
        assertEquals(PostalCode.of(postalCode), address.getPostalCode());
        assertEquals(number, address.getNumber());
        assertEquals(restaurantId, address.getRestaurantId());
    }

    @Test
    void shouldThrowExceptionWhenStreetIsNull() {
        String city = "São Paulo";
        String postalCode = "01234567";
        String number = "123";
        RestaurantId restaurantId = RestaurantId.of(1L);

        assertThrows(NullPointerException.class, () -> {
            RestaurantAddress.create(null, city, postalCode, number, restaurantId);
        });
    }

    @Test
    void shouldThrowExceptionWhenCityIsNull() {
        String street = "Rua das Flores";
        String postalCode = "01234567";
        String number = "123";
        RestaurantId restaurantId = RestaurantId.of(1L);

        assertThrows(NullPointerException.class, () -> {
            RestaurantAddress.create(street, null, postalCode, number, restaurantId);
        });
    }

    @Test
    void shouldThrowExceptionWhenNumberIsNull() {
        String street = "Rua das Flores";
        String city = "São Paulo";
        String postalCode = "01234567";
        RestaurantId restaurantId = RestaurantId.of(1L);

        assertThrows(NullPointerException.class, () -> {
            RestaurantAddress.create(street, city, postalCode, null, restaurantId);
        });
    }

    @Test
    void shouldThrowExceptionWhenPostalCodeIsInvalid() {
        String street = "Rua das Flores";
        String city = "São Paulo";
        String postalCode = "123";
        String number = "123";
        RestaurantId restaurantId = RestaurantId.of(1L);

        assertThrows(IllegalArgumentException.class, () -> {
            RestaurantAddress.create(street, city, postalCode, number, restaurantId);
        });
    }

    @Test
    void shouldUpdateDetailsSuccessfully() {
        String street = "Rua das Flores";
        String city = "São Paulo";
        String postalCode = "01234567";
        String number = "123";
        RestaurantId restaurantId = RestaurantId.of(1L);

        RestaurantAddress address = RestaurantAddress.of(1L, street, city, postalCode, number, restaurantId);

        String newStreet = "Rua Nova";
        String newCity = "Rio de Janeiro";
        String newPostalCode = "87654321";
        String newNumber = "456";

        RestaurantAddress updatedAddress = address.updateDetails(newStreet, newCity, newPostalCode, newNumber);

        assertEquals(address.getId(), updatedAddress.getId());
        assertEquals(newStreet, updatedAddress.getStreet());
        assertEquals(newCity, updatedAddress.getCity());
        assertEquals(PostalCode.of(newPostalCode), updatedAddress.getPostalCode());
        assertEquals(newNumber, updatedAddress.getNumber());
        assertEquals(restaurantId, updatedAddress.getRestaurantId());
    }

    @Test
    void shouldUpdateRestaurantIdSuccessfully() {
        String street = "Rua das Flores";
        String city = "São Paulo";
        String postalCode = "01234567";
        String number = "123";
        RestaurantId restaurantId = RestaurantId.of(1L);

        RestaurantAddress address = RestaurantAddress.of(1L, street, city, postalCode, number, restaurantId);

        RestaurantId newRestaurantId = RestaurantId.of(2L);
        RestaurantAddress updatedAddress = address.withUserId(newRestaurantId);

        assertEquals(address.getId(), updatedAddress.getId());
        assertEquals(street, updatedAddress.getStreet());
        assertEquals(city, updatedAddress.getCity());
        assertEquals(PostalCode.of(postalCode), updatedAddress.getPostalCode());
        assertEquals(number, updatedAddress.getNumber());
        assertEquals(newRestaurantId, updatedAddress.getRestaurantId());
    }

    @Test
    void shouldBeEqualWhenAllFieldsAreEqual() {
        Long id = 1L;
        String street = "Rua das Flores";
        String city = "São Paulo";
        String postalCode = "01234567";
        String number = "123";
        RestaurantId restaurantId = RestaurantId.of(1L);

        RestaurantAddress address1 = RestaurantAddress.of(id, street, city, postalCode, number, restaurantId);
        RestaurantAddress address2 = RestaurantAddress.of(id, street, city, postalCode, number, restaurantId);

        assertEquals(address1, address2);
        assertEquals(address1.hashCode(), address2.hashCode());
    }

    @Test
    void shouldNotBeEqualWhenFieldsAreDifferent() {
        String street = "Rua das Flores";
        String city = "São Paulo";
        String postalCode = "01234567";
        String number = "123";
        RestaurantId restaurantId = RestaurantId.of(1L);

        RestaurantAddress address1 = RestaurantAddress.of(1L, street, city, postalCode, number, restaurantId);
        RestaurantAddress address2 = RestaurantAddress.of(2L, street, city, postalCode, number, restaurantId);

        assertNotEquals(address1, address2);
    }

    @Test
    void shouldNotBeEqualWhenComparedWithNull() {
        String street = "Rua das Flores";
        String city = "São Paulo";
        String postalCode = "01234567";
        String number = "123";
        RestaurantId restaurantId = RestaurantId.of(1L);

        RestaurantAddress address = RestaurantAddress.of(1L, street, city, postalCode, number, restaurantId);

        assertNotEquals(address, null);
    }

    @Test
    void shouldNotBeEqualWhenComparedWithDifferentClass() {
        String street = "Rua das Flores";
        String city = "São Paulo";
        String postalCode = "01234567";
        String number = "123";
        RestaurantId restaurantId = RestaurantId.of(1L);

        RestaurantAddress address = RestaurantAddress.of(1L, street, city, postalCode, number, restaurantId);

        assertNotEquals(address, "not an address");
    }

    @Test
    void shouldGenerateCorrectToString() {
        String street = "Rua das Flores";
        String city = "São Paulo";
        String postalCode = "01234567";
        String number = "123";
        RestaurantId restaurantId = RestaurantId.of(1L);

        RestaurantAddress address = RestaurantAddress.of(1L, street, city, postalCode, number, restaurantId);

        String expected = "Rua das Flores, 123 - São Paulo (01234567)";
        assertEquals(expected, address.toString());
    }
}

