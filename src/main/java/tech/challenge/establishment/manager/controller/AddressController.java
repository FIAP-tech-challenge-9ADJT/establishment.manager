package tech.challenge.establishment.manager.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.challenge.establishment.manager.dtos.address.CreateAddressDTO;
import tech.challenge.establishment.manager.dtos.address.UpdateAddressDTO;
import tech.challenge.establishment.manager.entities.Address;
import tech.challenge.establishment.manager.services.AddressService;

@RestController
@RequestMapping("/address")
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Address> findById(@PathVariable Long id) {
        return ResponseEntity.ok(addressService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Address> createAddress(@RequestBody CreateAddressDTO address) {
        return ResponseEntity.ok(addressService.save(address));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Address> updateAddress(@PathVariable Long id, @RequestBody UpdateAddressDTO address) {
        return ResponseEntity.ok(addressService.update(id, address));
    }

    @DeleteMapping("/{id}")
    public void deleteAddress(@PathVariable Long id) {
        addressService.delete(id);
    }
}
