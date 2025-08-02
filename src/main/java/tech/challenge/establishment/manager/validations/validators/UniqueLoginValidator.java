package tech.challenge.establishment.manager.validations.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tech.challenge.establishment.manager.repositories.UserRepository;
import tech.challenge.establishment.manager.validations.UniqueLogin;

@Component
public class UniqueLoginValidator implements ConstraintValidator<UniqueLogin, String> {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void initialize(UniqueLogin constraintAnnotation) {
        // Inicialização se necessária
    }

    @Override
    public boolean isValid(String login, ConstraintValidatorContext context) {
        if (login == null || login.trim().isEmpty()) {
            return true; // Deixa a validação @NotBlank cuidar disso
        }
        return !userRepository.existsByLogin(login);
    }
}