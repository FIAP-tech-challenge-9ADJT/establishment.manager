package tech.challenge.establishment.manager.presentation.dtos.restaurant;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import tech.challenge.establishment.manager.presentation.dtos.restaurantAddress.UpdateRestaurantAddressDTO;

import java.time.LocalTime;

public record UpdateRestaurantDTO(
        @NotBlank(message = "Name is required")
        @Size(max = 100, message = "Name must have at most 100 characters")
        String name,

        @NotNull(message = "RestaurantAddress is required")
        @Valid UpdateRestaurantAddressDTO restaurantAddress,

        @NotNull(message = "KitchenType is required")
        String kitchenType,

        @NotNull(message = "Start operation are required")
        @JsonFormat(pattern = "HH:mm")
        LocalTime startOperation,

        @NotNull(message = "End operation are required")
        @JsonFormat(pattern = "HH:mm")
        LocalTime endOperation
) {}
