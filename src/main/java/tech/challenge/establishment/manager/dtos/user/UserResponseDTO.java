package tech.challenge.establishment.manager.dtos.user;

import tech.challenge.establishment.manager.entities.Address;
import tech.challenge.establishment.manager.entities.Role;
import tech.challenge.establishment.manager.entities.User;

import java.time.LocalDateTime;
import java.util.List;

public record UserResponseDTO(
        Long id,
        String name,
        String email,
        String login,
        String password,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Address address,
        List<Role> roles
) {
    public static UserResponseDTO from(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getLogin(),
                user.getPassword(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getAddress(),
                user.getRoles()
        );
    }
}