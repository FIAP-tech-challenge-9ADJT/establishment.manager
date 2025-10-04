package tech.challenge.establishment.manager.infrastructure.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tech.challenge.establishment.manager.infrastructure.persistence.entities.UserJpaEntity;

import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<UserJpaEntity, Long> {
    Optional<UserJpaEntity> findByLogin(String login);
    boolean existsByEmail(String email);
    boolean existsByLogin(String login);
<<<<<<< HEAD
    boolean existsById(Long id);
=======
>>>>>>> origin/main
}