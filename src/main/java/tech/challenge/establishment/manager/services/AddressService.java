package tech.challenge.establishment.manager.services;

import org.springframework.stereotype.Service;
import tech.challenge.establishment.manager.repositories.AddressRepository;

@Service
public class AddressService {

    private AddressRepository addressRepository;

    public AddressService(AddressRepository addressRepository){
        this.addressRepository = addressRepository;
    }
}
