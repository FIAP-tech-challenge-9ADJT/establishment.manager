package tech.challenge.establishment.manager.services;

import tech.challenge.establishment.manager.dtos.address.CreateAddressDTO;
import tech.challenge.establishment.manager.dtos.address.UpdateAddressDTO;
import tech.challenge.establishment.manager.entities.Address;
import tech.challenge.establishment.manager.entities.User;

public interface AddressService {
    Address findById(Long id);
    Address findByUserId(Long userId);
    Address create(CreateAddressDTO dto, User user);
    Address update(Long id, UpdateAddressDTO dto, User authenticatedUser);
    void delete(Long id);
}
