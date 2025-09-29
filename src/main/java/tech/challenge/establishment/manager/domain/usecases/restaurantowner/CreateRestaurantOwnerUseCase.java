package tech.challenge.establishment.manager.application.usecases;

import org.springframework.stereotype.Service;
import tech.challenge.establishment.manager.domain.entities.Address;
import tech.challenge.establishment.manager.domain.entities.User;
import tech.challenge.establishment.manager.domain.entities.Role;
import tech.challenge.establishment.manager.domain.repositories.RoleRepository;
import tech.challenge.establishment.manager.domain.repositories.UserRepository;

@Service
public class CreateRestaurantOwnerUseCase {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public CreateRestaurantOwnerUseCase(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public User execute(String name, String email, String login, String password, Address address, Role.RoleName roleName) {
        Role role = roleRepository.findByName(roleName)
            .orElseThrow(() -> new RuntimeException("Role não encontrada: " + roleName));
        User user = User.create(name, email, login, password, address);
        user = user.addRole(role);

        userRepository.save(user);

        return user;
    }
}