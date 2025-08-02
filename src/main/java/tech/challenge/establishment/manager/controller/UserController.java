package tech.challenge.establishment.manager.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import tech.challenge.establishment.manager.dtos.user.ChangePasswordDTO;
import tech.challenge.establishment.manager.dtos.user.CreateUserDTO;
import tech.challenge.establishment.manager.dtos.user.UpdateUserDTO;
import tech.challenge.establishment.manager.dtos.user.LoginUserDTO;
import tech.challenge.establishment.manager.dtos.user.UserResponseDTO;

import tech.challenge.establishment.manager.dtos.address.UpdateAddressDTO;
import tech.challenge.establishment.manager.entities.Address;
import tech.challenge.establishment.manager.entities.User;
import tech.challenge.establishment.manager.services.UserService;
import tech.challenge.establishment.manager.services.AddressService;
import tech.challenge.establishment.manager.services.TokenService;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final AddressService addressService;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public UserController(UserService userService,
                          AddressService addressService,
                          AuthenticationManager authenticationManager,
                          TokenService tokenService) {
        this.userService = userService;
        this.addressService = addressService;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

<<<<<<< HEAD
    // USER

=======
>>>>>>> origin/main
    @GetMapping
    public ResponseEntity<UserResponseDTO> findById(@AuthenticationPrincipal User authenticatedUser) {
        return ResponseEntity.ok(UserResponseDTO.from(userService.findById(authenticatedUser.getId())));
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> save(@RequestBody @Valid CreateUserDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(UserResponseDTO.from(userService.save(dto)));
    }

    @PutMapping
<<<<<<< HEAD
    public ResponseEntity<UserResponseDTO> update(@AuthenticationPrincipal User authenticatedUser,
                                                  @RequestBody @Valid UpdateUserDTO dto) {
        return ResponseEntity.ok(UserResponseDTO.from(userService.update(authenticatedUser.getId(), dto)));
    }

    // ADDRESS

    @GetMapping("/address")
    public ResponseEntity<Address> getAddress(@AuthenticationPrincipal User authenticatedUser) {
        return ResponseEntity.ok(addressService.findByUserId(authenticatedUser.getId()));
    }

    @PutMapping("/address")
    public ResponseEntity<Address> updateAddress(@AuthenticationPrincipal User authenticatedUser,
                                                 @RequestBody @Valid UpdateAddressDTO dto) {
        Address address = addressService.findByUserId(authenticatedUser.getId());
        return ResponseEntity.ok(addressService.update(address.getId(), dto, authenticatedUser));
    }
}
=======
    public ResponseEntity<UserResponseDTO> update(@AuthenticationPrincipal User authenticatedUser, @RequestBody @Valid UpdateUserDTO dto) {
        return ResponseEntity.ok(UserResponseDTO.from(userService.update(authenticatedUser.getId(), dto)));
    }
}  
>>>>>>> origin/main
