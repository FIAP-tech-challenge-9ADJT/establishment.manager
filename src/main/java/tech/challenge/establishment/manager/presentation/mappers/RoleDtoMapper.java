package tech.challenge.establishment.manager.presentation.mappers;

import tech.challenge.establishment.manager.domain.entities.Role;
import tech.challenge.establishment.manager.presentation.dtos.role.RoleResponseDTO;

public class RoleDtoMapper {
    
    public static RoleResponseDTO toResponseDto(Role role) {
        return new RoleResponseDTO(
            role.getId(),
            tech.challenge.establishment.manager.infrastructure.persistence.entities.RoleName.valueOf(role.getName().name())
        );
    }
}