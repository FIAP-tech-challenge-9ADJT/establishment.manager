package tech.challenge.establishment.manager.infrastructure.security;

import tech.challenge.establishment.manager.infrastructure.persistence.entities.UserJpaEntity;

public interface TokenService {
    String generateToken(UserJpaEntity user);
    String verifyToken(String token);
}