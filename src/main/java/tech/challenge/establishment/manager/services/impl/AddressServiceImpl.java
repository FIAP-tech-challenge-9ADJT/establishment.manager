package tech.challenge.establishment.manager.services.impl;

import org.springframework.stereotype.Service;
import tech.challenge.establishment.manager.dtos.address.CreateAddressDTO;
import tech.challenge.establishment.manager.dtos.address.UpdateAddressDTO;
import tech.challenge.establishment.manager.entities.Address;
import tech.challenge.establishment.manager.repositories.AddressRepository;
import tech.challenge.establishment.manager.services.AddressService;

import java.time.LocalDateTime;

@Service
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;

    public AddressServiceImpl(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Override
    public Address save(CreateAddressDTO createAddressDTO) {
        try{
            var entity = new Address(createAddressDTO);
            return addressRepository.save(entity);
        }catch(Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Address update(Long id, UpdateAddressDTO updateAddressDTO) {
        try{
            var addressEntity = addressRepository.findById(id);

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

        }catch(Exception e){
            throw new RuntimeException(e.getMessage());
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
