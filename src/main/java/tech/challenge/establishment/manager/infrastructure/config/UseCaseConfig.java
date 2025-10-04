package tech.challenge.establishment.manager.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import tech.challenge.establishment.manager.application.usecases.CreateUserUseCaseImpl;
import tech.challenge.establishment.manager.domain.repositories.RoleRepository;
import tech.challenge.establishment.manager.domain.repositories.UserRepository;
import tech.challenge.establishment.manager.domain.usecases.auth.AuthenticateUserUseCase;
import tech.challenge.establishment.manager.domain.usecases.auth.ChangePasswordUseCase;
import tech.challenge.establishment.manager.domain.usecases.user.FindUserUseCase;
import tech.challenge.establishment.manager.domain.usecases.user.UpdateUserUseCase;
import tech.challenge.establishment.manager.domain.usecases.admin.DeleteUserUseCase;

@Configuration
public class UseCaseConfig {
    
    @Bean
    public CreateUserUseCaseImpl createUserUseCase(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder) {
        return new CreateUserUseCaseImpl(userRepository, roleRepository, passwordEncoder);
    }
    
    @Bean
    public FindUserUseCase findUserUseCase(UserRepository userRepository) {
        return new FindUserUseCase(userRepository);
    }
    
    @Bean
    public UpdateUserUseCase updateUserUseCase(UserRepository userRepository) {
        return new UpdateUserUseCase(userRepository);
    }
    
    @Bean
    public AuthenticateUserUseCase authenticateUserUseCase(UserRepository userRepository) {
        return new AuthenticateUserUseCase(userRepository);
    }
    
    @Bean
    public ChangePasswordUseCase changePasswordUseCase(UserRepository userRepository) {
        return new ChangePasswordUseCase(userRepository);
    }
    
    @Bean
    public DeleteUserUseCase deleteUserUseCase(UserRepository userRepository) {
        return new DeleteUserUseCase(userRepository);
    }
}