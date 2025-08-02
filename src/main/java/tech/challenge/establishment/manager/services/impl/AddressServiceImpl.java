package tech.challenge.establishment.manager.services.impl;

import org.springframework.stereotype.Service;
import tech.challenge.establishment.manager.dtos.address.CreateAddressDTO;
import tech.challenge.establishment.manager.dtos.address.UpdateAddressDTO;
import tech.challenge.establishment.manager.entities.Address;
import tech.challenge.establishment.manager.entities.User;
import tech.challenge.establishment.manager.repositories.AddressRepository;
import tech.challenge.establishment.manager.services.AddressService;
import tech.challenge.establishment.manager.exceptions.ResourceNotFoundException;
import tech.challenge.establishment.manager.exceptions.UnauthorizedException;

@Service
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;

    public AddressServiceImpl(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Override
    public Address findById(Long id) {
        return addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Endereço não encontrado com ID: " + id));
    }

    @Override
    public Address findByUserId(Long userId) {
        return addressRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Endereço não encontrado para o usuário com ID: " + userId));
    }

    @Override
    public Address create(CreateAddressDTO dto, User user) {
        Address address = new Address();
        address.setStreet(dto.street());
        address.setCity(dto.city());
        address.setPostalCode(dto.postalCode());
        address.setNumber(dto.number());
        address.setUser(user);
        return addressRepository.save(address);
    }

    @Override
    public Address update(Long id, UpdateAddressDTO dto, User authenticatedUser) {
        Address address = findById(id);

        if (!address.getUser().getId().equals(authenticatedUser.getId())) {
            throw new UnauthorizedException("Você não tem permissão para alterar este endereço");
        }

        if (dto.street() != null) address.setStreet(dto.street());
        if (dto.city() != null) address.setCity(dto.city());
        if (dto.postalCode() != null) address.setPostalCode(dto.postalCode());
        if (dto.number() != null) address.setNumber(dto.number());

        return addressRepository.save(address);
    }

    @Override
    public void delete(Long id) {
        Address address = findById(id);
        addressRepository.delete(address);
    }
}
