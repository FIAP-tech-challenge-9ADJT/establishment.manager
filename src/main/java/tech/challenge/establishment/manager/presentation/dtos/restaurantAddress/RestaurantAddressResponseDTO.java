package tech.challenge.establishment.manager.presentation.dtos.restaurantAddress;

public record RestaurantAddressResponseDTO(
        Long id,
        String street,
        String city,
        String postalCode,
        String number
) {
}
