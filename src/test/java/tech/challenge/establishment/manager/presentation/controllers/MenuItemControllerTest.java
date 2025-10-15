package tech.challenge.establishment.manager.presentation.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import tech.challenge.establishment.manager.application.services.MenuItemApplicationService;
import tech.challenge.establishment.manager.domain.entities.MenuItem;
import tech.challenge.establishment.manager.domain.valueobjects.MenuItemId;
import tech.challenge.establishment.manager.domain.valueobjects.RestaurantId;
import tech.challenge.establishment.manager.infrastructure.persistence.entities.RoleJpaEntity;
import tech.challenge.establishment.manager.infrastructure.persistence.entities.UserJpaEntity;
import tech.challenge.establishment.manager.infrastructure.persistence.repositories.UserJpaRepository;
import tech.challenge.establishment.manager.infrastructure.security.TokenService;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class MenuItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MenuItemApplicationService menuItemApplicationService;
    
    @MockBean
    private UserJpaRepository userJpaRepository;
    
    @MockBean
    private TokenService tokenService;
    
    private UsernamePasswordAuthenticationToken authenticatedUser() {
        RoleJpaEntity userRole = new RoleJpaEntity();
        userRole.setId(2L);
        userRole.setName(RoleJpaEntity.RoleName.USER);

        UserJpaEntity user = new UserJpaEntity();
        user.setId(456L);
        user.setRoles(Set.of(userRole));

        return new UsernamePasswordAuthenticationToken(
            user, null, List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }

    private UserJpaEntity buildMockUser() {
        RoleJpaEntity ownerRole = new RoleJpaEntity();
        ownerRole.setId(2L);
        ownerRole.setName(RoleJpaEntity.RoleName.RESTAURANT_OWNER);

        UserJpaEntity user = new UserJpaEntity();
        user.setId(456L);
        user.setRoles(Set.of(ownerRole));
        return user;
    }

    @Test
    void shouldCreateMenuItem() throws Exception {
        MenuItem menuItem = MenuItem.of(
            1L,
            "Test Item",
            "Test Description",
            25.50,
            "https://example.com/photo.jpg",
            RestaurantId.of(1L)
        );
        when(menuItemApplicationService.createMenuItem(any(), any(), any(), any(), any(), any(), anyBoolean()))
            .thenReturn(menuItem);
        String json = """
            {
                "name": "Test Item",
                "description": "Test Description",
                "price": 25.50,
                "photoUrl": "https://example.com/photo.jpg",
                "restaurantId": 1
            }
        """;
        mockMvc.perform(post("/menu-items")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .with(user(buildMockUser()))
                .with(csrf()))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value("Test Item"))
            .andExpect(jsonPath("$.price").value(25.50));
    }

    @Test
    void shouldUpdateMenuItem() throws Exception {
        MenuItem menuItem = MenuItem.of(
            1L,
            "Updated Item",
            "Updated Description",
            30.00,
            "https://example.com/updated.jpg",
            RestaurantId.of(1L)
        );
        when(menuItemApplicationService.updateMenuItem(any(), any(), any(), any(), any(), any(), any(), anyBoolean()))
            .thenReturn(menuItem);
        String json = """
            {
                "name": "Updated Item",
                "description": "Updated Description",
                "price": 30.00,
                "photoUrl": "https://example.com/updated.jpg",
                "restaurantId": 1
            }
        """;
        mockMvc.perform(put("/menu-items/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .with(user(buildMockUser()))
                .with(csrf()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Updated Item"));
    }

    @Test
    void shouldDeleteMenuItem() throws Exception {
        doNothing().when(menuItemApplicationService).deleteMenuItem(any(), any(), anyBoolean());
        mockMvc.perform(delete("/menu-items/1")
                .with(user(buildMockUser()))
                .with(csrf()))
            .andExpect(status().isNoContent());
    }
    
    @Test
    void shouldGetMenuItemById() throws Exception {
        MenuItem menuItem = MenuItem.of(
                1L,
                "Test Item",
                "Test Description",
                25.50,
                "https://example.com/photo.jpg",
                RestaurantId.of(1L)
        );

        when(menuItemApplicationService.getMenuItemById(any(MenuItemId.class))).thenReturn(menuItem);

        mockMvc.perform(get("/menu-items/1")
        		.with(user(buildMockUser())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Item"));
    }

    @Test
    void shouldGetAllMenuItemsByRestaurant() throws Exception {
        MenuItem item1 = MenuItem.of(
                1L,
                "Item 1",
                "Description 1",
                20.00,
                "https://example.com/item1.jpg",
                RestaurantId.of(1L)
        );

        MenuItem item2 = MenuItem.of(
                2L,
                "Item 2",
                "Description 2",
                30.00,
                "https://example.com/item2.jpg",
                RestaurantId.of(1L)
        );

        when(menuItemApplicationService.getAllMenuItems(any(RestaurantId.class)))
                .thenReturn(List.of(item1, item2));

        mockMvc.perform(get("/menu-items/restaurant/1")
        		.with(user(buildMockUser())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Item 1"))
                .andExpect(jsonPath("$[1].name").value("Item 2"));
    }
}

