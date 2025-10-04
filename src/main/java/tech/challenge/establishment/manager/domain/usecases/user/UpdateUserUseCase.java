package tech.challenge.establishment.manager.domain.usecases.user;

import tech.challenge.establishment.manager.domain.entities.User;
import tech.challenge.establishment.manager.domain.exceptions.UserNotFoundException;
import tech.challenge.establishment.manager.domain.repositories.UserRepository;
import tech.challenge.establishment.manager.domain.valueobjects.UserId;

public class UpdateUserUseCase {
    
    private final UserRepository userRepository;
    
    public UpdateUserUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    public User execute(UserId userId, String name, String email) {
        User existingUser = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException(userId));
        
        User updatedUser = existingUser.updateProfile(name, email);
        
        return userRepository.save(updatedUser);
    }
}