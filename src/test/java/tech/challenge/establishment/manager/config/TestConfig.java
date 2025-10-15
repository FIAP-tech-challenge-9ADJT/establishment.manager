package tech.challenge.establishment.manager.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import tech.challenge.establishment.manager.domain.repositories.*;
import tech.challenge.establishment.manager.domain.usecases.auth.AuthenticateUserUseCase;
import tech.challenge.establishment.manager.domain.usecases.auth.ChangePasswordUseCase;
import tech.challenge.establishment.manager.domain.usecases.restaurant.*;
import tech.challenge.establishment.manager.domain.usecases.restaurantowner.CreateRestaurantOwnerUseCase;
import tech.challenge.establishment.manager.domain.usecases.user.*;
import tech.challenge.establishment.manager.infrastructure.persistence.repositories.*;
import tech.challenge.establishment.manager.infrastructure.security.TokenService;

import static org.mockito.Mockito.mock;

@TestConfiguration
public class TestConfig {

    @Bean
    @Primary
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Primary
    public UserRepository userRepository() {
        return mock(UserRepository.class);
    }

    @Bean
    @Primary
    public RoleRepository roleRepository() {
        return mock(RoleRepository.class);
    }

    @Bean
    @Primary
    public AddressRepository addressRepository() {
        return mock(AddressRepository.class);
    }

    @Bean
    @Primary
    public RestaurantRepository restaurantRepository() {
        return mock(RestaurantRepository.class);
    }

    @Bean
    @Primary
    public RestaurantAddressRepository restaurantAddressRepository() {
        return mock(RestaurantAddressRepository.class);
    }

    @Bean
    @Primary
    public MenuItemRepository menuItemRepository() {
        return mock(MenuItemRepository.class);
    }

    @Bean
    @Primary
    public UserJpaRepository userJpaRepository() {
        return mock(UserJpaRepository.class);
    }

    @Bean
    @Primary
    public RoleJpaRepository roleJpaRepository() {
        return mock(RoleJpaRepository.class);
    }

    @Bean
    @Primary
    public AddressJpaRepository addressJpaRepository() {
        return mock(AddressJpaRepository.class);
    }

    @Bean
    @Primary
    public RestaurantJpaRepository restaurantJpaRepository() {
        return mock(RestaurantJpaRepository.class);
    }

    @Bean
    @Primary
    public RestaurantAddressJpaRepository restaurantAddressJpaRepository() {
        return mock(RestaurantAddressJpaRepository.class);
    }

    @Bean
    @Primary
    public MenuItemJpaRepository menuItemJpaRepository() {
        return mock(MenuItemJpaRepository.class);
    }

    @Bean
    @Primary
    public TokenService tokenService() {
        return mock(TokenService.class);
    }

    @Bean
    @Primary
    public CreateUserUseCase createUserUseCase() {
        return new CreateUserUseCase(userRepository(), roleRepository());
    }

    @Bean
    @Primary
    public FindUserUseCase findUserUseCase() {
        return new FindUserUseCase(userRepository());
    }

    @Bean
    @Primary
    public UpdateUserUseCase updateUserUseCase() {
        return new UpdateUserUseCase(userRepository());
    }

    @Bean
    @Primary
    public AuthenticateUserUseCase authenticateUserUseCase() {
        return new AuthenticateUserUseCase(userRepository());
    }

    @Bean
    @Primary
    public ChangePasswordUseCase changePasswordUseCase() {
        return new ChangePasswordUseCase(userRepository());
    }

    @Bean
    @Primary
    public CreateRestaurantOwnerUseCase createRestaurantOwnerUseCase() {
        return new CreateRestaurantOwnerUseCase(userRepository(), roleRepository());
    }

    @Bean
    @Primary
    public CreateRestaurantUseCase createRestaurantUseCase() {
        return mock(CreateRestaurantUseCase.class);
    }

    @Bean
    @Primary
    public FindAllRestaurantsUseCase findAllRestaurantsUseCase() {
        return mock(FindAllRestaurantsUseCase.class);
    }

    @Bean
    @Primary
    public FindRestaurantByIdUseCase findRestaurantByIdUseCase() {
        return mock(FindRestaurantByIdUseCase.class);
    }

    @Bean
    @Primary
    public FindRestaurantByNameUseCase findRestaurantByNameUseCase() {
        return mock(FindRestaurantByNameUseCase.class);
    }

    @Bean
    @Primary
    public UpdateRestaurantUseCase updateRestaurantUseCase() {
        return mock(UpdateRestaurantUseCase.class);
    }

    @Bean
    @Primary
    public DeleteRestaurantUseCase deleteRestaurantUseCase() {
        return mock(DeleteRestaurantUseCase.class);
    }
}
