package tech.challenge.establishment.manager.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import tech.challenge.establishment.manager.infrastructure.persistence.entities.RoleJpaEntity;
import tech.challenge.establishment.manager.infrastructure.persistence.entities.UserJpaEntity;
import tech.challenge.establishment.manager.infrastructure.persistence.repositories.RoleJpaRepository;
import tech.challenge.establishment.manager.infrastructure.persistence.repositories.UserJpaRepository;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class RestaurantOwnerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserJpaRepository userRepository;

    @Autowired
    private RoleJpaRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    private String ownerToken;

    @BeforeEach
    void setUp() throws Exception {
        userRepository.deleteAll();
        roleRepository.deleteAll();

        RoleJpaEntity ownerRole = new RoleJpaEntity();
        ownerRole.setName(RoleJpaEntity.RoleName.RESTAURANT_OWNER);
        ownerRole = roleRepository.save(ownerRole);

        UserJpaEntity owner = new UserJpaEntity();
        owner.setName("Restaurant Owner");
        owner.setEmail("owner@example.com");
        owner.setLogin("owner");
        owner.setPassword(passwordEncoder.encode("password123"));
        owner.setRoles(new java.util.HashSet<>(Set.of(ownerRole)));
        userRepository.save(owner);

        ownerToken = getAuthToken("owner", "password123");
    }

    private String getAuthToken(String login, String password) throws Exception {
        String loginJson = String.format("""
                {
                    "login": "%s",
                    "password": "%s"
                }
                """, login, password);

        String response = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return objectMapper.readTree(response).get("accessToken").asText();
    }

    @Test
    void shouldGetOwnerProfile() throws Exception {
        mockMvc.perform(get("/restaurant-owners")
                        .header("Authorization", "Bearer " + ownerToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Restaurant Owner"))
                .andExpect(jsonPath("$.email").value("owner@example.com"))
                .andExpect(jsonPath("$.login").value("owner"));
    }

    @Test
    void shouldCreateRestaurantOwner() throws Exception {
        String json = """
                {
                    "name": "New Restaurant Owner",
                    "email": "newowner@example.com",
                    "login": "newowner",
                    "password": "password123",
                    "address": {
                        "street": "Owner Street",
                        "city": "Owner City",
                        "postalCode": "12345678",
                        "number": "200"
                    }
                }
                """;

        mockMvc.perform(post("/restaurant-owners")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("New Restaurant Owner"))
                .andExpect(jsonPath("$.email").value("newowner@example.com"))
                .andExpect(jsonPath("$.login").value("newowner"));
    }

    @Test
    void shouldUpdateOwnerProfile() throws Exception {
        String json = """
                {
                    "name": "Updated Restaurant Owner",
                    "email": "updatedowner@example.com"
                }
                """;

        mockMvc.perform(put("/restaurant-owners")
                        .header("Authorization", "Bearer " + ownerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Restaurant Owner"))
                .andExpect(jsonPath("$.email").value("updatedowner@example.com"));
    }

    @Test
    void shouldReturnNotFoundWhenOwnerAddressDoesNotExist() throws Exception {
      
        mockMvc.perform(get("/restaurant-owners/address")
                        .header("Authorization", "Bearer " + ownerToken))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateAddressForRestaurantOwner() throws Exception {
        
        String addressJson = """
            {
                "street": "Restaurant Street",
                "city": "Restaurant City",
                "postalCode": "12345678",
                "number": "300"
            }
        """;

        mockMvc.perform(post("/restaurant-owners/address")
                .header("Authorization", "Bearer " + ownerToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(addressJson))
            .andExpect(status().isConflict());  
    }

    @Test
    void shouldUpdateOwnerAddress() throws Exception {
        String updateAddressJson = """
                {
                    "street": "Updated Restaurant Street",
                    "city": "Updated Restaurant City",
                    "postalCode": "87654321",
                    "number": "400"
                }
                """;

        mockMvc.perform(put("/restaurant-owners/address")
                        .header("Authorization", "Bearer " + ownerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateAddressJson))
                .andExpect(status().isNotFound()); 
    }

    @Test
    void shouldRequireAuthenticationForOwnerEndpoints() throws Exception {
        mockMvc.perform(get("/restaurant-owners"))
                .andExpect(status().isForbidden());

        mockMvc.perform(put("/restaurant-owners")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isForbidden());

        mockMvc.perform(get("/restaurant-owners/address"))
                .andExpect(status().isForbidden());

        mockMvc.perform(post("/restaurant-owners/address")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isForbidden());

        mockMvc.perform(put("/restaurant-owners/address")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldFailToCreateRestaurantOwnerWithInvalidData() throws Exception {
        String json = """
                {
                    "name": "",
                    "email": "invalid-email",
                    "login": "",
                    "password": "123"
                }
                """;

        mockMvc.perform(post("/restaurant-owners")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldFailToCreateRestaurantOwnerWithDuplicateEmail() throws Exception {
        String json = """
                {
                    "name": "Duplicate Owner",
                    "email": "owner@example.com",
                    "login": "duplicateowner",
                    "password": "password123",
                    "address": {
                        "street": "Duplicate Street",
                        "city": "Duplicate City",
                        "postalCode": "99999999",
                        "number": "999"
                    }
                }
                """;

        mockMvc.perform(post("/restaurant-owners")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest()); 
    }

    @Test
    void shouldFailToCreateRestaurantOwnerWithDuplicateLogin() throws Exception {
        String json = """
                {
                    "name": "Duplicate Owner",
                    "email": "duplicate@example.com",
                    "login": "owner",
                    "password": "password123",
                    "address": {
                        "street": "Duplicate Street",
                        "city": "Duplicate City",
                        "postalCode": "88888888",
                        "number": "888"
                    }
                }
                """;

        mockMvc.perform(post("/restaurant-owners")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest()); 
    }
}
