package tech.challenge.establishment.manager.infrastructure.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.challenge.establishment.manager.infrastructure.persistence.entities.RoleJpaEntity;

import java.util.Optional;

public interface RoleJpaRepository extends JpaRepository<RoleJpaEntity, Long> {
    Optional<RoleJpaEntity> findByName(RoleJpaEntity.RoleName name);
}