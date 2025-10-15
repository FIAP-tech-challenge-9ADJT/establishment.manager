package tech.challenge.establishment.manager.infrastructure.persistence.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tech.challenge.establishment.manager.domain.entities.RestaurantAddress;
import tech.challenge.establishment.manager.domain.valueobjects.PostalCode;
import tech.challenge.establishment.manager.domain.valueobjects.RestaurantAddressId;
import tech.challenge.establishment.manager.domain.valueobjects.RestaurantId;
import tech.challenge.establishment.manager.infrastructure.persistence.entities.RestaurantAddressJpaEntity;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RestaurantAddressRepositoryImplTest {

    private RestaurantAddressRepositoryImpl repository;

    @Mock
    private RestaurantAddressJpaRepository restaurantAddressJpaRepository;

    @BeforeEach
    void setUp() {
        repository = new RestaurantAddressRepositoryImpl(restaurantAddressJpaRepository);
    }

    @Test
    void shouldFindByIdSuccessfully() {
        // Given
        Long id = 1L;
        RestaurantAddressId restaurantAddressId = RestaurantAddressId.of(id);
        
        RestaurantAddressJpaEntity jpaEntity = new RestaurantAddressJpaEntity();
        jpaEntity.setId(id);
        jpaEntity.setStreet("Test Street");
        jpaEntity.setCity("Test City");
        jpaEntity.setPostalCode("12345678");
        jpaEntity.setNumber("100");
        // Não definimos o restaurant aqui pois é um relacionamento

        when(restaurantAddressJpaRepository.findById(id)).thenReturn(Optional.of(jpaEntity));

        // When
        Optional<RestaurantAddress> result = repository.findById(restaurantAddressId);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getStreet()).isEqualTo("Test Street");
        assertThat(result.get().getCity()).isEqualTo("Test City");
        assertThat(result.get().getNumber()).isEqualTo("100");
        verify(restaurantAddressJpaRepository).findById(id);
    }

    @Test
    void shouldReturnEmptyWhenNotFoundById() {
        // Given
        Long id = 999L;
        RestaurantAddressId restaurantAddressId = RestaurantAddressId.of(id);
        
        when(restaurantAddressJpaRepository.findById(id)).thenReturn(Optional.empty());

        // When
        Optional<RestaurantAddress> result = repository.findById(restaurantAddressId);

        // Then
        assertThat(result).isEmpty();
        verify(restaurantAddressJpaRepository).findById(id);
    }

    @Test
    void shouldFindByRestaurantIdSuccessfully() {
        // Given
        Long restaurantId = 1L;
        RestaurantId restaurantIdObj = RestaurantId.of(restaurantId);
        
        RestaurantAddressJpaEntity jpaEntity = new RestaurantAddressJpaEntity();
        jpaEntity.setId(1L);
        jpaEntity.setStreet("Restaurant Street");
        jpaEntity.setCity("Restaurant City");
        jpaEntity.setPostalCode("87654321");
        jpaEntity.setNumber("200");
        // Não definimos o restaurant aqui pois é um relacionamento

        when(restaurantAddressJpaRepository.findByRestaurantId(restaurantId)).thenReturn(Optional.of(jpaEntity));

        // When
        Optional<RestaurantAddress> result = repository.findByRestaurantId(restaurantIdObj);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getStreet()).isEqualTo("Restaurant Street");
        assertThat(result.get().getCity()).isEqualTo("Restaurant City");
        assertThat(result.get().getNumber()).isEqualTo("200");
        verify(restaurantAddressJpaRepository).findByRestaurantId(restaurantId);
    }

    @Test
    void shouldReturnEmptyWhenNotFoundByRestaurantId() {
        // Given
        Long restaurantId = 999L;
        RestaurantId restaurantIdObj = RestaurantId.of(restaurantId);
        
        when(restaurantAddressJpaRepository.findByRestaurantId(restaurantId)).thenReturn(Optional.empty());

        // When
        Optional<RestaurantAddress> result = repository.findByRestaurantId(restaurantIdObj);

        // Then
        assertThat(result).isEmpty();
        verify(restaurantAddressJpaRepository).findByRestaurantId(restaurantId);
    }

    @Test
    void shouldSaveRestaurantAddressSuccessfully() {
        // Given
        RestaurantAddress restaurantAddress = new RestaurantAddress(
                RestaurantAddressId.of(1L),
                "Save Street",
                "Save City",
                new PostalCode("11111111"),
                "300",
                RestaurantId.of(1L)
        );

        RestaurantAddressJpaEntity jpaEntity = new RestaurantAddressJpaEntity();
        jpaEntity.setId(1L);
        jpaEntity.setStreet("Save Street");
        jpaEntity.setCity("Save City");
        jpaEntity.setPostalCode("11111111");
        jpaEntity.setNumber("300");
        // Não definimos o restaurant aqui pois é um relacionamento

        when(restaurantAddressJpaRepository.save(any(RestaurantAddressJpaEntity.class))).thenReturn(jpaEntity);

        // When
        RestaurantAddress result = repository.save(restaurantAddress);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getStreet()).isEqualTo("Save Street");
        assertThat(result.getCity()).isEqualTo("Save City");
        assertThat(result.getNumber()).isEqualTo("300");
        verify(restaurantAddressJpaRepository).save(any(RestaurantAddressJpaEntity.class));
    }

    @Test
    void shouldDeleteByIdSuccessfully() {
        // Given
        Long id = 1L;
        RestaurantAddressId restaurantAddressId = RestaurantAddressId.of(id);

        // When
        repository.delete(restaurantAddressId);

        // Then
        verify(restaurantAddressJpaRepository).deleteById(id);
    }
}
