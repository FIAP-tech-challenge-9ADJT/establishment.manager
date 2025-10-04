package tech.challenge.establishment.manager.domain.usecases.auth;

import tech.challenge.establishment.manager.domain.entities.User;
import tech.challenge.establishment.manager.domain.exceptions.UserNotFoundException;
import tech.challenge.establishment.manager.domain.repositories.UserRepository;
import tech.challenge.establishment.manager.domain.valueobjects.UserId;

public class ChangePasswordUseCase {
    
    private final UserRepository userRepository;
    
    public ChangePasswordUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    public void execute(UserId userId, String newPassword) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException(userId));
        
        User updatedUser = user.changePassword(newPassword);
        
        userRepository.save(updatedUser);
    }
}