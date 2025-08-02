package tech.challenge.establishment.manager.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import tech.challenge.establishment.manager.dtos.user.CreateUserDTO;
import tech.challenge.establishment.manager.dtos.user.UpdateUserDTO;
import tech.challenge.establishment.manager.dtos.user.UserResponseDTO;

import tech.challenge.establishment.manager.dtos.address.AddressResponseDTO;
import tech.challenge.establishment.manager.dtos.address.UpdateAddressDTO;
import tech.challenge.establishment.manager.entities.Address;
import tech.challenge.establishment.manager.entities.User;
import tech.challenge.establishment.manager.services.UserService;
import tech.challenge.establishment.manager.services.AddressService;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final AddressService addressService;

    public UserController(UserService userService,
            AddressService addressService) {
        this.userService = userService;
        this.addressService = addressService;
        ;
    }

    // USER

    @GetMapping
    public ResponseEntity<UserResponseDTO> findById(@AuthenticationPrincipal User authenticatedUser) {
        return ResponseEntity.ok(UserResponseDTO.from(userService.findById(authenticatedUser.getId())));
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> save(@RequestBody @Valid CreateUserDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(UserResponseDTO.from(userService.save(dto)));
    }

    @PutMapping
    public ResponseEntity<UserResponseDTO> update(@AuthenticationPrincipal User authenticatedUser,
            @RequestBody @Valid UpdateUserDTO dto) {
        return ResponseEntity.ok(UserResponseDTO.from(userService.update(authenticatedUser.getId(), dto)));
    }

    // ADDRESS

    @GetMapping("/address")
    public ResponseEntity<AddressResponseDTO> getAddress(@AuthenticationPrincipal User authenticatedUser) {
        Address address = addressService.findByUserId(authenticatedUser.getId());
        return ResponseEntity.ok(AddressResponseDTO.from(address));
    }

    @PutMapping("/address")
    public ResponseEntity<AddressResponseDTO> updateAddress(@AuthenticationPrincipal User authenticatedUser,
            @RequestBody @Valid UpdateAddressDTO dto) {
        Address address = addressService.findByUserId(authenticatedUser.getId());
        Address updatedAddress = addressService.update(address.getId(), dto, authenticatedUser);
        return ResponseEntity.ok(AddressResponseDTO.from(updatedAddress));
    }
}
