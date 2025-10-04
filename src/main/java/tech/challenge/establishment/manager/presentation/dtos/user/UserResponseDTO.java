package tech.challenge.establishment.manager.presentation.dtos.user;

import tech.challenge.establishment.manager.presentation.dtos.address.AddressResponseDTO;
import tech.challenge.establishment.manager.presentation.dtos.role.RoleResponseDTO;

import java.time.LocalDateTime;
import java.util.List;

public record UserResponseDTO(
        Long id,
        String name,
        String email,
        String login,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        AddressResponseDTO address,
        List<RoleResponseDTO> roles
) {
}