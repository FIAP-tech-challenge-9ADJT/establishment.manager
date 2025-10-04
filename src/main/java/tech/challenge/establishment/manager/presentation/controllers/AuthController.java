package tech.challenge.establishment.manager.presentation.controllers;

import tech.challenge.establishment.manager.presentation.dtos.auth.LoginResponseDTO;
import tech.challenge.establishment.manager.presentation.dtos.user.ChangePasswordDTO;
import tech.challenge.establishment.manager.presentation.dtos.user.LoginUserDTO;
import tech.challenge.establishment.manager.infrastructure.persistence.entities.UserJpaEntity;
import tech.challenge.establishment.manager.application.services.AuthApplicationService;
import tech.challenge.establishment.manager.infrastructure.security.TokenService;
import tech.challenge.establishment.manager.domain.valueobjects.UserId;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthApplicationService authApplicationService;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public AuthController(AuthApplicationService authApplicationService, 
                         AuthenticationManager authenticationManager,
                         TokenService tokenService) {
        this.authApplicationService = authApplicationService;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginUserDTO dto) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(dto.login(), dto.password());
        var authentication = authenticationManager.authenticate(authenticationToken);
        String accessToken = tokenService.generateToken((UserJpaEntity) authentication.getPrincipal());
        return ResponseEntity.ok(LoginResponseDTO.of(accessToken));
    }

    @PostMapping("/change-password")
    public ResponseEntity<Void> changePassword(@Valid @RequestBody ChangePasswordDTO dto,
            @AuthenticationPrincipal UserJpaEntity authenticatedUser) {
        authApplicationService.changePassword(UserId.of(authenticatedUser.getId()), dto.newPassword());
        return ResponseEntity.noContent().build();
    }
}