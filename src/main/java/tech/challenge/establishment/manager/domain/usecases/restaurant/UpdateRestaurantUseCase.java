package tech.challenge.establishment.manager.domain.usecases.restaurant;

import tech.challenge.establishment.manager.domain.entities.Restaurant;
import tech.challenge.establishment.manager.domain.entities.RestaurantAddress;
import tech.challenge.establishment.manager.domain.exceptions.RestaurantAlreadyExistsException;
import tech.challenge.establishment.manager.domain.exceptions.RestaurantNotFoundException;
import tech.challenge.establishment.manager.domain.repositories.RestaurantRepository;
import tech.challenge.establishment.manager.domain.valueobjects.KitchenType;
import tech.challenge.establishment.manager.domain.valueobjects.Name;
import tech.challenge.establishment.manager.domain.valueobjects.RestaurantId;

import java.time.LocalTime;

public class UpdateRestaurantUseCase {

    protected final RestaurantRepository restaurantRepository;

    public UpdateRestaurantUseCase(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }


    public Restaurant execute(RestaurantId restaurantId, String newName, RestaurantAddress newRestaurantAddress,
                              LocalTime newStartOperation, LocalTime newEndOperation, KitchenType newKitchenType) {

        Restaurant existing = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RestaurantNotFoundException(restaurantId));

        final RestaurantId existingId = existing.getId();
        Name finalName = newName != null ? Name.of(newName) : existing.getName();
        RestaurantAddress finalRestaurantAddress = newRestaurantAddress != null ? newRestaurantAddress : existing.getRestaurantAddress();
        LocalTime finalStartOperation = newStartOperation != null ? newStartOperation : existing.getStartOperation();
        LocalTime finalEndOperation = newEndOperation != null ? newEndOperation : existing.getEndOperation();

        restaurantRepository.findByNameAndRestaurantAddress(finalName, finalRestaurantAddress)
                .ifPresent(conflicting -> {
                    if (!conflicting.getId().equals(existingId)) {
                        throw new RestaurantAlreadyExistsException(finalName.value(), finalRestaurantAddress);
                    }
                });

        if (newName != null && !existing.getName().equals(finalName)) {
            existing = existing.updateName(finalName.value());
        }

        if (newRestaurantAddress != null && !existing.getRestaurantAddress().equals(finalRestaurantAddress)) {
            existing = existing.updateRestaurantAddress(finalRestaurantAddress);
        }

        if (!existing.getStartOperation().equals(finalStartOperation)
                || !existing.getEndOperation().equals(finalEndOperation)) {
            existing = existing.updateOperatingHours(finalStartOperation, finalEndOperation);
        }

        if (newKitchenType != null && !existing.getKitchenType().equals(newKitchenType)) {
            existing = existing.updateKitchenType(newKitchenType);
        }

        return restaurantRepository.save(existing);
    }
}
