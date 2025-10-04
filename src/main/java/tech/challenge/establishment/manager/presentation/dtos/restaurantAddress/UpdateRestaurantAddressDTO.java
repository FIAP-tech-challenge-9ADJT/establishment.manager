package tech.challenge.establishment.manager.presentation.dtos.restaurantAddress;

import jakarta.validation.constraints.Size;
import tech.challenge.establishment.manager.validations.ValidCEP;

public record UpdateRestaurantAddressDTO(

        @Size(max = 100, message = "Rua deve ter no máximo 100 caracteres")
        String street,

        @Size(max = 100, message = "Cidade deve ter no máximo 100 caracteres")
        String city,

        @ValidCEP
        String postalCode,

        @Size(max = 10, message = "Número deve ter no máximo 10 caracteres")
        String number) {
}
