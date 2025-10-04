package tech.challenge.establishment.manager.presentation.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import tech.challenge.establishment.manager.presentation.dtos.user.CreateUserDTO;
import tech.challenge.establishment.manager.presentation.dtos.user.UpdateUserDTO;
import tech.challenge.establishment.manager.presentation.dtos.user.UserResponseDTO;
import tech.challenge.establishment.manager.presentation.dtos.address.CreateAddressDTO;
import tech.challenge.establishment.manager.presentation.dtos.address.UpdateAddressDTO;
import tech.challenge.establishment.manager.presentation.dtos.address.AddressResponseDTO;
import tech.challenge.establishment.manager.presentation.mappers.UserDtoMapper;
import tech.challenge.establishment.manager.presentation.mappers.AddressDtoMapper;
import tech.challenge.establishment.manager.infrastructure.persistence.entities.UserJpaEntity;
import tech.challenge.establishment.manager.application.services.UserApplicationService;
import tech.challenge.establishment.manager.domain.repositories.AddressRepository;
import tech.challenge.establishment.manager.domain.valueobjects.UserId;
import tech.challenge.establishment.manager.domain.valueobjects.PostalCode;
import tech.challenge.establishment.manager.domain.entities.Address;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserApplicationService userApplicationService;
    private final AddressRepository addressRepository;

    public UserController(UserApplicationService userApplicationService,
                          AddressRepository addressRepository) {
        this.userApplicationService = userApplicationService;
        this.addressRepository = addressRepository;
    }

    @GetMapping
    public ResponseEntity<UserResponseDTO> getProfile(@AuthenticationPrincipal UserJpaEntity authenticatedUser) {
        var user = userApplicationService.findUser(UserId.of(authenticatedUser.getId()));
        return ResponseEntity.ok(UserDtoMapper.toResponseDto(user));
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody CreateUserDTO dto) {
        Address address = null;
        if (dto.address() != null) {
            address = AddressDtoMapper.fromCreateDto(dto.address());
        }
        var user = userApplicationService.createCustomer(
                dto.name(),
                dto.email(),
                dto.login(),
                dto.password(),
                address
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(UserDtoMapper.toResponseDto(user));
    }

    @PutMapping
    public ResponseEntity<UserResponseDTO> updateProfile(@Valid @RequestBody UpdateUserDTO dto,
                                                         @AuthenticationPrincipal UserJpaEntity authenticatedUser) {
        var user = userApplicationService.updateUser(
                UserId.of(authenticatedUser.getId()),
                dto.name(),
                dto.email()
        );
        return ResponseEntity.ok(UserDtoMapper.toResponseDto(user));
    }

    @GetMapping("/address")
    public ResponseEntity<AddressResponseDTO> getAddress(@AuthenticationPrincipal UserJpaEntity authenticatedUser) {
        var address = addressRepository.findByUserId(UserId.of(authenticatedUser.getId()));
        return address.map(addr -> ResponseEntity.ok(AddressDtoMapper.toResponseDto(addr)))
                      .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/address")
    public ResponseEntity<AddressResponseDTO> createAddress(@Valid @RequestBody CreateAddressDTO dto,
                                                            @AuthenticationPrincipal UserJpaEntity authenticatedUser) {
        var address = AddressDtoMapper.fromCreateDto(dto, UserId.of(authenticatedUser.getId()));
        var savedAddress = addressRepository.save(address);
        return ResponseEntity.status(HttpStatus.CREATED).body(AddressDtoMapper.toResponseDto(savedAddress));
    }

    @PutMapping("/address")
    public ResponseEntity<AddressResponseDTO> updateAddress(@AuthenticationPrincipal UserJpaEntity authenticatedUser,
                                                            @RequestBody @Valid UpdateAddressDTO dto) {
        var existingAddress = addressRepository.findByUserId(UserId.of(authenticatedUser.getId()));
        if (existingAddress.isPresent()) {
            Address oldAddress = existingAddress.get();
            Address updatedAddress = new Address(
                oldAddress.getId(),
                dto.street(),
                dto.city(),
                new PostalCode(dto.postalCode()),
                dto.number(),
                oldAddress.getUserId()
            );
            var savedAddress = addressRepository.save(updatedAddress);
            return ResponseEntity.ok(AddressDtoMapper.toResponseDto(savedAddress));
        }
        return ResponseEntity.notFound().build();
    }
}