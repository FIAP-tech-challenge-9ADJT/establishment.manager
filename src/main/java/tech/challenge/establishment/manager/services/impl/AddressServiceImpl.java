package tech.challenge.establishment.manager.services.impl;

import tech.challenge.establishment.manager.dtos.address.CreateAddressDTO;
import tech.challenge.establishment.manager.dtos.address.UpdateAddressDTO;
import tech.challenge.establishment.manager.entities.Address;
import tech.challenge.establishment.manager.repositories.AddressRepository;
import tech.challenge.establishment.manager.services.AddressService;

import java.time.LocalDateTime;

public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;

    public AddressServiceImpl(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Override
    public Address save(CreateAddressDTO createAddressDTO) {
        var entity = new Address(null, createAddressDTO.street(), createAddressDTO.city(), createAddressDTO.postalCode(), createAddressDTO.number(), LocalDateTime.now(), null, null);
        return addressRepository.save(entity);
    }

    @Override
    public Address update(Long id, UpdateAddressDTO updateAddressDTO) {
        var addressEntity = addressRepository.findById(id);

        if (addressEntity.isPresent()) {
            var address = addressEntity.get();
            if (updateAddressDTO.street() != null && !updateAddressDTO.street().isEmpty()) {
                address.setStreet(updateAddressDTO.street());
            }

            if (updateAddressDTO.city() != null && !updateAddressDTO.city().isEmpty()) {
                address.setCity(updateAddressDTO.city());
            }

            if (updateAddressDTO.postalCode() != null && !updateAddressDTO.postalCode().isEmpty()) {
                address.setPostalCode(updateAddressDTO.postalCode());
            }

            if (updateAddressDTO.number() != null && !updateAddressDTO.number().isEmpty()) {
                address.setNumber(updateAddressDTO.number());
            }

            address.setUpdatedAt(LocalDateTime.now());

            return addressRepository.save(address);
        }else{
            return null;
        }
    }

    @Override
    public Address findById(Long id) {
        return addressRepository.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        var addressExists = addressRepository.existsById(id);
        if(addressExists){
            addressRepository.deleteById(id);
        }
    }
}
