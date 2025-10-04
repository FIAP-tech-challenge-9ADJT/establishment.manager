package tech.challenge.establishment.manager.domain.repositories;

import tech.challenge.establishment.manager.domain.entities.Address;
import tech.challenge.establishment.manager.domain.valueobjects.AddressId;
import tech.challenge.establishment.manager.domain.valueobjects.UserId;

import java.util.Optional;

public interface AddressRepository {
    
    Optional<Address> findById(AddressId id);
    
    Optional<Address> findByUserId(UserId userId);
    
    Address save(Address address);
    
    void delete(AddressId id);
}