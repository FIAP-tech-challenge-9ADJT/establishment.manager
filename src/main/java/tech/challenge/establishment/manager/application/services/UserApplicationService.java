package tech.challenge.establishment.manager.application.services;

import org.springframework.stereotype.Service;
import tech.challenge.establishment.manager.application.usecases.CreateUserUseCaseImpl;
import tech.challenge.establishment.manager.domain.entities.Address;
import tech.challenge.establishment.manager.domain.entities.User;
import tech.challenge.establishment.manager.domain.usecases.user.FindUserUseCase;
import tech.challenge.establishment.manager.domain.usecases.user.UpdateUserUseCase;
import tech.challenge.establishment.manager.domain.valueobjects.UserId;
import tech.challenge.establishment.manager.domain.entities.Role;

@Service
public class UserApplicationService {

    private final CreateUserUseCaseImpl createUserUseCase;
    private final FindUserUseCase findUserUseCase;
    private final UpdateUserUseCase updateUserUseCase;

    public UserApplicationService(CreateUserUseCaseImpl createUserUseCase,
                                  FindUserUseCase findUserUseCase,
                                  UpdateUserUseCase updateUserUseCase) {
        this.createUserUseCase = createUserUseCase;
        this.findUserUseCase = findUserUseCase;
        this.updateUserUseCase = updateUserUseCase;
    }

    public User createCustomer(String name, String email, String login, String password, Address address) {
    return createUserUseCase.execute(name, email, login, password, address, Role.RoleName.USER);
    }

    public User createRestaurantOwner(String name, String email, String login, String password, Address address) {
        return createUserUseCase.execute(name, email, login, password, address, Role.RoleName.RESTAURANT_OWNER);
    }

    public User createAdmin(String name, String email, String login, String password, Address address) {
        return createUserUseCase.execute(name, email, login, password, address, Role.RoleName.ADMIN);
    }

    public User findUser(UserId userId) {
        return findUserUseCase.execute(userId);
    }

    public User updateUser(UserId userId, String name, String email) {
        return updateUserUseCase.execute(userId, name, email);
    }
}