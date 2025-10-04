package tech.challenge.establishment.manager.presentation.controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import tech.challenge.establishment.manager.application.services.RestaurantApplicationService;
import tech.challenge.establishment.manager.domain.valueobjects.KitchenType;
import tech.challenge.establishment.manager.domain.valueobjects.RestaurantId;
import tech.challenge.establishment.manager.infrastructure.persistence.entities.UserJpaEntity;
import tech.challenge.establishment.manager.presentation.dtos.restaurant.CreateRestaurantDTO;
import tech.challenge.establishment.manager.presentation.dtos.restaurant.RestaurantResponseDTO;
import tech.challenge.establishment.manager.presentation.dtos.restaurant.UpdateRestaurantDTO;
import tech.challenge.establishment.manager.presentation.mappers.AddressDtoMapper;
import tech.challenge.establishment.manager.presentation.mappers.RestaurantAddressDtoMapper;
import tech.challenge.establishment.manager.presentation.mappers.RestaurantDtoMapper;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/restaurants")
public class RestaurantController {

    private final RestaurantApplicationService restaurantService;

    public RestaurantController(RestaurantApplicationService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @PostMapping
    public ResponseEntity<RestaurantResponseDTO> createRestaurant(
            @Valid @RequestBody CreateRestaurantDTO dto,
            @AuthenticationPrincipal UserJpaEntity authenticatedUser) {

        Long ownerId = authenticatedUser.getId();
        var restaurant = RestaurantDtoMapper.fromCreateDto(dto, ownerId);

        var saved = restaurantService.createRestaurant(
                restaurant.getName().value(),
                restaurant.getRestaurantAddress(),
                restaurant.getKitchenType(),
                restaurant.getStartOperation(),
                restaurant.getEndOperation(),
                restaurant.getOwnerId()
        );

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(RestaurantDtoMapper.toResponseDto(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestaurantResponseDTO> updateRestaurant(
            @PathVariable Long id,
            @Valid @RequestBody UpdateRestaurantDTO dto,
            @AuthenticationPrincipal UserJpaEntity authenticatedUser) {

        var existingRestaurant = restaurantService.getRestaurantById(RestaurantId.of(id));
        
        // Validar se o usuário é o dono do restaurante (exceto ADMIN)
        if (!isOwnerOrAdmin(authenticatedUser, existingRestaurant)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        var updated = restaurantService.updateRestaurant(
                RestaurantId.of(id),
                dto.name(),
                RestaurantAddressDtoMapper.fromUpdateDto(dto.restaurantAddress(), existingRestaurant.getRestaurantAddress()),
                dto.startOperation(),
                dto.endOperation(),
                KitchenType.of(dto.kitchenType())
        );

        return ResponseEntity.ok(RestaurantDtoMapper.toResponseDto(updated));
    }

    @GetMapping
    public ResponseEntity<List<RestaurantResponseDTO>> getAllRestaurants() {
        var restaurants = restaurantService.getAllRestaurants();
        var dtos = restaurants.stream()
                .map(RestaurantDtoMapper::toResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantResponseDTO> getRestaurantById(@PathVariable Long id) {
        var restaurant = restaurantService.getRestaurantById(RestaurantId.of(id));
        return ResponseEntity.ok(RestaurantDtoMapper.toResponseDto(restaurant));
    }

    @GetMapping("/search")
    public ResponseEntity<List<RestaurantResponseDTO>> getRestaurantsByName(
            @RequestParam String name) {

        var restaurants = restaurantService.getRestaurantsByName(name);
        var dtos = restaurants.stream()
                .map(RestaurantDtoMapper::toResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRestaurant(
            @PathVariable Long id,
            @AuthenticationPrincipal UserJpaEntity authenticatedUser) {
        
        var existingRestaurant = restaurantService.getRestaurantById(RestaurantId.of(id));
        
        // Validar se o usuário é o dono do restaurante (exceto ADMIN)
        if (!isOwnerOrAdmin(authenticatedUser, existingRestaurant)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        restaurantService.deleteRestaurant(RestaurantId.of(id));
        return ResponseEntity.noContent().build();
    }

    /**
     * Verifica se o usuário é o dono do restaurante ou um administrador
     */
    private boolean isOwnerOrAdmin(UserJpaEntity user, tech.challenge.establishment.manager.domain.entities.Restaurant restaurant) {
        // Se for ADMIN, pode fazer qualquer operação
        boolean isAdmin = user.getRoles().stream()
                .anyMatch(role -> "ADMIN".equals(role.getName().name()));
        
        if (isAdmin) {
            return true;
        }
        
        // Se for RESTAURANT_OWNER, só pode editar seus próprios restaurantes
        boolean isRestaurantOwner = user.getRoles().stream()
                .anyMatch(role -> "RESTAURANT_OWNER".equals(role.getName().name()));
        
        return isRestaurantOwner && restaurant.getOwnerId().value().equals(user.getId());
    }
}