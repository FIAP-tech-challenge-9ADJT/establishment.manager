package tech.challenge.establishment.manager.domain.usecases.restaurantowner;

import tech.challenge.establishment.manager.domain.entities.User;
import tech.challenge.establishment.manager.domain.exceptions.UserNotFoundException;
import tech.challenge.establishment.manager.domain.repositories.UserRepository;
import tech.challenge.establishment.manager.domain.valueobjects.UserId;

public class FindRestaurantOwnerUseCase {

    private final UserRepository userRepository;

    public FindRestaurantOwnerUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User execute(UserId userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        boolean isOwner = user.getRoles().stream()
                              .anyMatch(role -> role.getName() == tech.challenge.establishment.manager.domain.entities.Role.RoleName.RESTAURANT_OWNER);

        if (!isOwner) {
            throw new UserNotFoundException(userId);
        }

        return user;
    }
}