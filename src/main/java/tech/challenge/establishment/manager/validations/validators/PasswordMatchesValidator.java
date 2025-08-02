package tech.challenge.establishment.manager.validations.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;
import tech.challenge.establishment.manager.dtos.user.ChangePasswordDTO;
import tech.challenge.establishment.manager.validations.PasswordMatches;

import java.lang.reflect.Field;

@Component
public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

    private String passwordField;
    private String confirmPasswordField;

    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
        this.passwordField = constraintAnnotation.password();
        this.confirmPasswordField = constraintAnnotation.confirmPassword();
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        if (obj == null) {
            return true;
        }

        try {
            // Tratamento especial para ChangePasswordDTO
            if (obj instanceof ChangePasswordDTO dto) {
                return dto.newPassword() != null && dto.newPassword().equals(dto.newPasswordConfirmation());
            }

            // Tratamento genérico usando reflection
            Field passwordFieldObj = obj.getClass().getDeclaredField(passwordField);
            Field confirmPasswordFieldObj = obj.getClass().getDeclaredField(confirmPasswordField);
            
            passwordFieldObj.setAccessible(true);
            confirmPasswordFieldObj.setAccessible(true);
            
            Object password = passwordFieldObj.get(obj);
            Object confirmPassword = confirmPasswordFieldObj.get(obj);
            
            if (password == null && confirmPassword == null) {
                return true;
            }
            
            return password != null && password.equals(confirmPassword);
            
        } catch (Exception e) {
            return false;
        }
    }
}