package tech.challenge.establishment.manager.infrastructure.persistence.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tech.challenge.establishment.manager.domain.entities.Restaurant;
import tech.challenge.establishment.manager.domain.entities.RestaurantAddress;
import tech.challenge.establishment.manager.domain.valueobjects.*;
import tech.challenge.establishment.manager.infrastructure.persistence.entities.RestaurantAddressJpaEntity;
import tech.challenge.establishment.manager.infrastructure.persistence.entities.RestaurantJpaEntity;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RestaurantRepositoryImplTest {

    private RestaurantRepositoryImpl repository;

    @Mock
    private RestaurantJpaRepository restaurantJpaRepository;

    @BeforeEach
    void setUp() {
        repository = new RestaurantRepositoryImpl(restaurantJpaRepository);
    }

    @Test
    void shouldSaveNewRestaurantSuccessfully() {
        // Given
        RestaurantAddress address = new RestaurantAddress(
                RestaurantAddressId.of(1L),
                "New Street",
                "New City",
                new PostalCode("12345678"),
                "100",
                RestaurantId.of(1L)
        );
        Restaurant restaurant = Restaurant.create( // Use create para novo restaurante
                "New Restaurant",
                address, // address é obrigatório
                KitchenType.of("Italian"),
                LocalTime.of(10, 0),
                LocalTime.of(22, 0),
                UserId.of(1L)
        );

        RestaurantAddressJpaEntity addressJpaEntity = new RestaurantAddressJpaEntity();
        addressJpaEntity.setId(1L);
        addressJpaEntity.setStreet("New Street");
        addressJpaEntity.setCity("New City");
        addressJpaEntity.setPostalCode("12345678");
        addressJpaEntity.setNumber("100");

        RestaurantJpaEntity savedEntity = new RestaurantJpaEntity();
        savedEntity.setId(1L);
        savedEntity.setName("New Restaurant");
        savedEntity.setKitchenType("Italian");
        savedEntity.setStartOperation(LocalTime.of(10, 0));
        savedEntity.setEndOperation(LocalTime.of(22, 0));
        savedEntity.setOwnerId(1L);
        savedEntity.setRestaurantAddress(addressJpaEntity);

        when(restaurantJpaRepository.save(any(RestaurantJpaEntity.class))).thenReturn(savedEntity);

        // When
        Restaurant result = repository.save(restaurant);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId().value()).isEqualTo(1L);
        assertThat(result.getName().value()).isEqualTo("New Restaurant");
        verify(restaurantJpaRepository).save(any(RestaurantJpaEntity.class));
    }

    @Test
    void shouldSaveExistingRestaurantSuccessfully() {
        // Given
        RestaurantAddress address = new RestaurantAddress(
                RestaurantAddressId.of(1L),
                "Updated Street",
                "Updated City",
                new PostalCode("87654321"),
                "200",
                RestaurantId.of(1L)
        );
        Restaurant restaurant = Restaurant.of(
                1L, // restaurante existente
                "Updated Restaurant",
                address, // address é obrigatório
                KitchenType.of("Mexican"),
                LocalTime.of(11, 0),
                LocalTime.of(23, 0),
                UserId.of(2L)
        );

        RestaurantJpaEntity existingEntity = new RestaurantJpaEntity();
        existingEntity.setId(1L);
        existingEntity.setName("Old Restaurant");

        RestaurantAddressJpaEntity updatedAddressJpaEntity = new RestaurantAddressJpaEntity();
        updatedAddressJpaEntity.setId(1L);
        updatedAddressJpaEntity.setStreet("Updated Street");
        updatedAddressJpaEntity.setCity("Updated City");
        updatedAddressJpaEntity.setPostalCode("87654321");
        updatedAddressJpaEntity.setNumber("200");

        RestaurantJpaEntity savedEntity = new RestaurantJpaEntity();
        savedEntity.setId(1L);
        savedEntity.setName("Updated Restaurant");
        savedEntity.setKitchenType("Mexican");
        savedEntity.setStartOperation(LocalTime.of(11, 0));
        savedEntity.setEndOperation(LocalTime.of(23, 0));
        savedEntity.setOwnerId(2L);
        savedEntity.setRestaurantAddress(updatedAddressJpaEntity);

        when(restaurantJpaRepository.findById(1L)).thenReturn(Optional.of(existingEntity));
        when(restaurantJpaRepository.save(any(RestaurantJpaEntity.class))).thenReturn(savedEntity);

        // When
        Restaurant result = repository.save(restaurant);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId().value()).isEqualTo(1L);
        assertThat(result.getName().value()).isEqualTo("Updated Restaurant");
        verify(restaurantJpaRepository).findById(1L);
        verify(restaurantJpaRepository).save(any(RestaurantJpaEntity.class));
    }

    @Test
    void shouldFindByIdSuccessfully() {
        // Given
        Long id = 1L;
        RestaurantId restaurantId = RestaurantId.of(id);

        RestaurantAddressJpaEntity addressEntity = new RestaurantAddressJpaEntity();
        addressEntity.setId(1L);
        addressEntity.setStreet("Test Street");
        addressEntity.setCity("Test City");
        addressEntity.setPostalCode("12345678");
        addressEntity.setNumber("100");

        RestaurantJpaEntity jpaEntity = new RestaurantJpaEntity();
        jpaEntity.setId(id);
        jpaEntity.setName("Test Restaurant");
        jpaEntity.setKitchenType("Chinese");
        jpaEntity.setStartOperation(LocalTime.of(9, 0));
        jpaEntity.setEndOperation(LocalTime.of(21, 0));
        jpaEntity.setOwnerId(3L);
        jpaEntity.setRestaurantAddress(addressEntity); // Adicionar address

        when(restaurantJpaRepository.findById(id)).thenReturn(Optional.of(jpaEntity));

        // When
        Optional<Restaurant> result = repository.findById(restaurantId);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getId().value()).isEqualTo(id);
        assertThat(result.get().getName().value()).isEqualTo("Test Restaurant");
        verify(restaurantJpaRepository).findById(id);
    }

    @Test
    void shouldReturnEmptyWhenNotFoundById() {
        // Given
        Long id = 999L;
        RestaurantId restaurantId = RestaurantId.of(id);

        when(restaurantJpaRepository.findById(id)).thenReturn(Optional.empty());

        // When
        Optional<Restaurant> result = repository.findById(restaurantId);

        // Then
        assertThat(result).isEmpty();
        verify(restaurantJpaRepository).findById(id);
    }

    @Test
    void shouldCheckExistsByNameAndRestaurantAddress() {
        // Given
        Name name = Name.of("Test Restaurant");
        RestaurantAddress address = new RestaurantAddress(
                RestaurantAddressId.of(1L),
                "Test Street",
                "Test City",
                new PostalCode("12345678"),
                "100",
                RestaurantId.of(1L)
        );

        when(restaurantJpaRepository.existsByNameAndRestaurantAddress(eq("Test Restaurant"), any(RestaurantAddressJpaEntity.class)))
                .thenReturn(true);

        // When
        boolean result = repository.existsByNameAndRestaurantAddress(name, address);

        // Then
        assertThat(result).isTrue();
        verify(restaurantJpaRepository).existsByNameAndRestaurantAddress(eq("Test Restaurant"), any(RestaurantAddressJpaEntity.class));
    }

    @Test
    void shouldFindByNameSuccessfully() {
        // Given
        Name name = Name.of("Pizza Place");

        RestaurantAddressJpaEntity addressEntity = new RestaurantAddressJpaEntity();
        addressEntity.setId(1L);
        addressEntity.setStreet("Pizza Street");
        addressEntity.setCity("Pizza City");
        addressEntity.setPostalCode("12345678");
        addressEntity.setNumber("100");

        RestaurantJpaEntity jpaEntity = new RestaurantJpaEntity();
        jpaEntity.setId(1L);
        jpaEntity.setName("Pizza Place");
        jpaEntity.setKitchenType("Italian");
        jpaEntity.setStartOperation(LocalTime.of(12, 0));
        jpaEntity.setEndOperation(LocalTime.of(22, 0));
        jpaEntity.setOwnerId(1L);
        jpaEntity.setRestaurantAddress(addressEntity); // Adicionar address

        when(restaurantJpaRepository.findByName("Pizza Place")).thenReturn(Arrays.asList(jpaEntity));

        // When
        Optional<Restaurant> result = repository.findByName(name);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getName().value()).isEqualTo("Pizza Place");
        verify(restaurantJpaRepository).findByName("Pizza Place");
    }

    @Test
    void shouldReturnEmptyWhenNotFoundByName() {
        // Given
        Name name = Name.of("Nonexistent Restaurant");

        when(restaurantJpaRepository.findByName("Nonexistent Restaurant")).thenReturn(Arrays.asList());

        // When
        Optional<Restaurant> result = repository.findByName(name);

        // Then
        assertThat(result).isEmpty();
        verify(restaurantJpaRepository).findByName("Nonexistent Restaurant");
    }

    @Test
    void shouldFindAllRestaurants() {
        // Given
        RestaurantAddressJpaEntity address1 = new RestaurantAddressJpaEntity();
        address1.setId(1L);
        address1.setStreet("Street 1");
        address1.setCity("City 1");
        address1.setPostalCode("12345678");
        address1.setNumber("100");

        RestaurantJpaEntity entity1 = new RestaurantJpaEntity();
        entity1.setId(1L);
        entity1.setName("Restaurant 1");
        entity1.setKitchenType("Italian");
        entity1.setStartOperation(LocalTime.of(10, 0));
        entity1.setEndOperation(LocalTime.of(22, 0));
        entity1.setOwnerId(1L);
        entity1.setRestaurantAddress(address1);

        RestaurantAddressJpaEntity address2 = new RestaurantAddressJpaEntity();
        address2.setId(2L);
        address2.setStreet("Street 2");
        address2.setCity("City 2");
        address2.setPostalCode("87654321");
        address2.setNumber("200");

        RestaurantJpaEntity entity2 = new RestaurantJpaEntity();
        entity2.setId(2L);
        entity2.setName("Restaurant 2");
        entity2.setKitchenType("Japanese");
        entity2.setStartOperation(LocalTime.of(11, 0));
        entity2.setEndOperation(LocalTime.of(23, 0));
        entity2.setOwnerId(2L);
        entity2.setRestaurantAddress(address2);

        when(restaurantJpaRepository.findAll()).thenReturn(Arrays.asList(entity1, entity2));

        // When
        List<Restaurant> result = repository.findAll();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName().value()).isEqualTo("Restaurant 1");
        assertThat(result.get(1).getName().value()).isEqualTo("Restaurant 2");
        verify(restaurantJpaRepository).findAll();
    }

    @Test
    void shouldReturnTrueWhenRestaurantExistsById() {
        // Given
        RestaurantId restaurantId = RestaurantId.of(1L);

        when(restaurantJpaRepository.existsById(1L)).thenReturn(true);

        // When
        boolean result = repository.existsById(restaurantId);

        // Then
        assertThat(result).isTrue();
        verify(restaurantJpaRepository).existsById(1L);
    }

    @Test
    void shouldReturnFalseWhenRestaurantDoesNotExistById() {
        // Given
        RestaurantId restaurantId = RestaurantId.of(999L);

        when(restaurantJpaRepository.existsById(999L)).thenReturn(false);

        // When
        boolean result = repository.existsById(restaurantId);

        // Then
        assertThat(result).isFalse();
        verify(restaurantJpaRepository).existsById(999L);
    }

    @Test
    void shouldDeleteByIdSuccessfully() {
        // Given
        RestaurantId restaurantId = RestaurantId.of(1L);

        // When
        repository.deleteById(restaurantId);

        // Then
        verify(restaurantJpaRepository).deleteById(1L);
    }

    @Test
    void shouldFindByNameContainingIgnoreCase() {
        // Given
        String searchName = "pizza";

        RestaurantAddressJpaEntity addressEntity = new RestaurantAddressJpaEntity();
        addressEntity.setId(1L);
        addressEntity.setStreet("Pizza Street");
        addressEntity.setCity("Pizza City");
        addressEntity.setPostalCode("12345678");
        addressEntity.setNumber("100");

        RestaurantJpaEntity entity = new RestaurantJpaEntity();
        entity.setId(1L);
        entity.setName("Pizza Palace");
        entity.setKitchenType("Italian");
        entity.setStartOperation(LocalTime.of(10, 0));
        entity.setEndOperation(LocalTime.of(22, 0));
        entity.setOwnerId(1L);
        entity.setRestaurantAddress(addressEntity);

        when(restaurantJpaRepository.findByNameContainingIgnoreCase(searchName)).thenReturn(Arrays.asList(entity));

        // When
        List<Restaurant> result = repository.findByNameContainingIgnoreCase(searchName);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName().value()).isEqualTo("Pizza Palace");
        verify(restaurantJpaRepository).findByNameContainingIgnoreCase(searchName);
    }

    @Test
    void shouldFindByNameAndRestaurantAddress() {
        // Given
        Name name = Name.of("Specific Restaurant");
        RestaurantAddress address = new RestaurantAddress(
                RestaurantAddressId.of(1L),
                "Specific Street",
                "Specific City",
                new PostalCode("87654321"),
                "200",
                RestaurantId.of(1L)
        );

        RestaurantAddressJpaEntity addressJpaEntity = new RestaurantAddressJpaEntity();
        addressJpaEntity.setId(1L);
        addressJpaEntity.setStreet("Specific Street");
        addressJpaEntity.setCity("Specific City");
        addressJpaEntity.setPostalCode("87654321");
        addressJpaEntity.setNumber("200");

        RestaurantJpaEntity jpaEntity = new RestaurantJpaEntity();
        jpaEntity.setId(1L);
        jpaEntity.setName("Specific Restaurant");
        jpaEntity.setKitchenType("French");
        jpaEntity.setStartOperation(LocalTime.of(8, 0));
        jpaEntity.setEndOperation(LocalTime.of(20, 0));
        jpaEntity.setOwnerId(1L);
        jpaEntity.setRestaurantAddress(addressJpaEntity);

        when(restaurantJpaRepository.findByNameAndRestaurantAddress(eq("Specific Restaurant"), any(RestaurantAddressJpaEntity.class)))
                .thenReturn(Arrays.asList(jpaEntity));

        // When
        Optional<Restaurant> result = repository.findByNameAndRestaurantAddress(name, address);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getName().value()).isEqualTo("Specific Restaurant");
        verify(restaurantJpaRepository).findByNameAndRestaurantAddress(eq("Specific Restaurant"), any(RestaurantAddressJpaEntity.class));
    }

    @Test
    void shouldReturnEmptyWhenNotFoundByNameAndAddress() {
        // Given
        Name name = Name.of("Nonexistent Restaurant");
        RestaurantAddress address = new RestaurantAddress(
                RestaurantAddressId.of(1L),
                "Nonexistent Street",
                "Nonexistent City",
                new PostalCode("00000000"),
                "0",
                RestaurantId.of(1L)
        );

        when(restaurantJpaRepository.findByNameAndRestaurantAddress(eq("Nonexistent Restaurant"), any(RestaurantAddressJpaEntity.class)))
                .thenReturn(Arrays.asList());

        // When
        Optional<Restaurant> result = repository.findByNameAndRestaurantAddress(name, address);

        // Then
        assertThat(result).isEmpty();
        verify(restaurantJpaRepository).findByNameAndRestaurantAddress(eq("Nonexistent Restaurant"), any(RestaurantAddressJpaEntity.class));
    }
}
