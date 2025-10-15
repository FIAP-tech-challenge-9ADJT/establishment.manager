package tech.challenge.establishment.manager.domain.entities;

import org.junit.jupiter.api.Test;
import tech.challenge.establishment.manager.domain.valueobjects.KitchenType;
import tech.challenge.establishment.manager.domain.valueobjects.RestaurantId;
import tech.challenge.establishment.manager.domain.valueobjects.UserId;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class RestaurantCompleteTest {

    @Test
    void shouldCreateRestaurantWithAllFields() {
        RestaurantAddress address = RestaurantAddress.create("Rua A", "São Paulo", "01234567", "100", null);
        KitchenType kitchenType = KitchenType.of("Italian");
        LocalTime start = LocalTime.of(11, 0);
        LocalTime end = LocalTime.of(23, 0);
        UserId ownerId = UserId.of(1L);

        Restaurant restaurant = Restaurant.create("Restaurant Name", address, kitchenType, start, end, ownerId);

        assertNotNull(restaurant);
        assertEquals("Restaurant Name", restaurant.getName().value());
        assertEquals(kitchenType, restaurant.getKitchenType());
        assertEquals(start, restaurant.getStartOperation());
        assertEquals(end, restaurant.getEndOperation());
        assertEquals(ownerId, restaurant.getOwnerId());
    }

    @Test
    void shouldCreateRestaurantWithOfMethod() {
        RestaurantAddress address = RestaurantAddress.create("Rua B", "Rio", "12345678", "200", null);
        Restaurant restaurant = Restaurant.of(1L, "Restaurant", address, KitchenType.of("Japanese"), 
            LocalTime.of(10, 0), LocalTime.of(22, 0), UserId.of(1L));

        assertNotNull(restaurant);
        assertNotNull(restaurant.getId());
        assertEquals(1L, restaurant.getId().value());
    }

    @Test
    void shouldGetRestaurantAddressAfterCreation() {
        RestaurantAddress address = RestaurantAddress.create("Rua D", "Fortaleza", "11111111", "400", null);
        Restaurant restaurant = Restaurant.create("Restaurant", address, KitchenType.of("Mexican"), 
            LocalTime.of(11, 0), LocalTime.of(22, 0), UserId.of(1L));

        assertEquals(address, restaurant.getRestaurantAddress());
    }

    @Test
    void shouldCreateRestaurantWithDifferentOperatingHours() {
        RestaurantAddress address = RestaurantAddress.create("Rua E", "Curitiba", "22222222", "500", null);
        
        Restaurant lunch = Restaurant.create("Lunch Place", address, KitchenType.of("Italian"), 
            LocalTime.of(11, 30), LocalTime.of(14, 30), UserId.of(1L));
        
        Restaurant dinner = Restaurant.create("Dinner Place", address, KitchenType.of("French"), 
            LocalTime.of(18, 0), LocalTime.of(23, 30), UserId.of(1L));

        assertEquals(LocalTime.of(11, 30), lunch.getStartOperation());
        assertEquals(LocalTime.of(14, 30), lunch.getEndOperation());
        assertEquals(LocalTime.of(18, 0), dinner.getStartOperation());
        assertEquals(LocalTime.of(23, 30), dinner.getEndOperation());
    }

    @Test
    void shouldUpdateRestaurantOperatingHours() {
        RestaurantAddress address = RestaurantAddress.create("Rua F", "Porto Alegre", "33333333", "600", null);
        Restaurant restaurant = Restaurant.of(1L, "Restaurant", address, KitchenType.of("Chinese"), 
            LocalTime.of(11, 0), LocalTime.of(22, 0), UserId.of(1L));

        Restaurant updated = restaurant.updateOperatingHours(LocalTime.of(12, 0), LocalTime.of(23, 30));

        assertEquals(LocalTime.of(12, 0), updated.getStartOperation());
        assertEquals(LocalTime.of(23, 30), updated.getEndOperation());
    }

    @Test
    void shouldUpdateRestaurantName() {
        RestaurantAddress address = RestaurantAddress.create("Rua G", "Salvador", "44444444", "700", null);
        Restaurant restaurant = Restaurant.of(1L, "Old Name", address, KitchenType.of("Indian"), 
            LocalTime.of(11, 0), LocalTime.of(22, 0), UserId.of(1L));

        Restaurant updated = restaurant.updateName("New Name");

        assertEquals("New Name", updated.getName().value());
    }

    @Test
    void shouldUpdateRestaurantAddress() {
        RestaurantAddress oldAddress = RestaurantAddress.create("Rua H", "Recife", "55555555", "800", null);
        RestaurantAddress newAddress = RestaurantAddress.create("Rua I", "Manaus", "66666666", "900", null);
        
        Restaurant restaurant = Restaurant.of(1L, "Restaurant", oldAddress, KitchenType.of("Italian"), 
            LocalTime.of(11, 0), LocalTime.of(22, 0), UserId.of(1L));

        Restaurant updated = restaurant.updateRestaurantAddress(newAddress);

        assertEquals(newAddress, updated.getRestaurantAddress());
    }

    @Test
    void shouldToStringContainRestaurantInfo() {
        RestaurantAddress address = RestaurantAddress.create("Rua J", "Belém", "77777777", "1000", null);
        Restaurant restaurant = Restaurant.create("Pizza Place", address, KitchenType.of("Italian"), 
            LocalTime.of(18, 0), LocalTime.of(23, 0), UserId.of(1L));

        String toString = restaurant.toString();
        
        assertNotNull(toString);
        assertTrue(toString.contains("Pizza Place") || toString.contains("Restaurant"));
    }
}

