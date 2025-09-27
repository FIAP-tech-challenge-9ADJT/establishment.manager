package tech.challenge.establishment.manager.domain.usecases.auth;

import tech.challenge.establishment.manager.domain.entities.User;
import tech.challenge.establishment.manager.domain.exceptions.InvalidCredentialsException;
import tech.challenge.establishment.manager.domain.repositories.UserRepository;
import tech.challenge.establishment.manager.domain.valueobjects.Login;

public class AuthenticateUserUseCase {
    
    private final UserRepository userRepository;
    
    public AuthenticateUserUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    public User execute(String login, String password) {
        User user = userRepository.findByLogin(Login.of(login))
            .orElseThrow(() -> new InvalidCredentialsException());
        
        // A validação da senha será feita na camada de infraestrutura
        // pois depende do PasswordEncoder do Spring Security
        
        return user;
    }
}