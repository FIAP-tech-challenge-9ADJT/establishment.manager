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
import tech.challenge.establishment.manager.infrastructure.persistence.repositories.*;

import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class MenuItemIntegrationTest {
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
    private MenuItemJpaRepository menuItemRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ObjectMapper objectMapper;

    private String ownerToken;
    private Long restaurantId;

    @BeforeEach
    void setUp() throws Exception {
        menuItemRepository.deleteAll();
        restaurantRepository.deleteAll();
        restaurantAddressRepository.deleteAll();
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
        owner.setRoles(Set.of(ownerRole));
        userRepository.save(owner);

        ownerToken = getAuthToken("owner", "password123");

        String createRestaurantJson = """
            {
                "name": "Test Restaurant",
                "kitchenType": "Italian",
                "startOperation": "10:00",
                "endOperation": "22:00",
                "restaurantAddress": {
                    "street": "Restaurant Street",
                    "city": "Restaurant City",
                    "postalCode": "12345678",
                    "number": "50"
                }
            }
            """;

        String restaurantResponse = mockMvc.perform(post("/restaurants")
                .header("Authorization", "Bearer " + ownerToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(createRestaurantJson))
            .andExpect(status().isCreated())
            .andReturn()
            .getResponse()
            .getContentAsString();

        restaurantId = objectMapper.readTree(restaurantResponse).get("id").asLong();
    }

    private String getAuthToken(String login, String password) throws Exception {
        String loginJson = """
            {
                "login": "%s",
                "password": "%s"
            }
            """.formatted(login, password);

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
    void shouldCreateMenuItem() throws Exception {
        String json = """
            {
                "name": "Margherita Pizza",
                "description": "Classic Italian pizza with tomato and mozzarella",
                "price": 35.50,
                "photoUrl": "https://example.com/pizza.jpg",
                "restaurantId": %d
            }
            """.formatted(restaurantId);

        mockMvc.perform(post("/menu-items")
                .header("Authorization", "Bearer " + ownerToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value("Margherita Pizza"))
            .andExpect(jsonPath("$.description").value("Classic Italian pizza with tomato and mozzarella"))
            .andExpect(jsonPath("$.price").value(35.50));
    }

    @Test
    void shouldGetMenuItemById() throws Exception {
        String createJson = """
            {
                "name": "Carbonara Pasta",
                "description": "Pasta with eggs, cheese, and bacon",
                "price": 42.00,
                "photoUrl": "https://example.com/carbonara.jpg",
                "restaurantId": %d
            }
            """.formatted(restaurantId);

        String createResponse = mockMvc.perform(post("/menu-items")
                        .header("Authorization", "Bearer " + ownerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createJson))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long menuItemId = objectMapper.readTree(createResponse).get("id").asLong();

        mockMvc.perform(get("/menu-items/" + menuItemId)
                .header("Authorization", "Bearer " + ownerToken))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Carbonara Pasta"))
            .andExpect(jsonPath("$.description").value("Pasta with eggs, cheese, and bacon"))
            .andExpect(jsonPath("$.price").value(42.00));
    }

    @Test
    void shouldUpdateMenuItem() throws Exception {
        String createJson = """
            {
                "name": "Original Item",
                "description": "Original description",
                "price": 25.00,
                "photoUrl": "https://example.com/original.jpg",
                "restaurantId": %d
            }
            """.formatted(restaurantId);

        String createResponse = mockMvc.perform(post("/menu-items")
                        .header("Authorization", "Bearer " + ownerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createJson))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long menuItemId = objectMapper.readTree(createResponse).get("id").asLong();

        String updateJson = """
            {
                "name": "Updated Item",
                "description": "Updated description",
                "price": 35.00,
                "photoUrl": "https://example.com/updated.jpg",
                "restaurantId": %d
            }
            """.formatted(restaurantId);

        mockMvc.perform(put("/menu-items/" + menuItemId)
                .header("Authorization", "Bearer " + ownerToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateJson))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Updated Item"))
            .andExpect(jsonPath("$.description").value("Updated description"))
            .andExpect(jsonPath("$.price").value(35.00));
    }

    @Test
    void shouldDeleteMenuItem() throws Exception {
        String createJson = """
            {
                "name": "To Delete Item",
                "description": "This will be deleted",
                "price": 15.00,
                "photoUrl": "https://example.com/delete.jpg",
                "restaurantId": %d
            }
            """.formatted(restaurantId);

        String createResponse = mockMvc.perform(post("/menu-items")
                        .header("Authorization", "Bearer " + ownerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createJson))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long menuItemId = objectMapper.readTree(createResponse).get("id").asLong();

        mockMvc.perform(delete("/menu-items/" + menuItemId)
                .header("Authorization", "Bearer " + ownerToken))
            .andExpect(status().isNoContent());

        mockMvc.perform(get("/menu-items/" + menuItemId)
                .header("Authorization", "Bearer " + ownerToken))
            .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnNotFoundForNonExistentMenuItem() throws Exception {
        mockMvc.perform(get("/menu-items/99999")
                .header("Authorization", "Bearer " + ownerToken))
            .andExpect(status().isNotFound());
    }
}
