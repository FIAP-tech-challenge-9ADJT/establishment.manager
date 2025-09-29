package tech.challenge.establishment.manager.domain.exceptions;

import tech.challenge.establishment.manager.domain.valueobjects.UserId;

public class UserNotFoundException extends DomainException {
    
    public UserNotFoundException(UserId userId) {
        super("User not found with ID: " + userId.value());
    }
    
    public UserNotFoundException(String login) {
        super("User not found with login: " + login);
    }
}