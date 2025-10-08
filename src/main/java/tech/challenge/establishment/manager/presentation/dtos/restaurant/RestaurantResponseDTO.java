package tech.challenge.establishment.manager.presentation.dtos.restaurant;

import com.fasterxml.jackson.annotation.JsonFormat;
import tech.challenge.establishment.manager.presentation.dtos.restaurantAddress.RestaurantAddressResponseDTO;

import java.time.LocalTime;

public record RestaurantResponseDTO(
        Long id,
        String name,
        RestaurantAddressResponseDTO restaurantAddress,
        String kitchenType,
        @JsonFormat(pattern = "HH:mm") LocalTime openingTime,
        @JsonFormat(pattern = "HH:mm") LocalTime closingTime,
        Long ownerId
) {}
