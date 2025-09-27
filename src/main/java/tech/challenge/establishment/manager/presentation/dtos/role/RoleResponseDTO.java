package tech.challenge.establishment.manager.presentation.dtos.role;

import tech.challenge.establishment.manager.infrastructure.persistence.entities.RoleName;

public record RoleResponseDTO(
        Long id,
        RoleName name
) {
}