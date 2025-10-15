package tech.challenge.establishment.manager.presentation.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import tech.challenge.establishment.manager.application.services.AuthApplicationService;
import tech.challenge.establishment.manager.domain.entities.User;
import tech.challenge.establishment.manager.domain.valueobjects.UserId;
import tech.challenge.establishment.manager.infrastructure.persistence.entities.RoleJpaEntity;
import tech.challenge.establishment.manager.infrastructure.persistence.entities.UserJpaEntity;
import tech.challenge.establishment.manager.infrastructure.persistence.repositories.UserJpaRepository;
import tech.challenge.establishment.manager.infrastructure.security.TokenService;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthApplicationService authApplicationService;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private TokenService tokenService;
    
    @MockBean
    private UserJpaRepository userJpaRepository;

    @Test
    void shouldLoginSuccessfully() throws Exception {
        RoleJpaEntity role = new RoleJpaEntity();
        role.setName(tech.challenge.establishment.manager.infrastructure.persistence.entities.RoleJpaEntity.RoleName.USER);
        role.setId(1L);

        UserJpaEntity userJpaEntity = new UserJpaEntity();
        userJpaEntity.setId(1L);
        userJpaEntity.setLogin("testuser");
        userJpaEntity.setPassword("password123");
        userJpaEntity.setRoles(Set.of(role));

        // Create domain User for the authenticate method
        User domainUser = User.of(
                1L,
                "Test User",
                "test@example.com",
                "testuser",
                "password123",
                null,
                Set.of(tech.challenge.establishment.manager.domain.entities.Role.of(1L, tech.challenge.establishment.manager.domain.entities.Role.RoleName.USER)),
                java.time.LocalDateTime.now(),
                java.time.LocalDateTime.now()
        );

        // Create authentication mock
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(userJpaEntity);
        
        // Mock the authentication manager
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(tokenService.generateToken(userJpaEntity)).thenReturn("mock-jwt-token");

        String json = """
                {
                    "login": "testuser",
                    "password": "password123"
                }
                """;

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("mock-jwt-token"));
    }

    @Test
    void shouldChangePassword() throws Exception {
        doNothing().when(authApplicationService).changePassword(any(UserId.class), any());
        String json = """
          {
            "currentPassword": "senhaAntiga123",
            "newPassword": "novaSenhaForte123",
            "newPasswordConfirmation": "novaSenhaForte123"
          }
          """;
        UserJpaEntity fakeUser = new UserJpaEntity();
        fakeUser.setId(99L);
        fakeUser.setLogin("fakeLogin");
        fakeUser.setPassword("fakePass");

        RoleJpaEntity userRole = new RoleJpaEntity();
        userRole.setName(RoleJpaEntity.RoleName.USER);

        fakeUser.setRoles(Set.of(userRole));

        mockMvc.perform(
            post("/auth/change-password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .with(SecurityMockMvcRequestPostProcessors.authentication(
                    new UsernamePasswordAuthenticationToken(fakeUser, fakeUser.getPassword(), fakeUser.getAuthorities())
                ))
                .with(SecurityMockMvcRequestPostProcessors.csrf())
        ).andExpect(status().isNoContent());
    }

}

