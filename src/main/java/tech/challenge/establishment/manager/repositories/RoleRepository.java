package tech.challenge.establishment.manager.repositories;

import tech.challenge.establishment.manager.entities.Role;
import tech.challenge.establishment.manager.entities.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(RoleName name);
}