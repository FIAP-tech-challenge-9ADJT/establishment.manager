package tech.challenge.establishment.manager.presentation.dtos.address;

public record AddressResponseDTO(
        Long id,
        String street,
        String city,
        String postalCode,
        String number
) {
}