package tech.challenge.establishment.manager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AccessTokenFilter accessTokenFilter;

    public SecurityConfig(AccessTokenFilter accessTokenFilter) {
        this.accessTokenFilter = accessTokenFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .authorizeHttpRequests(req -> {
                req.requestMatchers(HttpMethod.POST, "/users").permitAll(); // cadastro de usuário
                req.requestMatchers("/auth/login", "/auth").permitAll();
                req.requestMatchers("/admin").permitAll();
                req.requestMatchers(HttpMethod.GET, "/admin/**").hasRole("ADMIN");
                req.requestMatchers(HttpMethod.PUT, "/admin/**").hasRole("ADMIN");
                req.requestMatchers(HttpMethod.DELETE, "/admin/**").hasRole("ADMIN");
                req.requestMatchers(HttpMethod.GET, "/users").hasAnyRole("USER", "ADMIN");
                req.requestMatchers(HttpMethod.PUT, "/users/**").hasAnyRole("USER", "ADMIN");
                req.requestMatchers(HttpMethod.POST, "/users/change-password").hasAnyRole("USER", "ADMIN");
                req.requestMatchers(HttpMethod.GET, "/users/address").hasAnyRole("USER", "ADMIN");
                req.requestMatchers(HttpMethod.PUT, "/users/address").hasAnyRole("USER", "ADMIN");
                req.anyRequest().authenticated();
            })
            .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .csrf(csrf -> csrf.disable())
            .addFilterBefore(accessTokenFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}