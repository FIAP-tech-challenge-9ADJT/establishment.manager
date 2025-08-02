package tech.challenge.establishment.manager.validations.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tech.challenge.establishment.manager.repositories.UserRepository;
import tech.challenge.establishment.manager.validations.UniqueEmail;

@Component
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void initialize(UniqueEmail constraintAnnotation) {
        // Inicialização se necessária
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email == null || email.trim().isEmpty()) {
            return true; // Deixa a validação @NotBlank cuidar disso
        }
        return !userRepository.existsByEmail(email);
    }
}