package tech.challenge.establishment.manager.infrastructure.persistence.mappers;

import tech.challenge.establishment.manager.domain.entities.User;
import tech.challenge.establishment.manager.domain.entities.Role;
import tech.challenge.establishment.manager.domain.entities.Address;
import tech.challenge.establishment.manager.infrastructure.persistence.entities.UserJpaEntity;
import tech.challenge.establishment.manager.infrastructure.persistence.entities.AddressJpaEntity;

import java.util.HashSet;
import java.util.Objects;
import java.util.stream.Collectors;

public class UserJpaMapper {

    public static UserJpaEntity toJpaEntity(User user) {
        if (user == null) return null;

        UserJpaEntity jpaEntity = new UserJpaEntity();

        if (user.getId() != null) jpaEntity.setId(user.getId().value());
        jpaEntity.setName(user.getName().value());
        jpaEntity.setEmail(user.getEmail().value());
        jpaEntity.setLogin(user.getLogin().value());
        jpaEntity.setPassword(user.getPassword().value());
        jpaEntity.setCreatedAt(user.getCreatedAt());
        jpaEntity.setUpdatedAt(user.getUpdatedAt());

        // Removido mapeamento de userType

        if (user.getAddress() != null) {
            AddressJpaEntity addressJpaEntity = AddressJpaMapper.toJpaEntity(user.getAddress(), jpaEntity);
            jpaEntity.setAddress(addressJpaEntity);
        }

        if (user.getRoles() != null && !user.getRoles().isEmpty()) {
            jpaEntity.setRoles(
            user.getRoles().stream()
            .map(RoleJpaMapper::toJpaEntity)
            .collect(Collectors.toSet())
            );
        }

        return jpaEntity;
    }

    public static User toDomainEntity(UserJpaEntity jpaEntity) {
        if (jpaEntity == null) return null;

        Address address = null;
        if (jpaEntity.getAddress() != null) {
            address = AddressJpaMapper.toDomainEntity(jpaEntity.getAddress());
        }

        HashSet<Role> rolesHashSet = new HashSet<>();
        if (jpaEntity.getRoles() != null && !jpaEntity.getRoles().isEmpty()) {
            jpaEntity.getRoles().stream()
                    .map(RoleJpaMapper::toDomainEntity)
                    .filter(Objects::nonNull)
                    .forEach(rolesHashSet::add);
        }

        // Removido mapeamento de userType

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