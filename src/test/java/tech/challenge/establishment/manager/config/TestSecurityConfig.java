package tech.challenge.establishment.manager.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import tech.challenge.establishment.manager.infrastructure.security.TokenService;

@TestConfiguration
@EnableWebSecurity
public class TestSecurityConfig {

    @MockBean
    private TokenService tokenService;

    @Bean
    @Primary
    public PasswordEncoder testPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Primary
    public SecurityFilterChain testSecurityFilterChain(HttpSecurity http) throws Exception {
        return http
            .authorizeHttpRequests(req -> req
                .requestMatchers("/auth/**").permitAll()
                .requestMatchers("/users").permitAll()
                .anyRequest().authenticated())
            .csrf(csrf -> csrf.disable())
            .build();
    }

    @Bean
    @Primary
    public UserDetailsService testUserDetailsService() {
        UserDetails user = User.withDefaultPasswordEncoder()
                .username("testuser")
                .password("password")
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(user);
    }

    @Bean
    @Primary
    public AuthenticationManager testAuthenticationManager(
            UserDetailsService userDetailsService, 
            PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(authProvider);
    }
}
