package tech.challenge.establishment.manager.presentation.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import tech.challenge.establishment.manager.presentation.dtos.user.CreateUserDTO;
import tech.challenge.establishment.manager.presentation.dtos.user.UpdateUserDTO;
import tech.challenge.establishment.manager.presentation.dtos.user.UserResponseDTO;
import tech.challenge.establishment.manager.presentation.mappers.UserDtoMapper;
import tech.challenge.establishment.manager.application.services.UserApplicationService;
import tech.challenge.establishment.manager.domain.usecases.admin.DeleteUserUseCase;
import tech.challenge.establishment.manager.domain.valueobjects.UserId;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final UserApplicationService userService;
    private final DeleteUserUseCase deleteUserUseCase;

    public AdminController(UserApplicationService userService, DeleteUserUseCase deleteUserUseCase) {
        this.userService = userService;
        this.deleteUserUseCase = deleteUserUseCase;
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> createAdmin(@Valid @RequestBody CreateUserDTO dto) {
    var address = tech.challenge.establishment.manager.presentation.mappers.AddressDtoMapper.fromCreateDto(dto.address());
    var user = userService.createAdmin(dto.name(), dto.email(), dto.login(), dto.password(), address);
    return ResponseEntity.status(HttpStatus.CREATED).body(UserDtoMapper.toResponseDto(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(UserDtoMapper.toResponseDto(userService.findUser(UserId.of(id))));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> update(@PathVariable Long id, @Valid @RequestBody UpdateUserDTO dto) {
        return ResponseEntity.ok(UserDtoMapper.toResponseDto(userService.updateUser(UserId.of(id), dto.name(), dto.email())));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        deleteUserUseCase.execute(UserId.of(id));
        return ResponseEntity.noContent().build();
    }
}