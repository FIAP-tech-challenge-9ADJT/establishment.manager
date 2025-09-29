package tech.challenge.establishment.manager.application.usecases;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tech.challenge.establishment.manager.domain.entities.Address;
import tech.challenge.establishment.manager.domain.entities.Role;
import tech.challenge.establishment.manager.domain.entities.User;
import tech.challenge.establishment.manager.domain.exceptions.UserAlreadyExistsException;
import tech.challenge.establishment.manager.domain.repositories.RoleRepository;
import tech.challenge.establishment.manager.domain.repositories.UserRepository;
import tech.challenge.establishment.manager.domain.usecases.user.CreateUserUseCase;
import tech.challenge.establishment.manager.domain.valueobjects.Email;
import tech.challenge.establishment.manager.domain.valueobjects.Login;

@Service
public class CreateUserUseCaseImpl extends CreateUserUseCase {

    private final PasswordEncoder passwordEncoder;

    public CreateUserUseCaseImpl(UserRepository userRepository, 
                                 RoleRepository roleRepository,
                                 PasswordEncoder passwordEncoder) {
        super(userRepository, roleRepository);
        this.passwordEncoder = passwordEncoder;
    }

    public User execute(String name, String email, String login, String password, Address address, Role.RoleName roleName) {
        if (super.userRepository.existsByEmail(Email.of(email))) {
            throw new UserAlreadyExistsException("email", email);
        }
        if (super.userRepository.existsByLogin(Login.of(login))) {
            throw new UserAlreadyExistsException("login", login);
        }

        String encodedPassword = passwordEncoder.encode(password);

        User user = User.create(name, email, login, encodedPassword, address);

        Role role = super.roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Role '" + roleName + "' not found in DB. Check RoleRepository."));
        user = user.addRole(role);
        return super.userRepository.save(user);
    }
}