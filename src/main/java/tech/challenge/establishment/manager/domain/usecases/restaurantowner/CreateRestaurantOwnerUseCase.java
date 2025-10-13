package tech.challenge.establishment.manager.domain.usecases.restaurantowner;

import tech.challenge.establishment.manager.domain.entities.Address;
import tech.challenge.establishment.manager.domain.entities.User;
import tech.challenge.establishment.manager.domain.entities.Role;
import tech.challenge.establishment.manager.domain.exceptions.UserAlreadyExistsException;
import tech.challenge.establishment.manager.domain.repositories.RoleRepository;
import tech.challenge.establishment.manager.domain.repositories.UserRepository;
import tech.challenge.establishment.manager.domain.valueobjects.Email;
import tech.challenge.establishment.manager.domain.valueobjects.Login;

public class CreateRestaurantOwnerUseCase {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public CreateRestaurantOwnerUseCase(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public User execute(String name, String email, String login, String password, Address address, Role.RoleName roleName) {
        // Validar se email já existe
        if (userRepository.existsByEmail(Email.of(email))) {
            throw new UserAlreadyExistsException("email", email);
        }
        
        // Validar se login já existe
        if (userRepository.existsByLogin(Login.of(login))) {
            throw new UserAlreadyExistsException("login", login);
        }
        
        // Buscar a role
        Role role = roleRepository.findByName(roleName)
            .orElseThrow(() -> new RuntimeException("Role não encontrada: " + roleName));
        
        // Criar usuário
        User user = User.create(name, email, login, password, address);
        
        // Adicionar a role especificada
        user = user.addRole(role);

        return userRepository.save(user);
    }
}