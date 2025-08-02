package tech.challenge.establishment.manager.dtos.user;

import tech.challenge.establishment.manager.dtos.address.AddressResponseDTO;
import tech.challenge.establishment.manager.dtos.role.RoleResponseDTO;
import tech.challenge.establishment.manager.entities.User;

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
    public static UserResponseDTO from(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getLogin(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getAddress() != null ? AddressResponseDTO.from(user.getAddress()) : null,
                user.getRoles().stream().map(RoleResponseDTO::from).toList()
        );
    }
}