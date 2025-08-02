package tech.challenge.establishment.manager.dtos.role;

import tech.challenge.establishment.manager.entities.Role;
import tech.challenge.establishment.manager.entities.RoleName;

public record RoleResponseDTO(
        Long id,
        RoleName name
) {
    public static RoleResponseDTO from(Role role) {
        return new RoleResponseDTO(
                role.getId(),
                role.getName()
        );
    }
}