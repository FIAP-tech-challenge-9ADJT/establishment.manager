package tech.challenge.establishment.manager.presentation.controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.challenge.establishment.manager.application.services.MenuItemApplicationService;
import tech.challenge.establishment.manager.domain.entities.MenuItem;
import tech.challenge.establishment.manager.domain.valueobjects.MenuItemId;
import tech.challenge.establishment.manager.domain.valueobjects.RestaurantId;
import tech.challenge.establishment.manager.presentation.dtos.menuItem.CreateMenuItemDTO;
import tech.challenge.establishment.manager.presentation.dtos.menuItem.MenuItemResponseDTO;
import tech.challenge.establishment.manager.presentation.dtos.menuItem.UpdateMenuItemDTO;
import tech.challenge.establishment.manager.presentation.mappers.MenuItemDtoMapper;

import java.util.List;

@RestController
@RequestMapping("/menu-items")
public class MenuItemController {

    private final MenuItemApplicationService menuItemApplicationService;

    public MenuItemController(MenuItemApplicationService menuItemApplicationService) {
        this.menuItemApplicationService = menuItemApplicationService;
    }

    @PostMapping
    public ResponseEntity<MenuItemResponseDTO> createMenuItem(
            @Valid @RequestBody CreateMenuItemDTO dto
    ) {
        MenuItem created = menuItemApplicationService.createMenuItem(
                dto.name(),
                dto.description(),
                dto.price(),
                dto.photoUrl(),
                RestaurantId.of(dto.restaurantId())
        );
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(MenuItemDtoMapper.toResponseDto(created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MenuItemResponseDTO> updateMenuItem(
            @PathVariable Long id,
            @Valid @RequestBody UpdateMenuItemDTO dto
    ) {
        MenuItem updated = menuItemApplicationService.updateMenuItem(
                MenuItemId.of(id),
                dto.name(),
                dto.description(),
                dto.price(),
                dto.photoUrl(),
                RestaurantId.of(dto.restaurantId())
        );
        return ResponseEntity.ok(MenuItemDtoMapper.toResponseDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMenuItem(@PathVariable Long id) {
        menuItemApplicationService.deleteMenuItem(MenuItemId.of(id));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MenuItemResponseDTO> getMenuItemById(@PathVariable Long id) {
        MenuItem menuItem = menuItemApplicationService.getMenuItemById(MenuItemId.of(id));
        return ResponseEntity.ok(MenuItemDtoMapper.toResponseDto(menuItem));
    }

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<MenuItemResponseDTO>> getAllMenuItems(
            @PathVariable Long restaurantId
    ) {
        List<MenuItem> menuItems = menuItemApplicationService.getAllMenuItems(RestaurantId.of(restaurantId));
        return ResponseEntity.ok(menuItems.stream()
                .map(MenuItemDtoMapper::toResponseDto)
                .toList());
    }
}