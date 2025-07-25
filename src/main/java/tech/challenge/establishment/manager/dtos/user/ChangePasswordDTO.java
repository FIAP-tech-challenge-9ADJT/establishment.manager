package tech.challenge.establishment.manager.dtos.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ChangePasswordDTO(
        @NotBlank(message = "A senha atual é obrigatória")
        String currentPassword,

        @NotBlank(message = "A nova senha é obrigatória")
        @Size(min = 6, message = "A nova senha deve ter pelo menos 6 caracteres")
        String newPassword,

        @NotBlank(message = "A confirmação da nova senha é obrigatória")
        String newPasswordConfirmation
) {}
