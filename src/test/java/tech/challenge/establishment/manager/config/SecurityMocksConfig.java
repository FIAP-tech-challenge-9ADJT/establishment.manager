package tech.challenge.establishment.manager.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import tech.challenge.establishment.manager.infrastructure.security.TokenService;

@TestConfiguration
public class SecurityMocksConfig {
    @Bean
    public TokenService tokenService() {
        return org.mockito.Mockito.mock(TokenService.class);
    }
}
