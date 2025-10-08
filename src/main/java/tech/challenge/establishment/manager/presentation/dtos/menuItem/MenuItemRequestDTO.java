package tech.challenge.establishment.manager.presentation.dtos.menuItem;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record MenuItemRequestDTO(
        @NotBlank(message = "Name is required")
        @Size(max = 100, message = "Name must have at most 100 characters")
        String name,

        @NotBlank(message = "Description is required")
        @Size(max = 255, message = "Description must have at most 255 characters")
        String description,

        @NotNull(message = "Price is required")
        double price,

        @NotNull(message = "Only in restaurant flag is required")
        boolean onlyInRestaurant,

        @NotBlank(message = "Photo path is required")
        String photoPath,

        @NotNull(message = "Restaurant ID is required")
        Long restaurantId
) {}
