package tech.challenge.establishment.manager.infrastructure.persistence.mappers;

import tech.challenge.establishment.manager.domain.entities.User;
import tech.challenge.establishment.manager.domain.entities.Role;
import tech.challenge.establishment.manager.domain.entities.Address;
import tech.challenge.establishment.manager.infrastructure.persistence.entities.UserJpaEntity;
import tech.challenge.establishment.manager.infrastructure.persistence.entities.RoleJpaEntity;
import tech.challenge.establishment.manager.infrastructure.persistence.entities.AddressJpaEntity;

import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

public class UserJpaMapper {
    
    public static UserJpaEntity toJpaEntity(User user) {
        if (user == null) return null;
        
        UserJpaEntity jpaEntity = new UserJpaEntity();
        
        // Mapear campos básicos usando setters do Lombok
        if (user.getId() != null) {
            jpaEntity.setId(user.getId().value());
        }
        jpaEntity.setName(user.getName().value());
        jpaEntity.setEmail(user.getEmail().value());
        jpaEntity.setLogin(user.getLogin().value());
        jpaEntity.setPassword(user.getPassword().value());
        jpaEntity.setCreatedAt(user.getCreatedAt());
        jpaEntity.setUpdatedAt(user.getUpdatedAt());
        
        // Mapear address
        if (user.getAddress() != null) {
            AddressJpaEntity addressJpaEntity = AddressJpaMapper.toJpaEntity(user.getAddress(), jpaEntity);
            jpaEntity.setAddress(addressJpaEntity);
        }
        
        // Mapear roles
        List<RoleJpaEntity> rolesJpa = new ArrayList<>();
        if (user.getRoles() != null) {
            rolesJpa = user.getRoles().stream()
                .map(RoleJpaMapper::toJpaEntity)
                .collect(Collectors.toList());
        }
        jpaEntity.setRoles(rolesJpa);
        
        return jpaEntity;
    }
    
    public static User toDomainEntity(UserJpaEntity jpaEntity) {
        if (jpaEntity == null) return null;
        
        Address address = null;
        if (jpaEntity.getAddress() != null) {
            address = AddressJpaMapper.toDomainEntity(jpaEntity.getAddress());
        }
        
        // Resolver problema de conversão de tipos criando HashSet explicitamente
        HashSet<Role> rolesHashSet = new HashSet<>();
        if (jpaEntity.getRoles() != null) {
            jpaEntity.getRoles().stream()
                .map(RoleJpaMapper::toDomainEntity)
                .filter(Objects::nonNull)
                .forEach(rolesHashSet::add);
        }
        
        return User.of(
            jpaEntity.getId(),
            jpaEntity.getName(),
            jpaEntity.getEmail(),
            jpaEntity.getLogin(),
            jpaEntity.getPassword(),
            address,
            rolesHashSet,
            jpaEntity.getCreatedAt(),
            jpaEntity.getUpdatedAt()
        );
    }
}