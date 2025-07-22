package tech.challenge.establishment.manager.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.challenge.establishment.manager.dtos.address.CreateAddressDTO;
import tech.challenge.establishment.manager.dtos.address.UpdateAddressDTO;
import tech.challenge.establishment.manager.entities.Address;
import tech.challenge.establishment.manager.services.AddressService;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import tech.challenge.establishment.manager.entities.User;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/addresses")
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Address> getById(@PathVariable Long id) {
        return ResponseEntity.ok(addressService.findById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Address> getAddressByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(addressService.findByUserId(userId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Address> update(@PathVariable Long id,
                                          @RequestBody @Valid UpdateAddressDTO dto,
                                          @AuthenticationPrincipal User authenticatedUser) {
        return ResponseEntity.ok(addressService.update(id, dto, authenticatedUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        addressService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
