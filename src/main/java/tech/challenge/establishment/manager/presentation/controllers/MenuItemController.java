package tech.challenge.establishment.manager.presentation.controllers;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.challenge.establishment.manager.application.services.MenuItemApplicationService;
import tech.challenge.establishment.manager.domain.valueobjects.MenuItemId;
import tech.challenge.establishment.manager.domain.valueobjects.RestaurantId;
import tech.challenge.establishment.manager.presentation.dtos.menuItem.MenuItemRequestDTO;
import tech.challenge.establishment.manager.presentation.dtos.menuItem.MenuItemResponseDTO;
import tech.challenge.establishment.manager.presentation.mappers.MenuItemMapper;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/menu-items")
public class MenuItemController {

    private final MenuItemApplicationService menuItemService;
    private final MenuItemMapper mapper;

    public MenuItemController(MenuItemApplicationService menuItemService, MenuItemMapper mapper) {
        this.menuItemService = menuItemService;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<MenuItemResponseDTO> createMenuItem(@Valid @RequestBody MenuItemRequestDTO dto) {
        var menuItem = mapper.toDomain(dto);
        var saved = menuItemService.createMenuItem(
                menuItem.getName(),
                menuItem.getDescription(),
                menuItem.getPrice(),
                menuItem.isOnlyInRestaurant(),
                menuItem.getPhotoPath(),
                menuItem.getRestaurantId()
        );
        return ResponseEntity.ok(mapper.toResponseDTO(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MenuItemResponseDTO> updateMenuItem(
            @PathVariable Long id,
            @Valid @RequestBody MenuItemRequestDTO dto
    ) {
        var menuItem = mapper.toDomain(id, dto);
        var updated = menuItemService.updateMenuItem(
                MenuItemId.of(id),
                menuItem.getName(),
                menuItem.getDescription(),
                menuItem.getPrice(),
                menuItem.isOnlyInRestaurant(),
                menuItem.getPhotoPath()
        );
        return ResponseEntity.ok(mapper.toResponseDTO(updated));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MenuItemResponseDTO> getMenuItemById(@PathVariable Long id) {
        var menuItem = menuItemService.getMenuItemById(MenuItemId.of(id));
        return ResponseEntity.ok(mapper.toResponseDTO(menuItem));
    }

    @GetMapping("/menus-items/restaurant/{restaurantId}")
    public ResponseEntity<List<MenuItemResponseDTO>> getAllMenuItemsByRestaurant(@PathVariable Long restaurantId) {
        List<MenuItemResponseDTO> list = menuItemService.getAllMenuItemsByRestaurant(RestaurantId.of(restaurantId))
                .stream()
                .map(mapper::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMenuItem(@PathVariable Long id) {
        menuItemService.deleteMenuItem(MenuItemId.of(id));
        return ResponseEntity.noContent().build();
    }

}
