package tech.challenge.establishment.manager.presentation.controllers;

import tech.challenge.establishment.manager.presentation.dtos.user.CreateUserDTO;
import tech.challenge.establishment.manager.presentation.dtos.user.UpdateUserDTO;
import tech.challenge.establishment.manager.presentation.dtos.user.UserResponseDTO;
import tech.challenge.establishment.manager.presentation.mappers.UserDtoMapper;
import tech.challenge.establishment.manager.presentation.mappers.AddressDtoMapper;
import tech.challenge.establishment.manager.application.services.UserApplicationService;
import tech.challenge.establishment.manager.domain.usecases.admin.DeleteUserUseCase;
import tech.challenge.establishment.manager.domain.valueobjects.UserId;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final UserApplicationService userApplicationService;
    private final DeleteUserUseCase deleteUserUseCase;

    public AdminController(UserApplicationService userApplicationService,
                          DeleteUserUseCase deleteUserUseCase) {
        this.userApplicationService = userApplicationService;
        this.deleteUserUseCase = deleteUserUseCase;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> findById(@PathVariable Long id) {
        var user = userApplicationService.findUser(UserId.of(id));
        return ResponseEntity.ok(UserDtoMapper.toResponseDto(user));
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> save(@RequestBody @Valid CreateUserDTO dto) {
        var address = AddressDtoMapper.fromCreateDto(dto.address());
        var user = userApplicationService.createUser(dto.name(), dto.email(), dto.login(), dto.password(), address);
        return ResponseEntity.status(HttpStatus.CREATED).body(UserDtoMapper.toResponseDto(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> update(@PathVariable Long id, @RequestBody @Valid UpdateUserDTO dto) {
        var user = userApplicationService.updateUser(UserId.of(id), dto.name(), dto.email());
        return ResponseEntity.ok(UserDtoMapper.toResponseDto(user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        deleteUserUseCase.execute(UserId.of(id));
        return ResponseEntity.noContent().build();
    }
}