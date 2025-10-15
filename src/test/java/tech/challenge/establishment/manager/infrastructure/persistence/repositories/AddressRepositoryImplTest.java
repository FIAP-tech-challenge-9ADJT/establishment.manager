package tech.challenge.establishment.manager.infrastructure.persistence.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tech.challenge.establishment.manager.domain.entities.Address;
import tech.challenge.establishment.manager.domain.valueobjects.AddressId;
import tech.challenge.establishment.manager.domain.valueobjects.PostalCode;
import tech.challenge.establishment.manager.domain.valueobjects.UserId;
import tech.challenge.establishment.manager.infrastructure.persistence.entities.AddressJpaEntity;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AddressRepositoryImplTest {

    private AddressRepositoryImpl repository;

    @Mock
    private AddressJpaRepository addressJpaRepository;

    @BeforeEach
    void setUp() {
        repository = new AddressRepositoryImpl(addressJpaRepository);
    }

    @Test
    void shouldFindByIdSuccessfully() {
        // Given
        Long id = 1L;
        AddressId addressId = AddressId.of(id);
        
        AddressJpaEntity jpaEntity = new AddressJpaEntity();
        jpaEntity.setId(id);
        jpaEntity.setStreet("Test Street");
        jpaEntity.setCity("Test City");
        jpaEntity.setPostalCode("12345678");
        jpaEntity.setNumber("100");
        // Não definimos o user aqui pois é um relacionamento

        when(addressJpaRepository.findById(id)).thenReturn(Optional.of(jpaEntity));

        // When
        Optional<Address> result = repository.findById(addressId);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getStreet()).isEqualTo("Test Street");
        assertThat(result.get().getCity()).isEqualTo("Test City");
        assertThat(result.get().getNumber()).isEqualTo("100");
        verify(addressJpaRepository).findById(id);
    }

    @Test
    void shouldReturnEmptyWhenNotFoundById() {
        // Given
        Long id = 999L;
        AddressId addressId = AddressId.of(id);
        
        when(addressJpaRepository.findById(id)).thenReturn(Optional.empty());

        // When
        Optional<Address> result = repository.findById(addressId);

        // Then
        assertThat(result).isEmpty();
        verify(addressJpaRepository).findById(id);
    }

    @Test
    void shouldFindByUserIdSuccessfully() {
        // Given
        Long userId = 1L;
        UserId userIdObj = UserId.of(userId);
        
        AddressJpaEntity jpaEntity = new AddressJpaEntity();
        jpaEntity.setId(1L);
        jpaEntity.setStreet("User Street");
        jpaEntity.setCity("User City");
        jpaEntity.setPostalCode("87654321");
        jpaEntity.setNumber("200");
        // Não definimos o user aqui pois é um relacionamento

        when(addressJpaRepository.findByUserId(userId)).thenReturn(Optional.of(jpaEntity));

        // When
        Optional<Address> result = repository.findByUserId(userIdObj);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getStreet()).isEqualTo("User Street");
        assertThat(result.get().getCity()).isEqualTo("User City");
        assertThat(result.get().getNumber()).isEqualTo("200");
        verify(addressJpaRepository).findByUserId(userId);
    }

    @Test
    void shouldReturnEmptyWhenNotFoundByUserId() {
        // Given
        Long userId = 999L;
        UserId userIdObj = UserId.of(userId);
        
        when(addressJpaRepository.findByUserId(userId)).thenReturn(Optional.empty());

        // When
        Optional<Address> result = repository.findByUserId(userIdObj);

        // Then
        assertThat(result).isEmpty();
        verify(addressJpaRepository).findByUserId(userId);
    }

    @Test
    void shouldSaveAddressSuccessfully() {
        // Given
        Address address = new Address(
                AddressId.of(1L),
                "Save Street",
                "Save City",
                new PostalCode("11111111"),
                "300",
                UserId.of(1L)
        );

        AddressJpaEntity jpaEntity = new AddressJpaEntity();
        jpaEntity.setId(1L);
        jpaEntity.setStreet("Save Street");
        jpaEntity.setCity("Save City");
        jpaEntity.setPostalCode("11111111");
        jpaEntity.setNumber("300");
        // Não definimos o user aqui pois é um relacionamento

        when(addressJpaRepository.save(any(AddressJpaEntity.class))).thenReturn(jpaEntity);

        // When
        Address result = repository.save(address);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getStreet()).isEqualTo("Save Street");
        assertThat(result.getCity()).isEqualTo("Save City");
        assertThat(result.getNumber()).isEqualTo("300");
        verify(addressJpaRepository).save(any(AddressJpaEntity.class));
    }

    @Test
    void shouldDeleteByIdSuccessfully() {
        // Given
        Long id = 1L;
        AddressId addressId = AddressId.of(id);

        // When
        repository.delete(addressId);

        // Then
        verify(addressJpaRepository).deleteById(id);
    }
}
