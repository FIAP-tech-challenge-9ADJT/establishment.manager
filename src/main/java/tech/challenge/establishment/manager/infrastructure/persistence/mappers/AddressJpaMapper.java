package tech.challenge.establishment.manager.infrastructure.persistence.mappers;

import tech.challenge.establishment.manager.domain.entities.Address;
import tech.challenge.establishment.manager.domain.valueobjects.UserId;
import tech.challenge.establishment.manager.infrastructure.persistence.entities.AddressJpaEntity;
<<<<<<< HEAD
import tech.challenge.establishment.manager.infrastructure.persistence.entities.UserJpaEntity;
=======
>>>>>>> origin/main

public class AddressJpaMapper {
    
    public static AddressJpaEntity toJpaEntity(Address address) {
        return toJpaEntity(address, null);
    }
    
<<<<<<< HEAD
    public static AddressJpaEntity toJpaEntity(Address address, UserJpaEntity userJpaEntity) {
=======
    public static AddressJpaEntity toJpaEntity(Address address, tech.challenge.establishment.manager.infrastructure.persistence.entities.UserJpaEntity userJpaEntity) {
>>>>>>> origin/main
        if (address == null) return null;
        
        AddressJpaEntity jpaEntity = new AddressJpaEntity();
        
        if (address.getId() != null) {
            jpaEntity.setId(address.getId().value());
        }
        jpaEntity.setStreet(address.getStreet());
        jpaEntity.setCity(address.getCity());
        jpaEntity.setPostalCode(address.getPostalCode().value());
        jpaEntity.setNumber(address.getNumber());
        
        // Set the bidirectional relationship
        if (userJpaEntity != null) {
            jpaEntity.setUser(userJpaEntity);
        }
        
        return jpaEntity;
    }
    
    public static Address toDomainEntity(AddressJpaEntity jpaEntity) {
        if (jpaEntity == null) return null;
        
        UserId userId = jpaEntity.getUser() != null ? 
            UserId.of(jpaEntity.getUser().getId()) : null;
        
        if (jpaEntity.getId() != null) {
            return Address.of(
                jpaEntity.getId(),
                jpaEntity.getStreet(),
                jpaEntity.getCity(),
                jpaEntity.getPostalCode(),
                jpaEntity.getNumber(),
                userId
            );
        } else {
            return Address.create(
                jpaEntity.getStreet(),
                jpaEntity.getCity(),
                jpaEntity.getPostalCode(),
                jpaEntity.getNumber(),
                userId
            );
        }
    }
}