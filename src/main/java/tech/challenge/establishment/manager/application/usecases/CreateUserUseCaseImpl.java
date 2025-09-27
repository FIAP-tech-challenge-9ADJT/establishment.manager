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
    
    @Override
    public User execute(String name, String email, String login, String password, Address address) {
        // Validar se email já existe
        if (super.userRepository.existsByEmail(Email.of(email))) {
            throw new UserAlreadyExistsException("email", email);
        }
        
        // Validar se login já existe
        if (super.userRepository.existsByLogin(Login.of(login))) {
            throw new UserAlreadyExistsException("login", login);
        }
        
        // Codificar senha
        String encodedPassword = passwordEncoder.encode(password);
        
        // Criar usuário
        User user = User.create(name, email, login, encodedPassword, address);
        
        // Adicionar role padrão USER
        Role userRole = super.roleRepository.findByName(Role.RoleName.USER)
            .orElseThrow(() -> new RuntimeException("Default USER role not found"));
        
        user = user.addRole(userRole);
        
        // Salvar usuário
        return super.userRepository.save(user);
    }
}