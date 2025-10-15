package tech.challenge.establishment.manager.domain.entities;

import org.junit.jupiter.api.Test;
import tech.challenge.establishment.manager.domain.valueobjects.KitchenType;
import tech.challenge.establishment.manager.domain.valueobjects.UserId;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class RestaurantTest {

    @Test
    void shouldCreateRestaurantSuccessfully() {
        RestaurantAddress address = RestaurantAddress.create("Rua A", "São Paulo", "01234567", "100", null);
        Restaurant restaurant = Restaurant.create("Pizzaria", address, KitchenType.of("Italian"), 
            LocalTime.of(11, 0), LocalTime.of(23, 0), UserId.of(1L));

        assertNotNull(restaurant);
        assertEquals("Pizzaria", restaurant.getName().value());
        assertEquals("Italian", restaurant.getKitchenType().value());
    }

    @Test
    void shouldCreateRestaurantFromExisting() {
        RestaurantAddress address = RestaurantAddress.create("Rua B", "Rio", "12345678", "200", null);
        Restaurant restaurant = Restaurant.of(1L, "Hamburgeria", address, KitchenType.of("Brazilian"), 
            LocalTime.of(12, 0), LocalTime.of(22, 0), UserId.of(1L));

        assertNotNull(restaurant);
        assertEquals(1L, restaurant.getId().value());
    }

    @Test
    void shouldUpdateRestaurantName() {
        RestaurantAddress address = RestaurantAddress.create("Rua C", "Brasília", "23456789", "300", null);
        Restaurant restaurant = Restaurant.of(1L, "Old Name", address, KitchenType.of("Japanese"), 
            LocalTime.of(10, 0), LocalTime.of(22, 0), UserId.of(1L));

        Restaurant updated = restaurant.updateName("New Name");

        assertEquals("New Name", updated.getName().value());
        assertEquals(restaurant.getId(), updated.getId());
    }

    @Test
    void shouldUpdateRestaurantAddress() {
        RestaurantAddress oldAddress = RestaurantAddress.create("Old St", "Old City", "11111111", "100", null);
        Restaurant restaurant = Restaurant.of(1L, "Restaurant", oldAddress, KitchenType.of("Mexican"), 
            LocalTime.of(11, 0), LocalTime.of(23, 0), UserId.of(1L));

        RestaurantAddress newAddress = RestaurantAddress.create("New St", "New City", "22222222", "200", null);
        Restaurant updated = restaurant.updateRestaurantAddress(newAddress);

        assertEquals("New St", updated.getRestaurantAddress().getStreet());
    }

    @Test
    void shouldUpdateOperatingHours() {
        RestaurantAddress address = RestaurantAddress.create("Rua D", "Salvador", "34567890", "400", null);
        Restaurant restaurant = Restaurant.of(1L, "Restaurant", address, KitchenType.of("Chinese"), 
            LocalTime.of(11, 0), LocalTime.of(22, 0), UserId.of(1L));

        Restaurant updated = restaurant.updateOperatingHours(LocalTime.of(12, 0), LocalTime.of(23, 30));

        assertEquals(LocalTime.of(12, 0), updated.getStartOperation());
        assertEquals(LocalTime.of(23, 30), updated.getEndOperation());
    }

    @Test
    void shouldUpdateKitchenType() {
        RestaurantAddress address = RestaurantAddress.create("Rua E", "Fortaleza", "45678901", "500", null);
        Restaurant restaurant = Restaurant.of(1L, "Restaurant", address, KitchenType.of("Indian"), 
            LocalTime.of(11, 0), LocalTime.of(22, 0), UserId.of(1L));

        Restaurant updated = restaurant.updateKitchenType(KitchenType.of("Japanese"));

        assertEquals("Japanese", updated.getKitchenType().value());
    }

    @Test
    void shouldThrowExceptionWhenUpdatingWithoutId() {
        RestaurantAddress address = RestaurantAddress.create("Rua F", "Curitiba", "56789012", "600", null);
        Restaurant restaurant = Restaurant.create("Restaurant", address, KitchenType.of("French"), 
            LocalTime.of(11, 0), LocalTime.of(22, 0), UserId.of(1L));

        assertThrows(IllegalStateException.class, () -> restaurant.updateName("New Name"));
    }

    @Test
    void shouldThrowExceptionWhenNameIsNull() {
        RestaurantAddress address = RestaurantAddress.create("Rua G", "Recife", "67890123", "700", null);
        
        assertThrows(NullPointerException.class, () -> {
            Restaurant.create(null, address, KitchenType.of("Italian"), 
                LocalTime.of(11, 0), LocalTime.of(22, 0), UserId.of(1L));
        });
    }

    @Test
    void shouldThrowExceptionWhenAddressIsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            Restaurant.create("Restaurant", null, KitchenType.of("Italian"), 
                LocalTime.of(11, 0), LocalTime.of(22, 0), UserId.of(1L));
        });
    }

    @Test
    void shouldThrowExceptionWhenKitchenTypeIsNull() {
        RestaurantAddress address = RestaurantAddress.create("Rua H", "Manaus", "78901234", "800", null);
        
        assertThrows(IllegalArgumentException.class, () -> {
            Restaurant.create("Restaurant", address, null, 
                LocalTime.of(11, 0), LocalTime.of(22, 0), UserId.of(1L));
        });
    }

    @Test
    void shouldThrowExceptionWhenStartOperationIsNull() {
        RestaurantAddress address = RestaurantAddress.create("Rua I", "Belém", "89012345", "900", null);
        
        assertThrows(IllegalArgumentException.class, () -> {
            Restaurant.create("Restaurant", address, KitchenType.of("Italian"), 
                null, LocalTime.of(22, 0), UserId.of(1L));
        });
    }

    @Test
    void shouldThrowExceptionWhenEndOperationIsNull() {
        RestaurantAddress address = RestaurantAddress.create("Rua J", "Goiânia", "90123456", "1000", null);
        
        assertThrows(IllegalArgumentException.class, () -> {
            Restaurant.create("Restaurant", address, KitchenType.of("Italian"), 
                LocalTime.of(11, 0), null, UserId.of(1L));
        });
    }

    @Test
    void shouldThrowExceptionWhenOwnerIdIsNull() {
        RestaurantAddress address = RestaurantAddress.create("Rua K", "Vitória", "01234568", "1100", null);
        
        assertThrows(IllegalArgumentException.class, () -> {
            Restaurant.create("Restaurant", address, KitchenType.of("Italian"), 
                LocalTime.of(11, 0), LocalTime.of(22, 0), null);
        });
    }

    @Test
    void shouldHaveCorrectEquality() {
        RestaurantAddress address = RestaurantAddress.create("Rua L", "Natal", "12345679", "1200", null);
        Restaurant restaurant1 = Restaurant.of(1L, "Restaurant", address, KitchenType.of("Italian"), 
            LocalTime.of(11, 0), LocalTime.of(22, 0), UserId.of(1L));
        Restaurant restaurant2 = Restaurant.of(1L, "Restaurant", address, KitchenType.of("Italian"), 
            LocalTime.of(11, 0), LocalTime.of(22, 0), UserId.of(1L));
        Restaurant restaurant3 = Restaurant.of(2L, "Restaurant", address, KitchenType.of("Italian"), 
            LocalTime.of(11, 0), LocalTime.of(22, 0), UserId.of(1L));

        assertEquals(restaurant1, restaurant2);
        assertNotEquals(restaurant1, restaurant3);
    }

    @Test
    void shouldHaveCorrectHashCode() {
        RestaurantAddress address = RestaurantAddress.create("Rua M", "Maceió", "23456780", "1300", null);
        Restaurant restaurant1 = Restaurant.of(1L, "Restaurant", address, KitchenType.of("Italian"), 
            LocalTime.of(11, 0), LocalTime.of(22, 0), UserId.of(1L));
        Restaurant restaurant2 = Restaurant.of(1L, "Restaurant", address, KitchenType.of("Italian"), 
            LocalTime.of(11, 0), LocalTime.of(22, 0), UserId.of(1L));

        assertEquals(restaurant1.hashCode(), restaurant2.hashCode());
    }
}

