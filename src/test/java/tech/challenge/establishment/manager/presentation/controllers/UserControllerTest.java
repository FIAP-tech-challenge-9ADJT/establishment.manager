package tech.challenge.establishment.manager.presentation.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import tech.challenge.establishment.manager.application.services.UserApplicationService;
import tech.challenge.establishment.manager.domain.entities.Address;
import tech.challenge.establishment.manager.domain.entities.Role;
import tech.challenge.establishment.manager.domain.entities.User;
import tech.challenge.establishment.manager.domain.repositories.AddressRepository;
import tech.challenge.establishment.manager.domain.valueobjects.*;
import tech.challenge.establishment.manager.infrastructure.persistence.entities.RoleJpaEntity;
import tech.challenge.establishment.manager.infrastructure.persistence.entities.UserJpaEntity;
import tech.challenge.establishment.manager.infrastructure.persistence.repositories.UserJpaRepository;
import tech.challenge.establishment.manager.infrastructure.security.TokenService;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserApplicationService userApplicationService;

    @MockBean
    @Qualifier("addressRepository")
    private AddressRepository addressRepository;
    
    @MockBean
    private UserJpaRepository userJpaRepository;
    
    @MockBean
    private TokenService tokenService;
    
    private UserJpaEntity buildMockUser() {
        RoleJpaEntity userRole = new RoleJpaEntity();
        userRole.setId(1L);
        userRole.setName(RoleJpaEntity.RoleName.USER);

        UserJpaEntity user = new UserJpaEntity();
        user.setId(1L);
        user.setRoles(Set.of(userRole));
        return user;
    }
    
    private SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor authenticatedUser() {
        return SecurityMockMvcRequestPostProcessors.user("user").roles("USER");
    }

    @Test
    void shouldGetProfile() throws Exception {
        User user = User.of(
                1L,
                "Test User",
                "test@example.com",
                "testuser",
                "password123",
                null,
                Set.of(Role.of(1L, Role.RoleName.USER)),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(userApplicationService.findUser(any(UserId.class))).thenReturn(user);

        mockMvc.perform(get("/users")
                        .with(user(buildMockUser()))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test User"))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    void shouldCreateUser() throws Exception {
        User user = User.of(
                1L,
                "New User",
                "new@example.com",
                "newuser",
                "password123",
                null,
                Set.of(Role.of(1L, Role.RoleName.USER)),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(userApplicationService.createCustomer(any(), any(), any(), any(), any())).thenReturn(user);

        String json = """
                {
                    "name": "New User",
                    "email": "new@example.com",
                    "login": "newuser",
                    "password": "password123",
                    "confirmPassword": "password123",
                    "address": {
                        "street": "Test Street",
                        "city": "Test City",
                        "postalCode": "12345678",
                        "number": "123"
                    }
                }
                """;

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("New User"));
    }

    @Test
    void shouldUpdateProfile() throws Exception {
        User user = User.of(
                1L,
                "Updated User",
                "updated@example.com",
                "testuser",
                "password123",
                null,
                Set.of(Role.of(1L, Role.RoleName.USER)),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(userApplicationService.updateUser(any(UserId.class), any(), any())).thenReturn(user);

        String json = """
                {
                    "name": "Updated User",
                    "email": "updated@example.com"
                }
                """;

        mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .with(user(buildMockUser()))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated User"));
    }

    @Test
    void shouldGetAddress() throws Exception {
        Address address = new Address(
                AddressId.of(1L),
                "Test Street",
                "Test City",
                new PostalCode("12345678"),
                "123",
                UserId.of(1L)
        );

        when(addressRepository.findByUserId(any(UserId.class))).thenReturn(Optional.of(address));

        mockMvc.perform(get("/users/address")
                        .with(user(buildMockUser()))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.street").value("Test Street"));
    }

    @Test
    void shouldReturnNotFoundWhenAddressNotExists() throws Exception {
        when(addressRepository.findByUserId(any(UserId.class))).thenReturn(Optional.empty());

        mockMvc.perform(get("/users/address")
                        .with(user(buildMockUser()))
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateAddress() throws Exception {
        Address address = new Address(
                AddressId.of(1L),
                "New Street",
                "New City",
                new PostalCode("12345678"),
                "456",
                UserId.of(1L)
        );

        when(addressRepository.save(any(Address.class))).thenReturn(address);

        String json = """
                {
                    "street": "New Street",
                    "city": "New City",
                    "postalCode": "12345678",
                    "number": "456"
                }
                """;

        mockMvc.perform(post("/users/address")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .with(user(buildMockUser()))
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.street").value("New Street"));
    }

    @Test
    void shouldUpdateAddress() throws Exception {
        Address existingAddress = new Address(
                AddressId.of(1L),
                "Old Street",
                "Old City",
                new PostalCode("12345678"),
                "123",
                UserId.of(1L)
        );

        Address updatedAddress = new Address(
                AddressId.of(1L),
                "Updated Street",
                "Updated City",
                new PostalCode("87654321"),
                "789",
                UserId.of(1L)
        );

        when(addressRepository.findByUserId(any(UserId.class))).thenReturn(Optional.of(existingAddress));
        when(addressRepository.save(any(Address.class))).thenReturn(updatedAddress);

        String json = """
                {
                    "street": "Updated Street",
                    "city": "Updated City",
                    "postalCode": "87654321",
                    "number": "789"
                }
                """;

        mockMvc.perform(put("/users/address")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .with(user(buildMockUser()))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.street").value("Updated Street"));
    }

    @Test
    void shouldReturnNotFoundWhenUpdatingNonExistentAddress() throws Exception {
        when(addressRepository.findByUserId(any(UserId.class))).thenReturn(Optional.empty());

        String json = """
                {
                    "street": "Updated Street",
                    "city": "Updated City",
                    "postalCode": "87654321",
                    "number": "789"
                }
                """;

        mockMvc.perform(put("/users/address")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .with(user(buildMockUser()))
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }
}

