package tech.challenge.establishment.manager.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tech.challenge.establishment.manager.domain.repositories.*;
import tech.challenge.establishment.manager.infrastructure.persistence.repositories.*;

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

    @Bean
    public RestaurantRepository restaurantRepository(RestaurantJpaRepository restaurantJpaRepository) {
        return new RestaurantRepositoryImpl(restaurantJpaRepository);
    }

    @Bean
    public RestaurantAddressRepository restaurantAddressRepository(RestaurantAddressJpaRepository restaurantAddressJpaRepository) {
        return new RestaurantAddressRepositoryImpl(restaurantAddressJpaRepository);
    }

    @Bean
    public MenuItemRepository menuItemRepository(MenuItemJpaRepository menuItemJpaRepository) {
        return new MenuItemRepositoryImpl(menuItemJpaRepository);
    }
}