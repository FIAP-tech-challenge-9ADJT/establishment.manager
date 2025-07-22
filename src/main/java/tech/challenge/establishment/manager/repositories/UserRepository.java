package tech.challenge.establishment.manager.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import tech.challenge.establishment.manager.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLogin(String login);
}
