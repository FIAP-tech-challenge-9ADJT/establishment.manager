package tech.challenge.establishment.manager.dtos.address;

import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;

public record UpdateAddressDTO(

        @Size(max = 100, message = "Street must have at most 100 characters") String street,

        @Size(max = 100, message = "City must have at most 100 characters") String city,

        @Size(min = 8, max = 8, message = "Postal Code must have exactly 8 digits") @Pattern(regexp = "^[0-9]{8}$", message = "Postal Code must contain only digits") String postalCode,

        @Size(max = 10, message = "Number must have at most 10 characters") String number) {
}