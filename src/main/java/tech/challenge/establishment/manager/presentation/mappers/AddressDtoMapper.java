package tech.challenge.establishment.manager.presentation.mappers;

import tech.challenge.establishment.manager.domain.entities.Address;
import tech.challenge.establishment.manager.domain.valueobjects.PostalCode;
import tech.challenge.establishment.manager.domain.valueobjects.UserId;
import tech.challenge.establishment.manager.presentation.dtos.address.CreateAddressDTO;
import tech.challenge.establishment.manager.presentation.dtos.address.AddressResponseDTO;
import tech.challenge.establishment.manager.presentation.dtos.address.UpdateAddressDTO;

public class AddressDtoMapper {
    
    public static Address fromCreateDto(CreateAddressDTO dto) {
        return Address.create(
            dto.street(),
            dto.city(),
            dto.postalCode(),
            dto.number(),
            null // UserId será definido posteriormente
        );
    }
    
    public static Address fromCreateDto(CreateAddressDTO dto, UserId userId) {
        return Address.create(
            dto.street(),
            dto.city(),
            dto.postalCode(),
            dto.number(),
            userId
        );
    }

    public static Address fromUpdateDto(UpdateAddressDTO dto, Address existingAddress) {
        if (dto == null) return existingAddress;

        return new Address(
                existingAddress.getId(),
                dto.street() != null ? dto.street() : existingAddress.getStreet(),
                dto.city() != null ? dto.city() : existingAddress.getCity(),
                dto.postalCode() != null ? new PostalCode(dto.postalCode()) : existingAddress.getPostalCode(),
                dto.number() != null ? dto.number() : existingAddress.getNumber(),
                existingAddress.getUserId()
        );
    }
    
    public static AddressResponseDTO toResponseDto(Address address) {
        return new AddressResponseDTO(
            address.getId() != null ? address.getId().value() : null,
            address.getStreet(),
            address.getCity(),
            address.getPostalCode().value(),
            address.getNumber()
        );
    }
}