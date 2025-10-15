package tech.challenge.establishment.manager.domain.usecases.menuItem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tech.challenge.establishment.manager.domain.entities.MenuItem;
import tech.challenge.establishment.manager.domain.entities.Restaurant;
import tech.challenge.establishment.manager.domain.entities.RestaurantAddress;
import tech.challenge.establishment.manager.domain.exceptions.AccessDeniedException;
import tech.challenge.establishment.manager.domain.exceptions.MenuItemAlreadyExistsException;
import tech.challenge.establishment.manager.domain.exceptions.MenuItemNotFoundException;
import tech.challenge.establishment.manager.domain.repositories.MenuItemRepository;
import tech.challenge.establishment.manager.domain.repositories.RestaurantRepository;
import tech.challenge.establishment.manager.domain.valueobjects.*;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateMenuItemUseCaseTest {

    @Mock
    private MenuItemRepository menuItemRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    private UpdateMenuItemUseCase useCase;

    private MenuItem existingMenuItem;
    private Restaurant restaurant;
    private RestaurantAddress restaurantAddress;

    @BeforeEach
    void setUp() {
        useCase = new UpdateMenuItemUseCase(menuItemRepository, restaurantRepository);
        
        restaurantAddress = RestaurantAddress.create(
                "Restaurant Street",
                "Restaurant City",
                "12345678",
                "100",
                RestaurantId.of(1L)
        );
        
        restaurant = Restaurant.create(
                "Test Restaurant",
                restaurantAddress,
                KitchenType.of("Italian"),
                LocalTime.of(9, 0),
                LocalTime.of(22, 0),
                UserId.of(1L) // owner
        );
        restaurant = Restaurant.of(
                1L,
                restaurant.getName().value(),
                restaurant.getRestaurantAddress(),
                restaurant.getKitchenType(),
                restaurant.getStartOperation(),
                restaurant.getEndOperation(),
                restaurant.getOwnerId()
        );

        existingMenuItem = MenuItem.of(
                1L,
                "Original Pizza",
                "Original description",
                20.0,
                "http://example.com/original.jpg",
                RestaurantId.of(1L)
        );
    }

    @Test
    void shouldUpdateMenuItemSuccessfully() {
        // Given
        MenuItemId menuItemId = MenuItemId.of(1L);
        UserId ownerId = UserId.of(1L);
        
        MenuItem updatedMenuItem = MenuItem.of(
                1L,
                "Updated Pizza",
                "Updated description",
                25.0,
                "http://example.com/updated.jpg",
                RestaurantId.of(1L)
        );

        when(menuItemRepository.findById(menuItemId)).thenReturn(Optional.of(existingMenuItem));
        when(restaurantRepository.findById(RestaurantId.of(1L))).thenReturn(Optional.of(restaurant));
        when(menuItemRepository.existsByNameAndRestaurant("Updated Pizza", RestaurantId.of(1L))).thenReturn(false);
        when(menuItemRepository.save(any(MenuItem.class))).thenReturn(updatedMenuItem);

        // When
        MenuItem result = useCase.execute(
                menuItemId,
                "Updated Pizza",
                "Updated description",
                25.0,
                "http://example.com/updated.jpg",
                RestaurantId.of(1L),
                ownerId,
                false
        );

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getName().value()).isEqualTo("Updated Pizza");
        assertThat(result.getDescription()).isEqualTo("Updated description");
        assertThat(result.getPrice()).isEqualTo(25.0);
        assertThat(result.getPhotoUrl()).isEqualTo("http://example.com/updated.jpg");
        verify(menuItemRepository).save(any(MenuItem.class));
    }

    @Test
    void shouldUpdateMenuItemAsAdmin() {
        // Given
        MenuItemId menuItemId = MenuItemId.of(1L);
        UserId adminId = UserId.of(999L); // Different from owner
        
        MenuItem updatedMenuItem = MenuItem.of(
                1L,
                "Admin Updated Pizza",
                "Admin updated description",
                30.0,
                "http://example.com/admin.jpg",
                RestaurantId.of(1L)
        );

        when(menuItemRepository.findById(menuItemId)).thenReturn(Optional.of(existingMenuItem));
        when(menuItemRepository.existsByNameAndRestaurant("Admin Updated Pizza", RestaurantId.of(1L))).thenReturn(false);
        when(menuItemRepository.save(any(MenuItem.class))).thenReturn(updatedMenuItem);

        // When
        MenuItem result = useCase.execute(
                menuItemId,
                "Admin Updated Pizza",
                "Admin updated description",
                30.0,
                "http://example.com/admin.jpg",
                RestaurantId.of(1L),
                adminId,
                true // isAdmin = true
        );

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getName().value()).isEqualTo("Admin Updated Pizza");
        verify(restaurantRepository, never()).findById(any()); // Admin doesn't need ownership check
        verify(menuItemRepository).save(any(MenuItem.class));
    }

    @Test
    void shouldUpdateMenuItemWithPartialFields() {
        // Given
        MenuItemId menuItemId = MenuItemId.of(1L);
        UserId ownerId = UserId.of(1L);
        
        MenuItem updatedMenuItem = MenuItem.of(
                1L,
                "Updated Pizza", // only name updated
                "Original description", // description unchanged
                20.0, // price unchanged
                "http://example.com/original.jpg", // photo unchanged
                RestaurantId.of(1L)
        );

        when(menuItemRepository.findById(menuItemId)).thenReturn(Optional.of(existingMenuItem));
        when(restaurantRepository.findById(RestaurantId.of(1L))).thenReturn(Optional.of(restaurant));
        when(menuItemRepository.existsByNameAndRestaurant("Updated Pizza", RestaurantId.of(1L))).thenReturn(false);
        when(menuItemRepository.save(any(MenuItem.class))).thenReturn(updatedMenuItem);

        // When - only updating name
        MenuItem result = useCase.execute(
                menuItemId,
                "Updated Pizza", // only this field
                null, // description unchanged
                null, // price unchanged
                null, // photo unchanged
                null, // restaurant unchanged
                ownerId,
                false
        );

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getName().value()).isEqualTo("Updated Pizza");
        assertThat(result.getDescription()).isEqualTo("Original description");
        assertThat(result.getPrice()).isEqualTo(20.0);
        verify(menuItemRepository).save(any(MenuItem.class));
    }

    @Test
    void shouldThrowExceptionWhenMenuItemNotFound() {
        // Given
        MenuItemId menuItemId = MenuItemId.of(999L);
        when(menuItemRepository.findById(menuItemId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> useCase.execute(
                menuItemId,
                "Updated Pizza",
                "Updated description",
                25.0,
                "http://example.com/updated.jpg",
                RestaurantId.of(1L),
                UserId.of(1L),
                false
        )).isInstanceOf(MenuItemNotFoundException.class);
    }

    @Test
    void shouldThrowAccessDeniedExceptionWhenNotOwnerAndNotAdmin() {
        // Given
        MenuItemId menuItemId = MenuItemId.of(1L);
        UserId nonOwnerId = UserId.of(999L); // Different from owner

        when(menuItemRepository.findById(menuItemId)).thenReturn(Optional.of(existingMenuItem));
        when(restaurantRepository.findById(RestaurantId.of(1L))).thenReturn(Optional.of(restaurant));

        // When & Then
        assertThatThrownBy(() -> useCase.execute(
                menuItemId,
                "Updated Pizza",
                "Updated description",
                25.0,
                "http://example.com/updated.jpg",
                RestaurantId.of(1L),
                nonOwnerId,
                false // not admin
        )).isInstanceOf(AccessDeniedException.class)
          .hasMessageContaining("Access denied: You can only update menu items from your own restaurant");
    }

    @Test
    void shouldThrowExceptionWhenRestaurantNotFound() {
        // Given
        MenuItemId menuItemId = MenuItemId.of(1L);
        UserId ownerId = UserId.of(1L);

        when(menuItemRepository.findById(menuItemId)).thenReturn(Optional.of(existingMenuItem));
        when(restaurantRepository.findById(RestaurantId.of(1L))).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> useCase.execute(
                menuItemId,
                "Updated Pizza",
                "Updated description",
                25.0,
                "http://example.com/updated.jpg",
                RestaurantId.of(1L),
                ownerId,
                false
        )).isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("Restaurant not found: 1");
    }

    @Test
    void shouldThrowExceptionWhenMenuItemNameAlreadyExists() {
        // Given
        MenuItemId menuItemId = MenuItemId.of(1L);
        UserId ownerId = UserId.of(1L);
        
        MenuItem conflictingMenuItem = MenuItem.of(
                2L, // Different ID
                "Existing Pizza",
                "Some description",
                15.0,
                "http://example.com/existing.jpg",
                RestaurantId.of(1L)
        );

        when(menuItemRepository.findById(menuItemId)).thenReturn(Optional.of(existingMenuItem));
        when(restaurantRepository.findById(RestaurantId.of(1L))).thenReturn(Optional.of(restaurant));
        when(menuItemRepository.existsByNameAndRestaurant("Existing Pizza", RestaurantId.of(1L))).thenReturn(true);
        when(menuItemRepository.findAllByRestaurant(RestaurantId.of(1L))).thenReturn(List.of(existingMenuItem, conflictingMenuItem));

        // When & Then
        assertThatThrownBy(() -> useCase.execute(
                menuItemId,
                "Existing Pizza", // This name already exists
                "Updated description",
                25.0,
                "http://example.com/updated.jpg",
                RestaurantId.of(1L),
                ownerId,
                false
        )).isInstanceOf(MenuItemAlreadyExistsException.class);
    }

    @Test
    void shouldAllowUpdatingToSameNameForSameMenuItem() {
        // Given
        MenuItemId menuItemId = MenuItemId.of(1L);
        UserId ownerId = UserId.of(1L);
        
        MenuItem updatedMenuItem = MenuItem.of(
                1L,
                "Original Pizza", // Same name as existing
                "Updated description",
                25.0,
                "http://example.com/updated.jpg",
                RestaurantId.of(1L)
        );

        when(menuItemRepository.findById(menuItemId)).thenReturn(Optional.of(existingMenuItem));
        when(restaurantRepository.findById(RestaurantId.of(1L))).thenReturn(Optional.of(restaurant));
        when(menuItemRepository.existsByNameAndRestaurant("Original Pizza", RestaurantId.of(1L))).thenReturn(true);
        when(menuItemRepository.findAllByRestaurant(RestaurantId.of(1L))).thenReturn(List.of(existingMenuItem));
        when(menuItemRepository.save(any(MenuItem.class))).thenReturn(updatedMenuItem);

        // When
        MenuItem result = useCase.execute(
                menuItemId,
                "Original Pizza", // Same name
                "Updated description",
                25.0,
                "http://example.com/updated.jpg",
                RestaurantId.of(1L),
                ownerId,
                false
        );

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getName().value()).isEqualTo("Original Pizza");
        assertThat(result.getDescription()).isEqualTo("Updated description");
        verify(menuItemRepository).save(any(MenuItem.class));
    }
}