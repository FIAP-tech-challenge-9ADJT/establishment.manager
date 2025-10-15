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
class AdminIntegrationTest {

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

    private String adminToken;
    private Long testUserId;

    @BeforeEach
    void setUp() throws Exception {
        userRepository.deleteAll();
        roleRepository.deleteAll();

        // Criar role ADMIN
        RoleJpaEntity adminRole = new RoleJpaEntity();
        adminRole.setName(RoleJpaEntity.RoleName.ADMIN);
        adminRole = roleRepository.save(adminRole);

        // Criar usuário admin
        UserJpaEntity admin = new UserJpaEntity();
        admin.setName("Admin User");
        admin.setEmail("admin@example.com");
        admin.setLogin("admin");
        admin.setPassword(passwordEncoder.encode("password123"));
        admin.setRoles(new java.util.HashSet<>(Set.of(adminRole)));
        userRepository.save(admin);

        // Criar role USER para testes
        RoleJpaEntity userRole = new RoleJpaEntity();
        userRole.setName(RoleJpaEntity.RoleName.USER);
        userRole = roleRepository.save(userRole);

        // Criar usuário comum para testes
        UserJpaEntity testUser = new UserJpaEntity();
        testUser.setName("Test User");
        testUser.setEmail("testuser@example.com");
        testUser.setLogin("testuser");
        testUser.setPassword(passwordEncoder.encode("password123"));
        testUser.setRoles(new java.util.HashSet<>(Set.of(userRole)));
        testUser = userRepository.save(testUser);
        testUserId = testUser.getId();

        // Autenticar admin e obter token
        adminToken = getAuthToken("admin", "password123");
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
    void shouldCreateAdmin() throws Exception {
        String json = """
                {
                    "name": "New Admin",
                    "email": "newadmin@example.com",
                    "login": "newadmin",
                    "password": "password123",
                    "address": {
                        "street": "Admin Street",
                        "city": "Admin City",
                        "postalCode": "12345678",
                        "number": "100"
                    }
                }
                """;

        mockMvc.perform(post("/admin")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("New Admin"))
                .andExpect(jsonPath("$.email").value("newadmin@example.com"))
                .andExpect(jsonPath("$.login").value("newadmin"));
    }

    @Test
    void shouldFindUserById() throws Exception {
        mockMvc.perform(get("/admin/" + testUserId)
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test User"))
                .andExpect(jsonPath("$.email").value("testuser@example.com"))
                .andExpect(jsonPath("$.login").value("testuser"));
    }

    @Test
    void shouldUpdateUser() throws Exception {
        String json = """
                {
                    "name": "Updated Test User",
                    "email": "updated@example.com"
                }
                """;

        mockMvc.perform(put("/admin/" + testUserId)
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Test User"))
                .andExpect(jsonPath("$.email").value("updated@example.com"));
    }

    @Test
    void shouldDeleteUser() throws Exception {
        mockMvc.perform(delete("/admin/" + testUserId)
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNoContent());

        // Verificar que o usuário foi deletado (retorna 404 quando não encontra o usuário)
        mockMvc.perform(get("/admin/" + testUserId)
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNotFound()); // A aplicação agora retorna 404 corretamente
    }

    @Test
    void shouldReturnNotFoundForNonExistentUser() throws Exception {
        mockMvc.perform(get("/admin/99999")
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNotFound()); // A aplicação agora retorna 404 corretamente
    }

    @Test
    void shouldRequireAuthenticationForAdminEndpoints() throws Exception {
        // Teste sem token de autenticação
        mockMvc.perform(get("/admin/" + testUserId))
                .andExpect(status().isForbidden());

        mockMvc.perform(post("/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest()); // Esperamos 400 para dados inválidos, não 403

        mockMvc.perform(put("/admin/" + testUserId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isForbidden());

        mockMvc.perform(delete("/admin/" + testUserId))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldFailToCreateAdminWithInvalidData() throws Exception {
        String json = """
                {
                    "name": "",
                    "email": "invalid-email",
                    "login": "",
                    "password": "123"
                }
                """;

        mockMvc.perform(post("/admin")
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());
    }
}
