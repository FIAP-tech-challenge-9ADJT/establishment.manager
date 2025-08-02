package tech.challenge.establishment.manager.dtos.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import tech.challenge.establishment.manager.dtos.address.CreateAddressDTO;
import tech.challenge.establishment.manager.validations.UniqueEmail;
import tech.challenge.establishment.manager.validations.UniqueLogin;

public record CreateUserDTO(

        @NotBlank(message = "Name is required") 
        @Size(max = 100, message = "Name must have at most 100 characters") 
        String name,

        @NotBlank(message = "Email is required") 
        @Email(message = "Email must be valid") 
        @UniqueEmail
        String email,

        @NotBlank(message = "Login is required") 
        @Size(min = 4, max = 50, message = "Login must have between 4 and 50 characters") 
        @UniqueLogin
        String login,

        @NotBlank(message = "Password is required") 
        @Size(min = 6, message = "Password must have at least 6 characters") 
        String password,

        @NotNull(message = "Address is required") 
        @Valid CreateAddressDTO address) {
}