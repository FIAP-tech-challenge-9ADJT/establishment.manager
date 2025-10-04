package tech.challenge.establishment.manager.domain.repositories;

import tech.challenge.establishment.manager.domain.entities.User;
import tech.challenge.establishment.manager.domain.valueobjects.Email;
import tech.challenge.establishment.manager.domain.valueobjects.Login;
import tech.challenge.establishment.manager.domain.valueobjects.UserId;

import java.util.Optional;

public interface UserRepository {
    
    Optional<User> findById(UserId id);
    
    Optional<User> findByLogin(Login login);
    
    User save(User user);
    
    void delete(UserId id);
    
    boolean existsByEmail(Email email);
    
    boolean existsByLogin(Login login);
<<<<<<< HEAD

    boolean existsById(UserId id);
=======
>>>>>>> origin/main
}