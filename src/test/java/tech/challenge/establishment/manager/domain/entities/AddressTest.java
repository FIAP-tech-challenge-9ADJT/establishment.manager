package tech.challenge.establishment.manager.domain.entities;

import org.junit.jupiter.api.Test;
import tech.challenge.establishment.manager.domain.valueobjects.AddressId;
import tech.challenge.establishment.manager.domain.valueobjects.PostalCode;
import tech.challenge.establishment.manager.domain.valueobjects.UserId;

import static org.junit.jupiter.api.Assertions.*;

class AddressTest {

    @Test
    void shouldCreateAddressSuccessfully() {
        Address address = Address.create("Rua A", "São Paulo", "01234567", "100", null);

        assertNotNull(address);
        assertEquals("Rua A", address.getStreet());
        assertEquals("São Paulo", address.getCity());
        assertEquals("01234567", address.getPostalCode().value());
        assertEquals("100", address.getNumber());
        assertNull(address.getUserId());
        assertNull(address.getId());
    }

    @Test
    void shouldCreateAddressWithId() {
        UserId userId = UserId.of(1L);
        Address address = Address.of(1L, "Rua B", "Rio", "12345678", "200", userId);

        assertNotNull(address);
        assertNotNull(address.getId());
        assertEquals(1L, address.getId().value());
        assertEquals("Rua B", address.getStreet());
        assertEquals("Rio", address.getCity());
        assertEquals("12345678", address.getPostalCode().value());
        assertEquals("200", address.getNumber());
        assertEquals(userId, address.getUserId());
    }

    @Test
    void shouldUpdateAddressDetails() {
        Address original = Address.of(1L, "Rua A", "São Paulo", "01234567", "100", UserId.of(1L));
        Address updated = original.updateDetails("Rua B", "Rio", "87654321", "200");

        assertEquals(original.getId(), updated.getId());
        assertEquals("Rua B", updated.getStreet());
        assertEquals("Rio", updated.getCity());
        assertEquals("87654321", updated.getPostalCode().value());
        assertEquals("200", updated.getNumber());
        assertEquals(original.getUserId(), updated.getUserId());
    }

    @Test
    void shouldSetUserId() {
        Address address = Address.create("Rua A", "São Paulo", "01234567", "100", null);
        UserId userId = UserId.of(1L);
        
        Address withUserId = address.withUserId(userId);

        assertNotNull(withUserId);
        assertEquals(userId, withUserId.getUserId());
        assertEquals(address.getStreet(), withUserId.getStreet());
    }

    @Test
    void shouldThrowExceptionWhenStreetIsNull() {
        assertThrows(NullPointerException.class, () -> {
            Address.create(null, "São Paulo", "01234567", "100", null);
        });
    }

    @Test
    void shouldThrowExceptionWhenCityIsNull() {
        assertThrows(NullPointerException.class, () -> {
            Address.create("Rua A", null, "01234567", "100", null);
        });
    }

    @Test
    void shouldThrowExceptionWhenPostalCodeIsNull() {
        assertThrows(NullPointerException.class, () -> {
            Address.create("Rua A", "São Paulo", null, "100", null);
        });
    }

    @Test
    void shouldThrowExceptionWhenNumberIsNull() {
        assertThrows(NullPointerException.class, () -> {
            Address.create("Rua A", "São Paulo", "01234567", null, null);
        });
    }

    @Test
    void shouldBeEqualWhenSameId() {
        Address address1 = Address.of(1L, "Rua A", "São Paulo", "01234567", "100", null);
        Address address2 = Address.of(1L, "Rua B", "Rio", "87654321", "200", null);

        assertEquals(address1, address2);
        assertEquals(address1.hashCode(), address2.hashCode());
    }

    @Test
    void shouldNotBeEqualWhenDifferentId() {
        Address address1 = Address.of(1L, "Rua A", "São Paulo", "01234567", "100", null);
        Address address2 = Address.of(2L, "Rua A", "São Paulo", "01234567", "100", null);

        assertNotEquals(address1, address2);
    }

    @Test
    void shouldBeEqualToItself() {
        Address address = Address.of(1L, "Rua A", "São Paulo", "01234567", "100", null);

        assertEquals(address, address);
    }

    @Test
    void shouldNotBeEqualToNull() {
        Address address = Address.of(1L, "Rua A", "São Paulo", "01234567", "100", null);

        assertNotEquals(address, null);
    }

    @Test
    void shouldNotBeEqualToDifferentClass() {
        Address address = Address.of(1L, "Rua A", "São Paulo", "01234567", "100", null);

        assertNotEquals(address, "String");
    }

    @Test
    void shouldGenerateCorrectToString() {
        Address address = Address.create("Rua A", "São Paulo", "01234567", "100", null);
        String toString = address.toString();

        assertTrue(toString.contains("Rua A"));
        assertTrue(toString.contains("100"));
        assertTrue(toString.contains("São Paulo"));
        assertTrue(toString.contains("01234567"));
    }
}
