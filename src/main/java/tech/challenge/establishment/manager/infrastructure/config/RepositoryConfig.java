package tech.challenge.establishment.manager.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tech.challenge.establishment.manager.domain.repositories.AddressRepository;
import tech.challenge.establishment.manager.domain.repositories.RoleRepository;
import tech.challenge.establishment.manager.domain.repositories.UserRepository;
import tech.challenge.establishment.manager.infrastructure.persistence.repositories.AddressJpaRepository;
import tech.challenge.establishment.manager.infrastructure.persistence.repositories.AddressRepositoryImpl;
import tech.challenge.establishment.manager.infrastructure.persistence.repositories.RoleJpaRepository;
import tech.challenge.establishment.manager.infrastructure.persistence.repositories.RoleRepositoryImpl;
import tech.challenge.establishment.manager.infrastructure.persistence.repositories.UserJpaRepository;
import tech.challenge.establishment.manager.infrastructure.persistence.repositories.UserRepositoryImpl;

@Configuration
public class RepositoryConfig {
    
    @Bean
    public UserRepository userRepository(UserJpaRepository userJpaRepository) {
        return new UserRepositoryImpl(userJpaRepository);
    }
    
    @Bean
    public RoleRepository roleRepository(RoleJpaRepository roleJpaRepository) {
        return new RoleRepositoryImpl(roleJpaRepository);
    }
    
    @Bean
    public AddressRepository addressRepository(AddressJpaRepository addressJpaRepository) {
        return new AddressRepositoryImpl(addressJpaRepository);
    }
}