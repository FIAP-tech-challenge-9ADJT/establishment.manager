package tech.challenge.establishment.manager.dtos.address;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import tech.challenge.establishment.manager.validations.ValidCEP;

public record CreateAddressDTO(

                @NotBlank(message = "Rua é obrigatória") 
                @Size(max = 100, message = "Rua deve ter no máximo 100 caracteres") 
                String street,

                @NotBlank(message = "Cidade é obrigatória") 
                @Size(max = 100, message = "Cidade deve ter no máximo 100 caracteres") 
                String city,

                @NotBlank(message = "CEP é obrigatório") 
                @ValidCEP
                String postalCode,

                @NotBlank(message = "Número é obrigatório") 
                @Size(max = 10, message = "Número deve ter no máximo 10 caracteres") 
                String number) {
}