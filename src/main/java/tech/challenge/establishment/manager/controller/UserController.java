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

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(UserResponseDTO.from(userService.findById(id)));
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> save(@RequestBody @Valid CreateUserDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(UserResponseDTO.from(userService.save(dto)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> update(@PathVariable Long id,
                                                  @RequestBody @Valid UpdateUserDTO dto,
                                                  Principal principal) {

        User authenticatedUser = (User) userService.loadUserByUsername(principal.getName());

        if (!authenticatedUser.getId().equals(id)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não tem permissão para atualizar este usuário.");
        }

        return ResponseEntity.ok(UserResponseDTO.from(userService.update(id, dto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/login")
    public ResponseEntity<String> login(@RequestBody LoginUserDTO dto) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(dto.login(), dto.password());
        var authentication = authenticationManager.authenticate(authenticationToken);
        String accessToken = tokenService.generateToken((User) authentication.getPrincipal());
        return ResponseEntity.ok(accessToken);
    }

    @PostMapping("/change-password")
    public ResponseEntity<Void> changePassword(@RequestBody @Valid ChangePasswordDTO dto,
                                               @AuthenticationPrincipal User authenticatedUser) {
        userService.changePassword(authenticatedUser.getId(), dto);
        return ResponseEntity.noContent().build();
    }
}  
