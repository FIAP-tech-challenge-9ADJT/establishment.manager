package tech.challenge.establishment.manager.validations;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tech.challenge.establishment.manager.dtos.address.CreateAddressDTO;
import tech.challenge.establishment.manager.dtos.user.ChangePasswordDTO;
import tech.challenge.establishment.manager.validations.validators.ValidCPFValidator;
import tech.challenge.establishment.manager.validations.validators.ValidPhoneValidator;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ValidationTest {

    @Autowired
    private Validator validator;

    @Test
    void testValidCEP() {
        // CEP válido
        CreateAddressDTO validAddress = new CreateAddressDTO(
            "Rua das Flores, 123",
            "São Paulo",
            "01234567",
            "123"
        );
        Set<ConstraintViolation<CreateAddressDTO>> violations = validator.validate(validAddress);
        assertTrue(violations.isEmpty());

        // CEP inválido
        CreateAddressDTO invalidAddress = new CreateAddressDTO(
            "Rua das Flores, 123",
            "São Paulo",
            "123",
            "123"
        );
        violations = validator.validate(invalidAddress);
        assertFalse(violations.isEmpty());
    }

    @Test
    void testPasswordMatches() {
        // Senhas que coincidem
        ChangePasswordDTO validPassword = new ChangePasswordDTO(
            "oldPassword",
            "newPassword123",
            "newPassword123"
        );
        Set<ConstraintViolation<ChangePasswordDTO>> violations = validator.validate(validPassword);
        assertTrue(violations.stream().noneMatch(v -> v.getMessage().contains("não coincidem")));

        // Senhas que não coincidem
        ChangePasswordDTO invalidPassword = new ChangePasswordDTO(
            "oldPassword",
            "newPassword123",
            "differentPassword"
        );
        violations = validator.validate(invalidPassword);
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("não coincidem")));
    }

    @Test
    void testValidCPF() {
        ValidCPFValidator validator = new ValidCPFValidator();
        
        // CPF válido
        assertTrue(validator.isValid("11144477735", null));
        assertTrue(validator.isValid("111.444.777-35", null));
        
        // CPF inválido
        assertFalse(validator.isValid("11111111111", null));
        assertFalse(validator.isValid("123456789", null));
        assertFalse(validator.isValid("12345678901", null));
    }

    @Test
    void testValidPhone() {
        ValidPhoneValidator validator = new ValidPhoneValidator();
        
        // Telefones válidos
        assertTrue(validator.isValid("11987654321", null)); // Celular
        assertTrue(validator.isValid("1134567890", null));  // Fixo
        assertTrue(validator.isValid("(11) 98765-4321", null)); // Celular formatado
        assertTrue(validator.isValid("(11) 3456-7890", null));  // Fixo formatado
        
        // Telefones inválidos
        assertFalse(validator.isValid("123456789", null));   // Muito curto
        assertFalse(validator.isValid("123456789012", null)); // Muito longo
        assertFalse(validator.isValid("0987654321", null));  // Código de área inválido
        assertFalse(validator.isValid("1187654321", null));  // Celular sem 9
        assertFalse(validator.isValid("1194567890", null));  // Fixo com 9
    }
}