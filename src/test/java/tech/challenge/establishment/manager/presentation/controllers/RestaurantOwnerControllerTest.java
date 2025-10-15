package tech.challenge.establishment.manager.presentation.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

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
import tech.challenge.establishment.manager.domain.valueobjects.AddressId;
import tech.challenge.establishment.manager.domain.valueobjects.PostalCode;
import tech.challenge.establishment.manager.domain.valueobjects.UserId;
import tech.challenge.establishment.manager.infrastructure.persistence.entities.RoleJpaEntity;
import tech.challenge.establishment.manager.infrastructure.persistence.entities.UserJpaEntity;
import tech.challenge.establishment.manager.infrastructure.persistence.repositories.UserJpaRepository;
import tech.challenge.establishment.manager.infrastructure.security.TokenService;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class RestaurantOwnerControllerTest {

    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private UserApplicationService userApplicationService;
    
    @MockBean
    private UserJpaRepository userJpaRepository;
    
    @MockBean
    @Qualifier("addressRepository")
    private AddressRepository addressRepository;
    
    @MockBean
    private TokenService tokenService;
    
    private UserJpaEntity buildMockRestaurantOwner() {
        RoleJpaEntity restaurantOwnerRole = new RoleJpaEntity();
        restaurantOwnerRole.setId(2L);
        restaurantOwnerRole.setName(RoleJpaEntity.RoleName.RESTAURANT_OWNER);

        UserJpaEntity user = new UserJpaEntity();
        user.setId(1L);
        user.setRoles(Set.of(restaurantOwnerRole));
        return user;
    }
    
    private SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor ownerUser() {
        return SecurityMockMvcRequestPostProcessors.user("owner").roles("RESTAURANT_OWNER");
    }
    
    @Test
    void shouldCreateRestaurantOwner() throws Exception {
        User user = User.of(
                1L,
                "Restaurant Owner",
                "owner@example.com",
                "owneruser",
                "password123",
                null,
                Set.of(Role.of(1L, Role.RoleName.RESTAURANT_OWNER)),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(userApplicationService.createRestaurantOwner(any(), any(), any(), any(), any())).thenReturn(user);

        String json = """
                {
                    "name": "Restaurant Owner",
                    "email": "owner@example.com",
                    "login": "owneruser",
                    "password": "password123",
                    "confirmPassword": "password123",
                    "address": {
                        "street": "Rua Exemplo",
                        "city": "São Paulo",
                        "postalCode": "01000-000",
                        "number": "15"
                    }
                }
                """;

        mockMvc.perform(post("/restaurant-owners")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Restaurant Owner"));
    }

    @Test
    void shouldGetProfile() throws Exception {
        User user = User.of(
                1L,
                "Restaurant Owner",
                "owner@example.com",
                "owneruser",
                "password123",
                null,
                Set.of(Role.of(1L, Role.RoleName.RESTAURANT_OWNER)),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(userApplicationService.findUser(any(UserId.class))).thenReturn(user);

        mockMvc.perform(get("/restaurant-owners")
                        .with(user(buildMockRestaurantOwner()))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Restaurant Owner"));
    }

    @Test
    void shouldUpdateProfile() throws Exception {
        User user = User.of(
                1L,
                "Updated Owner",
                "updated@example.com",
                "owneruser",
                "password123",
                null,
                Set.of(Role.of(1L, Role.RoleName.RESTAURANT_OWNER)),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(userApplicationService.updateUser(any(UserId.class), any(), any())).thenReturn(user);

        String json = """
                {
                    "name": "Updated Owner",
                    "email": "updated@example.com"
                }
                """;

        mockMvc.perform(put("/restaurant-owners")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .with(user(buildMockRestaurantOwner()))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Owner"));
    }

    @Test
    void shouldGetAddress() throws Exception {
        Address address = new Address(
            AddressId.of(1L),
            "Owner Street",
            "Owner City",
            new PostalCode("12345678"),
            "100",
            UserId.of(1L)
        );
        when(addressRepository.findByUserId(any(UserId.class))).thenReturn(Optional.of(address));
        mockMvc.perform(get("/restaurant-owners/address")
                        .with(user(buildMockRestaurantOwner()))
                        .with(csrf()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.street").value("Owner Street"));
    }

    @Test
    void shouldCreateAddress() throws Exception {
        Address address = new Address(
                AddressId.of(1L),
                "New Street",
                "New City",
                new PostalCode("12345678"),
                "200",
                UserId.of(1L)
        );

        when(addressRepository.findByUserId(any(UserId.class))).thenReturn(Optional.empty());
        when(addressRepository.save(any(Address.class))).thenReturn(address);

        String json = """
                {
                    "street": "New Street",
                    "city": "New City",
                    "postalCode": "12345678",
                    "number": "200"
                }
                """;

        mockMvc.perform(post("/restaurant-owners/address")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .with(user(buildMockRestaurantOwner()))
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.street").value("New Street"));
    }

    @Test
    void shouldReturnConflictWhenCreatingExistingAddress() throws Exception {
        Address existingAddress = new Address(
                AddressId.of(1L),
                "Existing Street",
                "Existing City",
                new PostalCode("12345678"),
                "100",
                UserId.of(1L)
        );

        when(addressRepository.findByUserId(any(UserId.class))).thenReturn(Optional.of(existingAddress));

        String json = """
                {
                    "street": "New Street",
                    "city": "New City",
                    "postalCode": "12345678",
                    "number": "200"
                }
                """;

        mockMvc.perform(post("/restaurant-owners/address")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .with(user(buildMockRestaurantOwner()))
                        .with(csrf()))
                .andExpect(status().isConflict());
    }

    @Test
    void shouldUpdateAddress() throws Exception {
        Address existingAddress = new Address(
                AddressId.of(1L),
                "Old Street",
                "Old City",
                new PostalCode("12345678"),
                "100",
                UserId.of(1L)
        );

        Address updatedAddress = new Address(
                AddressId.of(1L),
                "Updated Street",
                "Updated City",
                new PostalCode("87654321"),
                "300",
                UserId.of(1L)
        );

        when(addressRepository.findByUserId(any(UserId.class))).thenReturn(Optional.of(existingAddress));
        when(addressRepository.save(any(Address.class))).thenReturn(updatedAddress);

        String json = """
                {
                    "street": "Updated Street",
                    "city": "Updated City",
                    "postalCode": "87654321",
                    "number": "300"
                }
                """;

        mockMvc.perform(put("/restaurant-owners/address")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .with(user(buildMockRestaurantOwner()))
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
                    "number": "300"
                }
                """;

        mockMvc.perform(put("/restaurant-owners/address")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .with(user(buildMockRestaurantOwner()))
                        .with(csrf()))
                .andExpect(status().isNotFound());
    }
}
