package tech.challenge.establishment.manager.services;

import tech.challenge.establishment.manager.dtos.address.CreateAddressDTO;
import tech.challenge.establishment.manager.dtos.address.UpdateAddressDTO;
import tech.challenge.establishment.manager.entities.Address;

public interface AddressService {
    Address findById(Long id);

    Address save(CreateAddressDTO address);

    Address update(Long id, UpdateAddressDTO address);

    void delete(Long id);
}
