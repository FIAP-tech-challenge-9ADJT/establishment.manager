package tech.challenge.establishment.manager.infrastructure.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.challenge.establishment.manager.infrastructure.persistence.entities.AddressJpaEntity;

import java.util.Optional;

public interface AddressJpaRepository extends JpaRepository<AddressJpaEntity, Long> {
    Optional<AddressJpaEntity> findByUserId(Long userId);
}