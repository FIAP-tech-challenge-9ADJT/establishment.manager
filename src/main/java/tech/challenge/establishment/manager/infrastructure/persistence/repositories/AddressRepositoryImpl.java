package tech.challenge.establishment.manager.infrastructure.persistence.repositories;

import org.springframework.stereotype.Repository;
import tech.challenge.establishment.manager.domain.entities.Address;
import tech.challenge.establishment.manager.domain.repositories.AddressRepository;
import tech.challenge.establishment.manager.domain.valueobjects.AddressId;
import tech.challenge.establishment.manager.domain.valueobjects.UserId;
import tech.challenge.establishment.manager.infrastructure.persistence.mappers.AddressJpaMapper;

import java.util.Optional;

@Repository
public class AddressRepositoryImpl implements AddressRepository {
    
    private final AddressJpaRepository addressJpaRepository;
    
    public AddressRepositoryImpl(AddressJpaRepository addressJpaRepository) {
        this.addressJpaRepository = addressJpaRepository;
    }
    
    @Override
    public Optional<Address> findById(AddressId id) {
        return addressJpaRepository.findById(id.value())
            .map(AddressJpaMapper::toDomainEntity);
    }
    
    @Override
    public Optional<Address> findByUserId(UserId userId) {
        return addressJpaRepository.findByUserId(userId.value())
            .map(AddressJpaMapper::toDomainEntity);
    }
    
    @Override
    public Address save(Address address) {
        var jpaEntity = AddressJpaMapper.toJpaEntity(address);
        var savedEntity = addressJpaRepository.save(jpaEntity);
        return AddressJpaMapper.toDomainEntity(savedEntity);
    }
    
    @Override
    public void delete(AddressId id) {
        addressJpaRepository.deleteById(id.value());
    }
}