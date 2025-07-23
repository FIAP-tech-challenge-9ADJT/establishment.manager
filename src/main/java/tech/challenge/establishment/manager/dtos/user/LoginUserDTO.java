package tech.challenge.establishment.manager.dtos.user;

import jakarta.validation.constraints.NotBlank;

public record LoginUserDTO(
    @NotBlank(message = "Login is required") String login,
    @NotBlank(message = "Password is required") String password
) {}