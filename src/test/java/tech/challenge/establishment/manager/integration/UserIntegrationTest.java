package tech.challenge.establishment.manager.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Set;
import java.util.UUID;

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
class UserIntegrationTest {

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

    private RoleJpaEntity userRole;
    private String authToken;

    @BeforeEach
    void setUp() throws Exception {
        userRepository.deleteAll();
        roleRepository.deleteAll();

        userRole = new RoleJpaEntity();
        userRole.setName(RoleJpaEntity.RoleName.USER);
        userRole = roleRepository.save(userRole);

        UserJpaEntity user = new UserJpaEntity();
        user.setName("Test User");
        user.setEmail("test@example.com");
        user.setLogin("testuser");
        user.setPassword(passwordEncoder.encode("password123"));
        
        user.setRoles(new java.util.HashSet<>(Set.of(userRole)));
        user = userRepository.save(user);

        String loginJson = """
                {
                    "login": "testuser",
                    "password": "password123"
                }
                """;

        String response = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        authToken = objectMapper.readTree(response).get("accessToken").asText();
    }

    @Test
    void shouldCreateUser() throws Exception {
        String json = """
                {
                    "name": "New User",
                    "email": "newuser@example.com",
                    "login": "newuser",
                    "password": "password123",
                    "address": {
                        "street": "New Street",
                        "city": "New City",
                        "postalCode": "12345678",
                        "number": "123"
                    }
                }
                """;

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("New User"))
                .andExpect(jsonPath("$.email").value("newuser@example.com"))
                .andExpect(jsonPath("$.login").value("newuser"));
    }

    @Test
    void shouldGetUserProfile() throws Exception {
        mockMvc.perform(get("/users")
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test User"))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.login").value("testuser"));
    }

    @Test
    void shouldUpdateUserProfile() throws Exception {
        mockMvc.perform(get("/users")
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isOk());
        
        String json = """
                {
                    "name": "Updated User",
                    "email": "updated@example.com"
                }
                """;

        mockMvc.perform(put("/users")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated User"))
                .andExpect(jsonPath("$.email").value("updated@example.com"));
    }

    @Test
    void shouldCreateAddressForAuthenticatedUser() throws Exception {
        
        String addressJson = """
            {
                "street": "Rua de Teste",
                "city": "Testolândia",
                "postalCode": "12345678",
                "number": "42"
            }
        """;

        mockMvc.perform(post("/users/address")
                .header("Authorization", "Bearer " + authToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(addressJson))
            .andExpect(status().isConflict()); 
    }

    @Test
    void shouldGetUserAddress() throws Exception {
        
        mockMvc.perform(get("/users/address")
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isNotFound()); 
    }

    @Test
    void shouldUpdateUserAddress() throws Exception {
        String updateAddressJson = """
                {
                    "street": "New Street",
                    "city": "New City",
                    "postalCode": "87654321",
                    "number": "200"
                }
                """;

        mockMvc.perform(put("/users/address")
                        .header("Authorization", "Bearer " + authToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateAddressJson))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnNotFoundWhenAddressDoesNotExist() throws Exception {
        mockMvc.perform(get("/users/address")
                        .header("Authorization", "Bearer " + authToken))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldFailToCreateUserWithDuplicateEmail() throws Exception {
        String json = """
                {
                    "name": "Duplicate User",
                    "email": "test@example.com",
                    "login": "duplicateuser",
                    "password": "password123",
                    "address": {
                        "street": "Duplicate Street",
                        "city": "Duplicate City",
                        "postalCode": "99999999",
                        "number": "999"
                    }
                }
                """;

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest()); 
    }

    @Test
    void shouldFailToCreateUserWithDuplicateLogin() throws Exception {
        String json = """
                {
                    "name": "Duplicate User",
                    "email": "duplicate@example.com",
                    "login": "testuser",
                    "password": "password123",
                    "address": {
                        "street": "Duplicate Street",
                        "city": "Duplicate City",
                        "postalCode": "88888888",
                        "number": "888"
                    }
                }
                """;

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest()); 
    }

    @Test
    void shouldRequireAuthenticationForProfile() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isForbidden());
    }
}

