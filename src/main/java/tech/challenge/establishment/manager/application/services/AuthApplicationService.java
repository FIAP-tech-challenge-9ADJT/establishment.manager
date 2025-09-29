package tech.challenge.establishment.manager.application.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tech.challenge.establishment.manager.domain.entities.User;
import tech.challenge.establishment.manager.domain.exceptions.InvalidCredentialsException;
import tech.challenge.establishment.manager.domain.usecases.auth.AuthenticateUserUseCase;
import tech.challenge.establishment.manager.domain.usecases.auth.ChangePasswordUseCase;
import tech.challenge.establishment.manager.domain.valueobjects.UserId;

@Service
public class AuthApplicationService {
    
    private final AuthenticateUserUseCase authenticateUserUseCase;
    private final ChangePasswordUseCase changePasswordUseCase;
    private final PasswordEncoder passwordEncoder;
    
    public AuthApplicationService(AuthenticateUserUseCase authenticateUserUseCase,
                                 ChangePasswordUseCase changePasswordUseCase,
                                 PasswordEncoder passwordEncoder) {
        this.authenticateUserUseCase = authenticateUserUseCase;
        this.changePasswordUseCase = changePasswordUseCase;
        this.passwordEncoder = passwordEncoder;
    }
    
    public User authenticate(String login, String password) {
        User user = authenticateUserUseCase.execute(login, password);
        
        // Validar senha
        if (!passwordEncoder.matches(password, user.getPassword().value())) {
            throw new InvalidCredentialsException();
        }
        
        return user;
    }
    
    public void changePassword(UserId userId, String newPassword) {
        String encodedPassword = passwordEncoder.encode(newPassword);
        changePasswordUseCase.execute(userId, encodedPassword);
    }
}