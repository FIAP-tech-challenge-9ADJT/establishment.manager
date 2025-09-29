package tech.challenge.establishment.manager.domain.usecases.restaurantowner;

import tech.challenge.establishment.manager.domain.entities.User;
import tech.challenge.establishment.manager.domain.exceptions.UserNotFoundException;
import tech.challenge.establishment.manager.domain.repositories.UserRepository;
import tech.challenge.establishment.manager.domain.valueobjects.UserId;

public class UpdateRestaurantOwnerUseCase {

    private final UserRepository userRepository;

    public UpdateRestaurantOwnerUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User execute(UserId userId, String name, String email) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        // Garantir que é um restaurant owner
        boolean isOwner = existingUser.getRoles().stream()
                .anyMatch(role -> role.getName() == tech.challenge.establishment.manager.domain.entities.Role.RoleName.RESTAURANT_OWNER);

        if (!isOwner) {
            throw new UserNotFoundException(userId); // ou exceção específica
        }

        User updatedUser = existingUser.updateProfile(name, email);

        return userRepository.save(updatedUser);
    }
}