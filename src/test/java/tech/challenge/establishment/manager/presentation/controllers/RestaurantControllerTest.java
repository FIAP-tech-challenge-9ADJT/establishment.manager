package tech.challenge.establishment.manager.presentation.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import tech.challenge.establishment.manager.application.services.RestaurantApplicationService;
import tech.challenge.establishment.manager.domain.entities.Restaurant;
import tech.challenge.establishment.manager.domain.entities.RestaurantAddress;
import tech.challenge.establishment.manager.domain.valueobjects.KitchenType;
import tech.challenge.establishment.manager.domain.valueobjects.Name;
import tech.challenge.establishment.manager.domain.valueobjects.PostalCode;
import tech.challenge.establishment.manager.domain.valueobjects.RestaurantAddressId;
import tech.challenge.establishment.manager.domain.valueobjects.RestaurantId;
import tech.challenge.establishment.manager.domain.valueobjects.UserId;
import tech.challenge.establishment.manager.infrastructure.persistence.entities.RoleJpaEntity;
import tech.challenge.establishment.manager.infrastructure.persistence.entities.RoleJpaEntity.RoleName;
import tech.challenge.establishment.manager.infrastructure.persistence.entities.UserJpaEntity;
import tech.challenge.establishment.manager.infrastructure.persistence.repositories.UserJpaRepository;
import tech.challenge.establishment.manager.presentation.dtos.restaurant.CreateRestaurantDTO;
import tech.challenge.establishment.manager.presentation.dtos.restaurant.UpdateRestaurantDTO;
import tech.challenge.establishment.manager.presentation.dtos.restaurantAddress.CreateRestaurantAddressDTO;
import tech.challenge.establishment.manager.presentation.dtos.restaurantAddress.UpdateRestaurantAddressDTO;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class RestaurantControllerTest {

    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private RestaurantApplicationService restaurantService;

    @MockBean
    private UserJpaRepository userJpaRepository;

    private UserJpaEntity buildMockUser(Long id, String... roles) {
        UserJpaEntity user = new UserJpaEntity();
        user.setId(id);
        Set<RoleJpaEntity> roleSet = Arrays.stream(roles)
                .map(role -> {
                    RoleJpaEntity r = new RoleJpaEntity();
                    r.setName(RoleName.valueOf(role));
                    return r;
                }).collect(Collectors.toSet());
        user.setRoles(roleSet);
        return user;
    }
    
    private SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor adminUser() {
        return SecurityMockMvcRequestPostProcessors.user("admin").roles("ADMIN");
    }

    private SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor ownerUser() {
        return SecurityMockMvcRequestPostProcessors.user("owner").roles("RESTAURANT_OWNER");
    }

    private SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor simpleUser() {
        return SecurityMockMvcRequestPostProcessors.user("user").roles("USER");
    }

    @Test
    void shouldCreateRestaurant() throws Exception {
       
        CreateRestaurantAddressDTO addressDTO = new CreateRestaurantAddressDTO(
            "Rua Um", "Cidade XPTO", "12345-000", "123"
        );
        
        CreateRestaurantDTO dto = new CreateRestaurantDTO(
            "Restaurante Teste", addressDTO, "Italian",
            LocalTime.of(10, 0), LocalTime.of(22, 0)
        );
        
        UserJpaEntity fakeUser = buildMockUser(123L, "RESTAURANT_OWNER");

       
        RestaurantId restId = RestaurantId.of(100L);
        Name restName = Name.of("Restaurante Teste");
        KitchenType kitchenType = new KitchenType("Italian");
        LocalTime startOp = LocalTime.of(10, 0);
        LocalTime endOp = LocalTime.of(22, 0);

        RestaurantAddress addressMock = Mockito.mock(RestaurantAddress.class);
        Mockito.when(addressMock.getId()).thenReturn(RestaurantAddressId.of(100L));
        Mockito.when(addressMock.getStreet()).thenReturn("Rua Um");
        Mockito.when(addressMock.getCity()).thenReturn("Cidade XPTO");
        Mockito.when(addressMock.getPostalCode()).thenReturn(new PostalCode("12345-000"));
        Mockito.when(addressMock.getNumber()).thenReturn("123");

        Restaurant restaurantMock = Mockito.mock(Restaurant.class);
        Mockito.when(restaurantMock.getId()).thenReturn(restId);
        Mockito.when(restaurantMock.getName()).thenReturn(restName);
        Mockito.when(restaurantMock.getOwnerId()).thenReturn(UserId.of(123L));
        Mockito.when(restaurantMock.getKitchenType()).thenReturn(kitchenType);
        Mockito.when(restaurantMock.getStartOperation()).thenReturn(startOp);
        Mockito.when(restaurantMock.getEndOperation()).thenReturn(endOp);
        Mockito.when(restaurantMock.getRestaurantAddress()).thenReturn(addressMock);

        Mockito.when(restaurantService.createRestaurant(
             Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()
         )).thenReturn(restaurantMock);

        mockMvc.perform(post("/restaurants")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
                .with(user(fakeUser))
                .with(csrf()))
            .andExpect(status().isCreated());
    }

    @Test
    void shouldUpdateRestaurant() throws Exception {
        Long restaurantId = 44L;
        
        UpdateRestaurantAddressDTO addressDTO = new UpdateRestaurantAddressDTO(
            "Rua Dois", "Nova Cidade", "88888-888", "999"
        );
        
        UpdateRestaurantDTO dto = new UpdateRestaurantDTO(
            "Restaurante Atualizado",
            addressDTO,
            "French",
            LocalTime.of(9, 0),
            LocalTime.of(20, 0)
        );
        
        UserJpaEntity fakeUser = buildMockUser(44L, "RESTAURANT_OWNER");

        RestaurantId restId = RestaurantId.of(restaurantId);
        Name restName = Name.of("Restaurante Atualizado");
        KitchenType kitchenType = new KitchenType("French");
        LocalTime startOp = LocalTime.of(9, 0);
        LocalTime endOp = LocalTime.of(20, 0);

        RestaurantAddress addressMock = Mockito.mock(RestaurantAddress.class);
        Mockito.when(addressMock.getId()).thenReturn(RestaurantAddressId.of(restaurantId));
        Mockito.when(addressMock.getStreet()).thenReturn("Rua Dois");
        Mockito.when(addressMock.getCity()).thenReturn("Nova Cidade");
        Mockito.when(addressMock.getPostalCode()).thenReturn(new PostalCode("88888-888"));
        Mockito.when(addressMock.getNumber()).thenReturn("999");

        Restaurant restaurantMock = Mockito.mock(Restaurant.class);
        Mockito.when(restaurantMock.getId()).thenReturn(restId);
        Mockito.when(restaurantMock.getName()).thenReturn(restName);
        Mockito.when(restaurantMock.getOwnerId()).thenReturn(UserId.of(44L));
        Mockito.when(restaurantMock.getKitchenType()).thenReturn(kitchenType);
        Mockito.when(restaurantMock.getStartOperation()).thenReturn(startOp);
        Mockito.when(restaurantMock.getEndOperation()).thenReturn(endOp);
        Mockito.when(restaurantMock.getRestaurantAddress()).thenReturn(addressMock);

        Mockito.when(restaurantService.getRestaurantById(Mockito.any())).thenReturn(restaurantMock);
        Mockito.when(restaurantService.updateRestaurant(
            Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()
        )).thenReturn(restaurantMock);

        mockMvc.perform(put("/restaurants/" + restaurantId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto))
                .with(user(fakeUser))
                .with(csrf()))
            .andExpect(status().isOk());
    }


    @Test
    void shouldGetAllRestaurants() throws Exception {
        RestaurantAddress address1 = RestaurantAddress.of(
                1L, "Street 1", "City 1", "12345678", "123", RestaurantId.of(1L)
        );
        RestaurantAddress address2 = RestaurantAddress.of(
                2L, "Street 2", "City 2", "87654321", "456", RestaurantId.of(2L)
        );
        Restaurant restaurant1 = Restaurant.of(
                1L, "Restaurant 1", address1, KitchenType.of("Italian"),
                LocalTime.of(10, 0), LocalTime.of(22, 0), UserId.of(1L)
        );
        Restaurant restaurant2 = Restaurant.of(
                2L, "Restaurant 2", address2, KitchenType.of("Japanese"),
                LocalTime.of(11, 0), LocalTime.of(23, 0), UserId.of(2L)
        );

        when(restaurantService.getAllRestaurants()).thenReturn(List.of(restaurant1, restaurant2));

        mockMvc.perform(get("/restaurants")
                        .with(simpleUser()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Restaurant 1"))
                .andExpect(jsonPath("$[1].name").value("Restaurant 2"));
    }

    @Test
    void shouldGetRestaurantById() throws Exception {
        RestaurantAddress address = RestaurantAddress.of(
                1L, "Test Street", "Test City", "12345678", "123", RestaurantId.of(1L)
        );
        Restaurant restaurant = Restaurant.of(
                1L, "Test Restaurant", address, KitchenType.of("Italian"),
                LocalTime.of(10, 0), LocalTime.of(22, 0), UserId.of(1L)
        );
        when(restaurantService.getRestaurantById(any(RestaurantId.class))).thenReturn(restaurant);

        mockMvc.perform(get("/restaurants/1")
                        .with(simpleUser()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Restaurant"));
    }

    @Test
    void shouldSearchRestaurantsByName() throws Exception {
        RestaurantAddress address = RestaurantAddress.of(
                1L, "Test Street", "Test City", "12345678", "123", RestaurantId.of(1L)
        );
        Restaurant restaurant = Restaurant.of(
                1L, "Pizza Place", address, KitchenType.of("Italian"),
                LocalTime.of(10, 0), LocalTime.of(22, 0), UserId.of(1L)
        );
        when(restaurantService.getRestaurantsByName("Pizza")).thenReturn(List.of(restaurant));

        mockMvc.perform(get("/restaurants/search").param("name", "Pizza")
                        .with(simpleUser()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Pizza Place"));
    }
    @Test
    void shouldDeleteRestaurant() throws Exception {
        Long restaurantId = 10L;
        UserJpaEntity fakeUser = buildMockUser(88L, "RESTAURANT_OWNER");

        var restaurant = Mockito.mock(tech.challenge.establishment.manager.domain.entities.Restaurant.class);
        Mockito.when(restaurant.getOwnerId()).thenReturn(UserId.of(88L));
        Mockito.when(restaurantService.getRestaurantById(any())).thenReturn(restaurant);

        mockMvc.perform(delete("/restaurants/" + restaurantId)
            .with(user(fakeUser))
            .with(csrf()))
            .andExpect(status().isNoContent());
    }

}