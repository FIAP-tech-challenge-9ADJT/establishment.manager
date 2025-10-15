package tech.challenge.establishment.manager.infrastructure.persistence.mappers;

import org.junit.jupiter.api.Test;
import tech.challenge.establishment.manager.domain.entities.Restaurant;
import tech.challenge.establishment.manager.domain.entities.RestaurantAddress;
import tech.challenge.establishment.manager.domain.valueobjects.*;
import tech.challenge.establishment.manager.infrastructure.persistence.entities.RestaurantAddressJpaEntity;
import tech.challenge.establishment.manager.infrastructure.persistence.entities.RestaurantJpaEntity;

import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RestaurantJpaMapperTest {

    @Test
    void shouldMapToJpaEntityWithNullRestaurant() {
        // Given
        Restaurant restaurant = null;

        // When
        RestaurantJpaEntity result = RestaurantJpaMapper.toJpaEntity(restaurant);

        // Then
        assertThat(result).isNull();
    }

    @Test
    void shouldMapToJpaEntityWithoutAddress() {
        // Given
        RestaurantAddress address = new RestaurantAddress(
                RestaurantAddressId.of(1L),
                "Test Street",
                "Test City", 
                new PostalCode("12345678"),
                "100",
                RestaurantId.of(1L)
        );
        Restaurant restaurant = Restaurant.of(
                1L,
                "Test Restaurant",
                address, // address é obrigatório
                KitchenType.of("Italian"),
                LocalTime.of(10, 0),
                LocalTime.of(22, 0),
                UserId.of(1L)
        );

        // When
        RestaurantJpaEntity result = RestaurantJpaMapper.toJpaEntity(restaurant);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Test Restaurant");
        assertThat(result.getKitchenType()).isEqualTo("Italian");
        assertThat(result.getStartOperation()).isEqualTo(LocalTime.of(10, 0));
        assertThat(result.getEndOperation()).isEqualTo(LocalTime.of(22, 0));
        assertThat(result.getOwnerId()).isEqualTo(1L);
        assertThat(result.getRestaurantAddress()).isNotNull();
    }

    @Test
    void shouldMapToJpaEntityWithAddress() {
        // Given
        RestaurantAddress address = new RestaurantAddress(
                RestaurantAddressId.of(1L),
                "Test Street",
                "Test City",
                new PostalCode("12345678"),
                "100",
                RestaurantId.of(1L)
        );

        Restaurant restaurant = Restaurant.of(
                1L,
                "Test Restaurant",
                address,
                KitchenType.of("Mexican"),
                LocalTime.of(11, 0),
                LocalTime.of(23, 0),
                UserId.of(2L)
        );

        // When
        RestaurantJpaEntity result = RestaurantJpaMapper.toJpaEntity(restaurant);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Test Restaurant");
        assertThat(result.getKitchenType()).isEqualTo("Mexican");
        assertThat(result.getStartOperation()).isEqualTo(LocalTime.of(11, 0));
        assertThat(result.getEndOperation()).isEqualTo(LocalTime.of(23, 0));
        assertThat(result.getOwnerId()).isEqualTo(2L);
        assertThat(result.getRestaurantAddress()).isNotNull();
    }

    @Test
    void shouldMapToJpaEntityWithNullId() {
        // Given
        RestaurantAddress address = new RestaurantAddress(
                RestaurantAddressId.of(3L),
                "New Street",
                "New City", 
                new PostalCode("12345678"),
                "300",
                RestaurantId.of(3L)
        );
        Restaurant restaurant = Restaurant.create( // Use create para ID nulo
                "New Restaurant",
                address,
                KitchenType.of("Chinese"),
                LocalTime.of(12, 0),
                LocalTime.of(21, 0),
                UserId.of(3L)
        );

        // When
        RestaurantJpaEntity result = RestaurantJpaMapper.toJpaEntity(restaurant);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isNull();
        assertThat(result.getName()).isEqualTo("New Restaurant");
        assertThat(result.getKitchenType()).isEqualTo("Chinese");
        assertThat(result.getOwnerId()).isEqualTo(3L);
    }

    @Test
    void shouldMapToJpaEntityWithExistingEntity() {
        // Given
        RestaurantJpaEntity existingEntity = new RestaurantJpaEntity();
        existingEntity.setId(5L);
        existingEntity.setName("Old Name");

        RestaurantAddress address = new RestaurantAddress(
                RestaurantAddressId.of(2L),
                "Updated Street",
                "Updated City", 
                new PostalCode("87654321"),
                "200",
                RestaurantId.of(2L)
        );
        Restaurant restaurant = Restaurant.of(
                2L,
                "Updated Restaurant",
                address, // address é obrigatório
                KitchenType.of("Japanese"),
                LocalTime.of(9, 0),
                LocalTime.of(20, 0),
                UserId.of(4L)
        );

        // When
        RestaurantJpaEntity result = RestaurantJpaMapper.toJpaEntity(restaurant, existingEntity);

        // Then
        assertThat(result).isSameAs(existingEntity);
        assertThat(result.getId()).isEqualTo(2L);
        assertThat(result.getName()).isEqualTo("Updated Restaurant");
        assertThat(result.getKitchenType()).isEqualTo("Japanese");
        assertThat(result.getOwnerId()).isEqualTo(4L);
    }

    @Test
    void shouldMapToJpaEntityWithNullExistingEntity() {
        // Given
        RestaurantJpaEntity existingEntity = null;

        RestaurantAddress address = new RestaurantAddress(
                RestaurantAddressId.of(3L),
                "New Street",
                "New City", 
                new PostalCode("11111111"),
                "300",
                RestaurantId.of(3L)
        );
        Restaurant restaurant = Restaurant.of(
                3L,
                "New Restaurant",
                address, // address é obrigatório
                KitchenType.of("Brazilian"),
                LocalTime.of(8, 0),
                LocalTime.of(19, 0),
                UserId.of(5L)
        );

        // When
        RestaurantJpaEntity result = RestaurantJpaMapper.toJpaEntity(restaurant, existingEntity);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(3L);
        assertThat(result.getName()).isEqualTo("New Restaurant");
        assertThat(result.getKitchenType()).isEqualTo("Brazilian");
        assertThat(result.getOwnerId()).isEqualTo(5L);
    }

    @Test
    void shouldMapToJpaEntityWithExistingEntityAndAddress() {
        // Given
        RestaurantAddressJpaEntity existingAddress = new RestaurantAddressJpaEntity();
        existingAddress.setId(10L);

        RestaurantJpaEntity existingEntity = new RestaurantJpaEntity();
        existingEntity.setId(5L);
        existingEntity.setRestaurantAddress(existingAddress);

        RestaurantAddress address = new RestaurantAddress(
                RestaurantAddressId.of(1L),
                "New Street",
                "New City",
                new PostalCode("87654321"),
                "200",
                RestaurantId.of(3L)
        );

        Restaurant restaurant = Restaurant.of(
                3L,
                "Restaurant with Address",
                address,
                KitchenType.of("French"),
                LocalTime.of(7, 0),
                LocalTime.of(18, 0),
                UserId.of(6L)
        );

        // When
        RestaurantJpaEntity result = RestaurantJpaMapper.toJpaEntity(restaurant, existingEntity);

        // Then
        assertThat(result).isSameAs(existingEntity);
        assertThat(result.getId()).isEqualTo(3L);
        assertThat(result.getName()).isEqualTo("Restaurant with Address");
        assertThat(result.getKitchenType()).isEqualTo("French");
        assertThat(result.getOwnerId()).isEqualTo(6L);
        assertThat(result.getRestaurantAddress()).isNotNull();
    }

    @Test
    void shouldMapToDomainEntityWithNullJpaEntity() {
        // Given
        RestaurantJpaEntity entity = null;

        // When
        Restaurant result = RestaurantJpaMapper.toDomainEntity(entity);

        // Then
        assertThat(result).isNull();
    }

    @Test
    void shouldMapToDomainEntitySuccessfully() {
        // Given
        RestaurantAddressJpaEntity addressEntity = new RestaurantAddressJpaEntity();
        addressEntity.setId(1L);
        addressEntity.setStreet("JPA Street");
        addressEntity.setCity("JPA City");
        addressEntity.setPostalCode("11111111");
        addressEntity.setNumber("300");

        RestaurantJpaEntity entity = new RestaurantJpaEntity();
        entity.setId(10L);
        entity.setName("JPA Restaurant");
        entity.setKitchenType("Indian");
        entity.setStartOperation(LocalTime.of(6, 0));
        entity.setEndOperation(LocalTime.of(17, 0));
        entity.setOwnerId(7L);
        entity.setRestaurantAddress(addressEntity);

        // When
        Restaurant result = RestaurantJpaMapper.toDomainEntity(entity);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId().value()).isEqualTo(10L);
        assertThat(result.getName().value()).isEqualTo("JPA Restaurant");
        assertThat(result.getKitchenType().value()).isEqualTo("Indian");
        assertThat(result.getStartOperation()).isEqualTo(LocalTime.of(6, 0));
        assertThat(result.getEndOperation()).isEqualTo(LocalTime.of(17, 0));
        assertThat(result.getOwnerId().value()).isEqualTo(7L);
        assertThat(result.getRestaurantAddress()).isNotNull();
    }

    @Test
    void shouldMapToDomainEntityWithNullAddress() {
        // Given
        RestaurantJpaEntity entity = new RestaurantJpaEntity();
        entity.setId(11L);
        entity.setName("Restaurant No Address");
        entity.setKitchenType("Chinese");
        entity.setStartOperation(LocalTime.of(5, 0));
        entity.setEndOperation(LocalTime.of(16, 0));
        entity.setOwnerId(8L);
        entity.setRestaurantAddress(null);

        // When & Then
        // O mapper deve lançar uma exceção ou retornar null quando o address é null
        // pois Restaurant.of() requer um address não-null
        assertThatThrownBy(() -> RestaurantJpaMapper.toDomainEntity(entity))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Address cannot be null");
    }
}
