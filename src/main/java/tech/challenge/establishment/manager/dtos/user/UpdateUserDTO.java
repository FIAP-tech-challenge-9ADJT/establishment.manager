package tech.challenge.establishment.manager.dtos.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import tech.challenge.establishment.manager.dtos.address.UpdateAddressDTO;

public record UpdateUserDTO(

        @Size(max = 100, message = "Name must have at most 100 characters") String name,

        @Email(message = "Email must be valid") String email,

        @Size(min = 4, max = 50, message = "Login must have between 4 and 50 characters") String login,

        @Valid UpdateAddressDTO address) {
}
