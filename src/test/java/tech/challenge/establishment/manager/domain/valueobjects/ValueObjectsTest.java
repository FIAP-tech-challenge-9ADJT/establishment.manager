package tech.challenge.establishment.manager.domain.valueobjects;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ValueObjectsTest {

    @Test
    void shouldCreateEmailSuccessfully() {
        Email email = Email.of("test@example.com");
        assertEquals("test@example.com", email.value());
    }

    @Test
    void shouldThrowExceptionForInvalidEmail() {
        assertThrows(IllegalArgumentException.class, () -> Email.of("invalid-email"));
        assertThrows(IllegalArgumentException.class, () -> Email.of(""));
        assertThrows(NullPointerException.class, () -> Email.of(null));
    }

    @Test
    void shouldCreatePasswordSuccessfully() {
        Password password = Password.of("password123");
        assertEquals("password123", password.value());
    }

    @Test
    void shouldCreateEncodedPassword() {
        Password password = Password.encoded("encodedPassword123");
        assertEquals("encodedPassword123", password.value());
    }

    @Test
    void shouldThrowExceptionForShortPassword() {
        assertThrows(IllegalArgumentException.class, () -> Password.of("short"));
        assertThrows(IllegalArgumentException.class, () -> Password.of("12345"));
    }

    @Test
    void shouldThrowExceptionForNullPassword() {
        assertThrows(NullPointerException.class, () -> Password.of(null));
    }

    @Test
    void shouldCreateNameSuccessfully() {
        Name name = Name.of("João Silva");
        assertEquals("João Silva", name.value());
    }

    @Test
    void shouldThrowExceptionForInvalidName() {
        assertThrows(IllegalArgumentException.class, () -> Name.of(""));
        assertThrows(IllegalArgumentException.class, () -> Name.of("   "));
        assertThrows(NullPointerException.class, () -> Name.of(null));
    }

    @Test
    void shouldCreateLoginSuccessfully() {
        Login login = Login.of("user123");
        assertEquals("user123", login.value());
    }

    @Test
    void shouldThrowExceptionForInvalidLogin() {
        assertThrows(IllegalArgumentException.class, () -> Login.of("usr"));
        assertThrows(IllegalArgumentException.class, () -> Login.of(""));
        assertThrows(NullPointerException.class, () -> Login.of(null));
    }

    @Test
    void shouldCreatePostalCodeSuccessfully() {
        PostalCode postalCode = PostalCode.of("12345678");
        assertEquals("12345678", postalCode.value());
    }

    @Test
    void shouldThrowExceptionForInvalidPostalCode() {
        assertThrows(IllegalArgumentException.class, () -> PostalCode.of("1234"));
        assertThrows(IllegalArgumentException.class, () -> PostalCode.of("abc12345"));
        assertThrows(NullPointerException.class, () -> PostalCode.of(null));
    }

    @Test
    void shouldCreateUserIdSuccessfully() {
        UserId userId = UserId.of(1L);
        assertEquals(1L, userId.value());
    }

    @Test
    void shouldThrowExceptionForInvalidUserId() {
        assertThrows(NullPointerException.class, () -> UserId.of(null));
        assertThrows(IllegalArgumentException.class, () -> UserId.of(0L));
        assertThrows(IllegalArgumentException.class, () -> UserId.of(-1L));
    }

    @Test
    void shouldCreateAddressIdSuccessfully() {
        AddressId addressId = AddressId.of(1L);
        assertEquals(1L, addressId.value());
    }

    @Test
    void shouldThrowExceptionForInvalidAddressId() {
        assertThrows(NullPointerException.class, () -> AddressId.of(null));
        assertThrows(IllegalArgumentException.class, () -> AddressId.of(0L));
        assertThrows(IllegalArgumentException.class, () -> AddressId.of(-1L));
    }

    @Test
    void shouldCreateRestaurantIdSuccessfully() {
        RestaurantId restaurantId = RestaurantId.of(1L);
        assertEquals(1L, restaurantId.value());
    }

    @Test
    void shouldThrowExceptionForInvalidRestaurantId() {
        assertThrows(NullPointerException.class, () -> RestaurantId.of(null));
        assertThrows(IllegalArgumentException.class, () -> RestaurantId.of(0L));
        assertThrows(IllegalArgumentException.class, () -> RestaurantId.of(-1L));
    }

    @Test
    void shouldCreateRestaurantAddressIdSuccessfully() {
        RestaurantAddressId id = RestaurantAddressId.of(1L);
        assertEquals(1L, id.value());
    }

    @Test
    void shouldThrowExceptionForInvalidRestaurantAddressId() {
        assertThrows(NullPointerException.class, () -> RestaurantAddressId.of(null));
        assertThrows(IllegalArgumentException.class, () -> RestaurantAddressId.of(0L));
        assertThrows(IllegalArgumentException.class, () -> RestaurantAddressId.of(-1L));
    }

    @Test
    void shouldCreateMenuItemIdSuccessfully() {
        MenuItemId id = MenuItemId.of(1L);
        assertEquals(1L, id.value());
    }

    @Test
    void shouldThrowExceptionForInvalidMenuItemId() {
        assertThrows(NullPointerException.class, () -> MenuItemId.of(null));
        assertThrows(IllegalArgumentException.class, () -> MenuItemId.of(0L));
        assertThrows(IllegalArgumentException.class, () -> MenuItemId.of(-1L));
    }

    @Test
    void shouldCreateKitchenTypeSuccessfully() {
        KitchenType type = KitchenType.of("Italian");
        assertEquals("Italian", type.value());
    }

    @Test
    void shouldThrowExceptionForInvalidKitchenType() {
        assertThrows(IllegalArgumentException.class, () -> KitchenType.of("InvalidType"));
        assertThrows(IllegalArgumentException.class, () -> KitchenType.of(""));
        assertThrows(NullPointerException.class, () -> KitchenType.of(null));
    }

    @Test
    void shouldAcceptAllValidKitchenTypes() {
        assertDoesNotThrow(() -> KitchenType.of("Italian"));
        assertDoesNotThrow(() -> KitchenType.of("Japanese"));
        assertDoesNotThrow(() -> KitchenType.of("Brazilian"));
        assertDoesNotThrow(() -> KitchenType.of("Mexican"));
        assertDoesNotThrow(() -> KitchenType.of("Chinese"));
        assertDoesNotThrow(() -> KitchenType.of("French"));
        assertDoesNotThrow(() -> KitchenType.of("Indian"));
    }

    @Test
    void shouldThrowExceptionForLongName() {
        String longName = "a".repeat(101);
        assertThrows(IllegalArgumentException.class, () -> Name.of(longName));
    }

    @Test
    void shouldThrowExceptionForLongLogin() {
        String longLogin = "a".repeat(51);
        assertThrows(IllegalArgumentException.class, () -> Login.of(longLogin));
    }

    @Test
    void shouldAcceptMaxLengthName() {
        String maxName = "a".repeat(100);
        assertDoesNotThrow(() -> Name.of(maxName));
    }

    @Test
    void shouldAcceptMaxLengthLogin() {
        String maxLogin = "a".repeat(50);
        assertDoesNotThrow(() -> Login.of(maxLogin));
    }

    @Test
    void shouldAcceptMinLengthLogin() {
        assertDoesNotThrow(() -> Login.of("user"));
    }

    @Test
    void shouldAcceptMinLengthPassword() {
        assertDoesNotThrow(() -> Password.of("pass12"));
    }
}

