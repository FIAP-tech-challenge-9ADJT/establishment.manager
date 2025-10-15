package tech.challenge.establishment.manager.presentation.mappers;

import org.junit.jupiter.api.Test;
import tech.challenge.establishment.manager.domain.entities.Address;
import tech.challenge.establishment.manager.domain.entities.Role;
import tech.challenge.establishment.manager.domain.entities.User;
import tech.challenge.establishment.manager.domain.valueobjects.*;
import tech.challenge.establishment.manager.presentation.dtos.address.CreateAddressDTO;
import tech.challenge.establishment.manager.presentation.dtos.user.CreateUserDTO;
import tech.challenge.establishment.manager.presentation.dtos.user.UserResponseDTO;

import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class UserDtoMapperTest {

    @Test
    void shouldMapFromCreateDtoWithAddress() {
        // Given
        CreateAddressDTO addressDto = new CreateAddressDTO(
                "User Street",
                "User City",
                "12345678",
                "100"
        );
        CreateUserDTO dto = new CreateUserDTO(
                "Test User",
                "test@example.com",
                "testuser",
                "password123",
                addressDto
        );

        // When
        User user = UserDtoMapper.fromCreateDto(dto);

        // Then
        assertThat(user).isNotNull();
        assertThat(user.getName().value()).isEqualTo("Test User");
        assertThat(user.getEmail().value()).isEqualTo("test@example.com");
        assertThat(user.getLogin().value()).isEqualTo("testuser");
        assertThat(user.getPassword()).isNotNull();
        assertThat(user.getAddress()).isNotNull();
        assertThat(user.getAddress().getStreet()).isEqualTo("User Street");
        assertThat(user.getAddress().getCity()).isEqualTo("User City");
        assertThat(user.getAddress().getPostalCode().value()).isEqualTo("12345678");
        assertThat(user.getAddress().getNumber()).isEqualTo("100");
    }

    @Test
    void shouldMapFromCreateDtoWithoutAddress() {
        // Given
        CreateUserDTO dto = new CreateUserDTO(
                "User No Address",
                "noaddress@example.com",
                "noaddressuser",
                "password456",
                null // no address
        );

        // When
        User user = UserDtoMapper.fromCreateDto(dto);

        // Then
        assertThat(user).isNotNull();
        assertThat(user.getName().value()).isEqualTo("User No Address");
        assertThat(user.getEmail().value()).isEqualTo("noaddress@example.com");
        assertThat(user.getLogin().value()).isEqualTo("noaddressuser");
        assertThat(user.getPassword()).isNotNull();
        assertThat(user.getAddress()).isNull();
    }

    @Test
    void shouldMapToResponseDtoWithAddress() {
        // Given
        Address address = new Address(
                AddressId.of(1L),
                "Response Street",
                "Response City",
                new PostalCode("87654321"),
                "200",
                UserId.of(1L)
        );

        User user = User.of(
                1L,
                "Response User",
                "response@example.com",
                "responseuser",
                "hashedpassword",
                address,
                Set.of(Role.of(1L, Role.RoleName.USER)),
                LocalDateTime.of(2023, 1, 1, 10, 0),
                LocalDateTime.of(2023, 1, 2, 11, 0)
        );

        // When
        UserResponseDTO dto = UserDtoMapper.toResponseDto(user);

        // Then
        assertThat(dto).isNotNull();
        assertThat(dto.id()).isEqualTo(1L);
        assertThat(dto.name()).isEqualTo("Response User");
        assertThat(dto.email()).isEqualTo("response@example.com");
        assertThat(dto.login()).isEqualTo("responseuser");
        assertThat(dto.createdAt()).isEqualTo(LocalDateTime.of(2023, 1, 1, 10, 0));
        assertThat(dto.updatedAt()).isEqualTo(LocalDateTime.of(2023, 1, 2, 11, 0));
        assertThat(dto.address()).isNotNull();
        assertThat(dto.address().street()).isEqualTo("Response Street");
        assertThat(dto.roles()).hasSize(1);
        assertThat(dto.roles().get(0).name().toString()).isEqualTo("USER");
    }

    @Test
    void shouldMapToResponseDtoWithoutAddress() {
        // Given
        User user = User.of(
                2L,
                "User Without Address",
                "withoutaddress@example.com",
                "withoutaddressuser",
                "hashedpassword",
                null, // no address
                Set.of(Role.of(2L, Role.RoleName.RESTAURANT_OWNER)),
                LocalDateTime.of(2023, 2, 1, 12, 0),
                LocalDateTime.of(2023, 2, 2, 13, 0)
        );

        // When
        UserResponseDTO dto = UserDtoMapper.toResponseDto(user);

        // Then
        assertThat(dto).isNotNull();
        assertThat(dto.id()).isEqualTo(2L);
        assertThat(dto.name()).isEqualTo("User Without Address");
        assertThat(dto.email()).isEqualTo("withoutaddress@example.com");
        assertThat(dto.login()).isEqualTo("withoutaddressuser");
        assertThat(dto.createdAt()).isEqualTo(LocalDateTime.of(2023, 2, 1, 12, 0));
        assertThat(dto.updatedAt()).isEqualTo(LocalDateTime.of(2023, 2, 2, 13, 0));
        assertThat(dto.address()).isNull();
        assertThat(dto.roles()).hasSize(1);
        assertThat(dto.roles().get(0).name().toString()).isEqualTo("RESTAURANT_OWNER");
    }

    @Test
    void shouldMapToResponseDtoWithNullId() {
        // Given
        User user = User.create(
                "New User",
                "newuser@example.com",
                "newuser",
                "password789",
                null
        );

        // When
        UserResponseDTO dto = UserDtoMapper.toResponseDto(user);

        // Then
        assertThat(dto).isNotNull();
        assertThat(dto.id()).isNull(); // New user has no ID yet
        assertThat(dto.name()).isEqualTo("New User");
        assertThat(dto.email()).isEqualTo("newuser@example.com");
        assertThat(dto.login()).isEqualTo("newuser");
        assertThat(dto.address()).isNull();
        assertThat(dto.roles()).isEmpty(); // New user has no roles assigned yet
    }

    @Test
    void shouldMapToResponseDtoWithMultipleRoles() {
        // Given
        User user = User.of(
                3L,
                "Multi Role User",
                "multirole@example.com",
                "multiroleuser",
                "hashedpassword",
                null,
                Set.of(
                        Role.of(1L, Role.RoleName.USER),
                        Role.of(2L, Role.RoleName.ADMIN)
                ),
                LocalDateTime.of(2023, 3, 1, 14, 0),
                LocalDateTime.of(2023, 3, 2, 15, 0)
        );

        // When
        UserResponseDTO dto = UserDtoMapper.toResponseDto(user);

        // Then
        assertThat(dto).isNotNull();
        assertThat(dto.id()).isEqualTo(3L);
        assertThat(dto.name()).isEqualTo("Multi Role User");
        assertThat(dto.roles()).hasSize(2);
        assertThat(dto.roles().stream().map(role -> role.name().toString()))
                .containsExactlyInAnyOrder("USER", "ADMIN");
    }

    @Test
    void shouldMapFromCreateDtoWithSpecialCharacters() {
        // Given
        CreateAddressDTO addressDto = new CreateAddressDTO(
                "Rua José da Silva",
                "São Paulo",
                "01234567",
                "123A"
        );
        CreateUserDTO dto = new CreateUserDTO(
                "José da Silva Júnior",
                "jose.silva@exemplo.com.br",
                "jose.silva",
                "senhaSegura123!",
                addressDto
        );

        // When
        User user = UserDtoMapper.fromCreateDto(dto);

        // Then
        assertThat(user).isNotNull();
        assertThat(user.getName().value()).isEqualTo("José da Silva Júnior");
        assertThat(user.getEmail().value()).isEqualTo("jose.silva@exemplo.com.br");
        assertThat(user.getLogin().value()).isEqualTo("jose.silva");
        assertThat(user.getAddress()).isNotNull();
        assertThat(user.getAddress().getStreet()).isEqualTo("Rua José da Silva");
        assertThat(user.getAddress().getCity()).isEqualTo("São Paulo");
        assertThat(user.getAddress().getNumber()).isEqualTo("123A");
    }
}
