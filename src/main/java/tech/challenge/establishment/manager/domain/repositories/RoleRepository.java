package tech.challenge.establishment.manager.domain.repositories;

import tech.challenge.establishment.manager.domain.entities.Role;

import java.util.Optional;

public interface RoleRepository {
    
    Optional<Role> findByName(Role.RoleName name);
    
    Role save(Role role);
}