package tech.challenge.establishment.manager.infrastructure.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AccessTokenFilter accessTokenFilter;
    private final UserDetailsService userDetailsService;

    public SecurityConfig(AccessTokenFilter accessTokenFilter, UserDetailsService userDetailsService) {
        this.accessTokenFilter = accessTokenFilter;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .authorizeHttpRequests(req -> {

                // ENDPOINTS PÚBLICOS - criação de usuários
                req.requestMatchers(HttpMethod.POST, "/users").permitAll();
                req.requestMatchers(HttpMethod.POST, "/restaurant-owners").permitAll();
                req.requestMatchers(HttpMethod.POST, "/admin").permitAll();
                req.requestMatchers("/auth/login", "/auth").permitAll();
                req.requestMatchers("/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**",
                                "/swagger-resources/**", "/webjars/**").permitAll();

                // ADMIN
                req.requestMatchers(HttpMethod.GET, "/admin/**").hasRole("ADMIN");
                req.requestMatchers(HttpMethod.PUT, "/admin/**").hasRole("ADMIN");
                req.requestMatchers(HttpMethod.DELETE, "/admin/**").hasRole("ADMIN");

                // USUÁRIO (CUSTOMER)
                req.requestMatchers(HttpMethod.GET, "/users").hasAnyRole("USER", "ADMIN");
                req.requestMatchers(HttpMethod.PUT, "/users/**").hasAnyRole("USER", "ADMIN");
                req.requestMatchers(HttpMethod.POST, "/users/change-password").hasAnyRole("USER", "ADMIN");
                req.requestMatchers(HttpMethod.GET, "/users/address").hasAnyRole("USER", "ADMIN");
                req.requestMatchers(HttpMethod.PUT, "/users/address").hasAnyRole("USER", "ADMIN");

                // DONO DO RESTAURANTE (RESTAURANT_OWNER)
                req.requestMatchers(HttpMethod.GET, "/restaurant-owners/**").hasAnyRole("RESTAURANT_OWNER", "ADMIN");
                req.requestMatchers(HttpMethod.PUT, "/restaurant-owners/**").hasAnyRole("RESTAURANT_OWNER", "ADMIN");
                req.requestMatchers(HttpMethod.POST, "/restaurant-owners/**").hasAnyRole("RESTAURANT_OWNER", "ADMIN");
                req.requestMatchers(HttpMethod.GET, "/restaurant-owners/address").hasAnyRole("RESTAURANT_OWNER", "ADMIN");
                req.requestMatchers(HttpMethod.PUT, "/restaurant-owners/address").hasAnyRole("RESTAURANT_OWNER", "ADMIN");

                // RESTAURANTES
                req.requestMatchers(HttpMethod.POST, "/restaurants").hasAnyRole("RESTAURANT_OWNER", "ADMIN");
                req.requestMatchers(HttpMethod.PUT, "/restaurants/**").hasAnyRole("RESTAURANT_OWNER", "ADMIN");
                req.requestMatchers(HttpMethod.DELETE, "/restaurants/**").hasAnyRole("RESTAURANT_OWNER", "ADMIN");
                req.requestMatchers(HttpMethod.GET, "/restaurants/**").hasAnyRole("USER", "RESTAURANT_OWNER", "ADMIN");
                req.requestMatchers(HttpMethod.GET, "/restaurants").hasAnyRole("USER", "RESTAURANT_OWNER", "ADMIN");

                // QUALQUER OUTRO REQUER AUTENTICAÇÃO
                req.anyRequest().authenticated();
            })
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .csrf(csrf -> csrf.disable())
            .addFilterBefore(accessTokenFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}