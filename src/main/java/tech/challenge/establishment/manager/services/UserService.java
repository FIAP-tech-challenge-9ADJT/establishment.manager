package tech.challenge.establishment.manager.services;

import org.springframework.stereotype.Service;
import tech.challenge.establishment.manager.repositories.UserRepository;

@Service
public class UserService {

   private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
