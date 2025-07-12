package tech.challenge.establishment.manager.dtos.address;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;
import tech.challenge.establishment.manager.entities.User;

public record CreateAddressDTO(

                @NotBlank(message = "Street is required") @Size(max = 100, message = "Street must have at most 100 characters") String street,

                @NotBlank(message = "City is required") @Size(max = 100, message = "City must have at most 100 characters") String city,

                @NotBlank(message = "Postal Code is required") @Size(min = 8, max = 8, message = "Postal Code must have exactly 8 digits") @Pattern(regexp = "^[0-9]{8}$", message = "Postal Code must contain only digits") String postalCode,

                @NotBlank(message = "Number is required") @Size(max = 10, message = "Number must have at most 10 characters") String number,

                @NotBlank(message = "User is required") User user) {
}
