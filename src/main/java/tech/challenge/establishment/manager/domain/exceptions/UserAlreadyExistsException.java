package tech.challenge.establishment.manager.domain.exceptions;

public class UserAlreadyExistsException extends DomainException {
    
    public UserAlreadyExistsException(String field, String value) {
        super("User already exists with " + field + ": " + value);
    }
}