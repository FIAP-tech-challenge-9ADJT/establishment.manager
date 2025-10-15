package tech.challenge.establishment.manager.presentation.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import tech.challenge.establishment.manager.application.services.UserApplicationService;
import tech.challenge.establishment.manager.domain.entities.Role;
import tech.challenge.establishment.manager.domain.entities.User;
import tech.challenge.establishment.manager.domain.usecases.admin.DeleteUserUseCase;
import tech.challenge.establishment.manager.domain.valueobjects.UserId;
import tech.challenge.establishment.manager.infrastructure.persistence.entities.RoleJpaEntity;
import tech.challenge.establishment.manager.infrastructure.persistence.entities.UserJpaEntity;
import tech.challenge.establishment.manager.infrastructure.persistence.repositories.UserJpaRepository;
import tech.challenge.establishment.manager.infrastructure.security.TokenService;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserApplicationService userApplicationService;

    @MockBean
    private DeleteUserUseCase deleteUserUseCase;
    
    @MockBean
    private TokenService tokenService;
    
    @MockBean
    private UserJpaRepository userJpaRepository;

    private UserJpaEntity buildMockAdmin() {
        RoleJpaEntity adminRole = new RoleJpaEntity();
        adminRole.setId(1L);
        adminRole.setName(RoleJpaEntity.RoleName.ADMIN);

        UserJpaEntity user = new UserJpaEntity();
        user.setId(1L);
        user.setLogin("admin");
        user.setRoles(Set.of(adminRole));
        return user;
    }

    @Test
    void shouldCreateAdmin() throws Exception {
        User user = User.of(
                1L,
                "Admin User",
                "admin@example.com",
                "adminuser",
                "password123",
                null,
                Set.of(Role.of(1L, Role.RoleName.ADMIN)),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(userApplicationService.createAdmin(any(), any(), any(), any(), any())).thenReturn(user);

        String json = """
        		{
        		  "name": "Admin User",
        		  "email": "admin@example.com",
        		  "login": "adminuser",
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

        mockMvc.perform(post("/admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Admin User"))
                .andExpect(jsonPath("$.email").value("admin@example.com"));
    }

    @Test
    void shouldFindUserById() throws Exception {
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

        mockMvc.perform(get("/admin/1")
                        .with(user(buildMockAdmin()))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test User"));
    }

    @Test
    void shouldUpdateUser() throws Exception {
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

        mockMvc.perform(put("/admin/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .with(user(buildMockAdmin()))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated User"));
    }

    @Test
    void shouldDeleteUser() throws Exception {
        doNothing().when(deleteUserUseCase).execute(any(UserId.class));

        mockMvc.perform(delete("/admin/1")
                        .with(user(buildMockAdmin()))
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }
}
