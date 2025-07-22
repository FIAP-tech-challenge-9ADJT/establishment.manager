package tech.challenge.establishment.manager.services;

import tech.challenge.establishment.manager.entities.User;

public interface TokenService {
    String generateToken(User user);
    String verifyToken(String token);
}