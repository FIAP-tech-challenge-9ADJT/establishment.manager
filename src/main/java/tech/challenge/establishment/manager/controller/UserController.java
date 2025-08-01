package tech.challenge.establishment.manager.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import jakarta.validation.Valid;
import java.security.Principal;
import tech.challenge.establishment.manager.dtos.user.ChangePasswordDTO;
import tech.challenge.establishment.manager.dtos.user.CreateUserDTO;
import tech.challenge.establishment.manager.dtos.user.UpdateUserDTO;
import tech.challenge.establishment.manager.dtos.user.LoginUserDTO;
import tech.challenge.establishment.manager.dtos.user.UserResponseDTO;
import tech.challenge.establishment.manager.entities.User;
import tech.challenge.establishment.manager.services.UserService;
import tech.challenge.establishment.manager.services.TokenService;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public UserController(UserService userService, AuthenticationManager authenticationManager, TokenService tokenService) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @GetMapping
    public ResponseEntity<UserResponseDTO> findById(@AuthenticationPrincipal User authenticatedUser) {
        return ResponseEntity.ok(UserResponseDTO.from(userService.findById(authenticatedUser.getId())));
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> save(@RequestBody @Valid CreateUserDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(UserResponseDTO.from(userService.save(dto)));
    }

    @PutMapping
    public ResponseEntity<UserResponseDTO> update(@AuthenticationPrincipal User authenticatedUser, @RequestBody @Valid UpdateUserDTO dto) {
        return ResponseEntity.ok(UserResponseDTO.from(userService.update(authenticatedUser.getId(), dto)));
    }
}  
