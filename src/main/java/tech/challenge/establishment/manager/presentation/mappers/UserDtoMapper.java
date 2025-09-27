package tech.challenge.establishment.manager.presentation.mappers;

import tech.challenge.establishment.manager.domain.entities.User;
import tech.challenge.establishment.manager.domain.entities.Address;
import tech.challenge.establishment.manager.presentation.dtos.user.CreateUserDTO;
import tech.challenge.establishment.manager.presentation.dtos.user.UserResponseDTO;

public class UserDtoMapper {
    
    public static User fromCreateDto(CreateUserDTO dto) {
        Address address = null;
        if (dto.address() != null) {
            address = AddressDtoMapper.fromCreateDto(dto.address());
        }
        
        return User.create(
            dto.name(),
            dto.email(),
            dto.login(),
            dto.password(),
            address
        );
    }
    

    
    public static UserResponseDTO toResponseDto(User user) {
        return new UserResponseDTO(
            user.getId() != null ? user.getId().value() : null,
            user.getName().value(),
            user.getEmail().value(),
            user.getLogin().value(),
            user.getCreatedAt(),
            user.getUpdatedAt(),
            user.getAddress() != null ? AddressDtoMapper.toResponseDto(user.getAddress()) : null,
            user.getRoles().stream()
                .map(RoleDtoMapper::toResponseDto)
                .toList()
        );
    }
}