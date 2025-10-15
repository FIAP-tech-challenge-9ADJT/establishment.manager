package tech.challenge.establishment.manager.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import tech.challenge.establishment.manager.infrastructure.persistence.entities.RoleJpaEntity;
import tech.challenge.establishment.manager.infrastructure.persistence.entities.UserJpaEntity;
import tech.challenge.establishment.manager.infrastructure.persistence.repositories.RestaurantAddressJpaRepository;
import tech.challenge.establishment.manager.infrastructure.persistence.repositories.RestaurantJpaRepository;
import tech.challenge.establishment.manager.infrastructure.persistence.repositories.RoleJpaRepository;
import tech.challenge.establishment.manager.infrastructure.persistence.repositories.UserJpaRepository;

import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class RestaurantIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserJpaRepository userRepository;

    @Autowired
    private RoleJpaRepository roleRepository;

    @Autowired
    private RestaurantJpaRepository restaurantRepository;

    @Autowired
    private RestaurantAddressJpaRepository restaurantAddressRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    private String ownerToken;
    private String adminToken;

    @BeforeEach
    void setUp() throws Exception {
        restaurantRepository.deleteAll();
        restaurantAddressRepository.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll();

        RoleJpaEntity ownerRole = new RoleJpaEntity();
        ownerRole.setName(RoleJpaEntity.RoleName.RESTAURANT_OWNER);
        ownerRole = roleRepository.save(ownerRole);

        RoleJpaEntity adminRole = new RoleJpaEntity();
        adminRole.setName(RoleJpaEntity.RoleName.ADMIN);
        adminRole = roleRepository.save(adminRole);

        UserJpaEntity owner = new UserJpaEntity();
        owner.setName("Restaurant Owner");
        owner.setEmail("owner@example.com");
        owner.setLogin("owner");
        owner.setPassword(passwordEncoder.encode("password123"));
        owner.setRoles(Set.of(ownerRole));
        userRepository.save(owner);

        UserJpaEntity admin = new UserJpaEntity();
        admin.setName("Admin User");
        admin.setEmail("admin@example.com");
        admin.setLogin("admin");
        admin.setPassword(passwordEncoder.encode("password123"));
        admin.setRoles(Set.of(adminRole));
        userRepository.save(admin);

        ownerToken = getAuthToken("owner", "password123");
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
    void shouldCreateRestaurant() throws Exception {
        String json = """
                {
                    "name": "Test Restaurant",
                    "kitchenType": "Italian",
                    "startOperation": "10:00",
                    "endOperation": "22:00",
                    "restaurantAddress": {
                        "street": "Restaurant Street",
                        "city": "Restaurant City",
                        "postalCode": "12345678",
                        "number": "100"
                    }
                }
                """;

        mockMvc.perform(post("/restaurants")
                        .header("Authorization", "Bearer " + ownerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Test Restaurant"))
                .andExpect(jsonPath("$.kitchenType").value("Italian"));
    }

    @Test
    void shouldGetAllRestaurants() throws Exception {
        String createJson = """
                {
                    "name": "Restaurant 1",
                    "kitchenType": "Italian",
                    "capacity": 50,
                    "startOperation": "10:00",
                    "endOperation": "22:00",
                    "restaurantAddress": {
                        "street": "Street 1",
                        "city": "City 1",
                        "postalCode": "12345678",
                        "number": "10"
                    }
                }
                """;

        mockMvc.perform(post("/restaurants")
                        .header("Authorization", "Bearer " + ownerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createJson))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/restaurants")
                        .header("Authorization", "Bearer " + adminToken)) 
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Restaurant 1"))
                .andExpect(jsonPath("$[0].kitchenType").value("Italian"));
    }

    @Test
    void shouldGetRestaurantById() throws Exception {
        String createJson = """
                {
                    "name": "Test Restaurant",
                    "kitchenType": "Japanese",
                    "capacity": 30,
                    "startOperation": "11:00",
                    "endOperation": "23:00",
                    "restaurantAddress": {
                        "street": "Test Street",
                        "city": "Test City",
                        "postalCode": "87654321",
                        "number": "20"
                    }
                }
                """;

        String response = mockMvc.perform(post("/restaurants")
                        .header("Authorization", "Bearer " + ownerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createJson))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long restaurantId = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(get("/restaurants/" + restaurantId)
                        .header("Authorization", "Bearer " + adminToken)) 
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Restaurant"))
                .andExpect(jsonPath("$.kitchenType").value("Japanese"));
    }

    @Test
    void shouldUpdateRestaurant() throws Exception {
        String createJson = """
                {
                    "name": "Original Restaurant",
                    "kitchenType": "Italian",
                    "capacity": 50,
                    "startOperation": "10:00",
                    "endOperation": "22:00",
                    "restaurantAddress": {
                        "street": "Original Street",
                        "city": "Original City",
                        "postalCode": "12345678",
                        "number": "30"
                    }
                }
                """;

        String createResponse = mockMvc.perform(post("/restaurants")
                        .header("Authorization", "Bearer " + ownerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createJson))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long restaurantId = objectMapper.readTree(createResponse).get("id").asLong();

        String updateJson = """
                {
                    "name": "Updated Restaurant",
                    "kitchenType": "Brazilian",
                    "capacity": 75,
                    "startOperation": "09:00",
                    "endOperation": "23:00",
                    "restaurantAddress": {
                        "street": "Updated Street",
                        "city": "Updated City",
                        "postalCode": "87654321",
                        "number": "75"
                    }
                }
                """;

        mockMvc.perform(put("/restaurants/" + restaurantId)
                        .header("Authorization", "Bearer " + adminToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Restaurant"))
                .andExpect(jsonPath("$.kitchenType").value("Brazilian"));
    }

    @Test
    void shouldSearchRestaurantsByName() throws Exception {
        String createJson1 = """
                {
                    "name": "Pizza Palace",
                    "kitchenType": "Italian",
                    "capacity": 40,
                    "startOperation": "11:00",
                    "endOperation": "23:00",
                    "restaurantAddress": {
                        "street": "Pizza Street",
                        "city": "Pizza City",
                        "postalCode": "11111111",
                        "number": "40"
                    }
                }
                """;

        mockMvc.perform(post("/restaurants")
                        .header("Authorization", "Bearer " + ownerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createJson1))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/restaurants/search")
                        .param("name", "Pizza")
                        .header("Authorization", "Bearer " + adminToken)) 
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Pizza Palace"));
    }

    @Test
    void shouldDeleteRestaurant() throws Exception {
        String createJson = """
                {
                    "name": "To Delete Restaurant",
                    "kitchenType": "Mexican",
                    "capacity": 30,
                    "startOperation": "12:00",
                    "endOperation": "22:00",
                    "restaurantAddress": {
                        "street": "Delete Street",
                        "city": "Delete City",
                        "postalCode": "99999999",
                        "number": "99"
                    }
                }
                """;

        String createResponse = mockMvc.perform(post("/restaurants")
                        .header("Authorization", "Bearer " + ownerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createJson))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long restaurantId = objectMapper.readTree(createResponse).get("id").asLong();

        mockMvc.perform(delete("/restaurants/" + restaurantId)
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/restaurants/" + restaurantId)
                        .header("Authorization", "Bearer " + adminToken))
                .andExpect(status().isNotFound());  
    }

    @Test
    void shouldReturnNotFoundForNonExistentRestaurant() throws Exception {
        mockMvc.perform(get("/restaurants/99999")
                        .header("Authorization", "Bearer " + adminToken)) 
                .andExpect(status().isNotFound()); 
    }
}

