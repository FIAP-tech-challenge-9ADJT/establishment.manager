package tech.challenge.establishment.manager.dtos.address;

import tech.challenge.establishment.manager.entities.Address;

public record AddressResponseDTO(
        Long id,
        String street,
        String city,
        String postalCode,
        String number
) {
    public static AddressResponseDTO from(Address address) {
        return new AddressResponseDTO(
                address.getId(),
                address.getStreet(),
                address.getCity(),
                address.getPostalCode(),
                address.getNumber()
        );
    }
}