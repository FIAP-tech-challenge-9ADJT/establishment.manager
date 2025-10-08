package tech.challenge.establishment.manager.presentation.dtos.menuItem;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateMenuItemDTO(
        @NotBlank String name,
        @NotBlank String description,
        @NotNull Double price,
        @NotBlank String photoUrl,
        @NotNull Long restaurantId
) {}
