package tech.challenge.establishment.manager.presentation.mappers;

import org.junit.jupiter.api.Test;
import tech.challenge.establishment.manager.domain.entities.MenuItem;
import tech.challenge.establishment.manager.domain.valueobjects.MenuItemId;
import tech.challenge.establishment.manager.domain.valueobjects.RestaurantId;
import tech.challenge.establishment.manager.presentation.dtos.menuItem.CreateMenuItemDTO;
import tech.challenge.establishment.manager.presentation.dtos.menuItem.MenuItemResponseDTO;
import tech.challenge.establishment.manager.presentation.dtos.menuItem.UpdateMenuItemDTO;

import static org.assertj.core.api.Assertions.assertThat;

class MenuItemDtoMapperTest {

    @Test
    void shouldMapFromCreateDto() {
        // Given
        CreateMenuItemDTO dto = new CreateMenuItemDTO(
                "Pizza Margherita",
                "Delicious pizza with tomato and mozzarella",
                25.99,
                "https://example.com/pizza.jpg",
                1L
        );

        // When
        MenuItem result = MenuItemDtoMapper.fromCreateDto(dto);

        // Then
        assertThat(result.getName().value()).isEqualTo("Pizza Margherita");
        assertThat(result.getDescription()).isEqualTo("Delicious pizza with tomato and mozzarella");
        assertThat(result.getPrice()).isEqualTo(25.99);
        assertThat(result.getPhotoUrl()).isEqualTo("https://example.com/pizza.jpg");
        assertThat(result.getRestaurantId().value()).isEqualTo(1L);
    }

    @Test
    void shouldMapFromUpdateDto() {
        // Given
        MenuItem existingMenuItem = MenuItem.of(
                1L,
                "Old Pizza",
                "Old description",
                20.00,
                "https://example.com/old.jpg",
                RestaurantId.of(1L)
        );

        UpdateMenuItemDTO dto = new UpdateMenuItemDTO(
                "New Pizza",
                "New description",
                30.00,
                "https://example.com/new.jpg",
                1L // restaurantId é obrigatório
        );

        // When
        MenuItem result = MenuItemDtoMapper.fromUpdateDto(dto, existingMenuItem);

        // Then
        assertThat(result.getId().value()).isEqualTo(1L);
        assertThat(result.getName().value()).isEqualTo("New Pizza");
        assertThat(result.getDescription()).isEqualTo("New description");
        assertThat(result.getPrice()).isEqualTo(30.00);
        assertThat(result.getPhotoUrl()).isEqualTo("https://example.com/new.jpg");
        assertThat(result.getRestaurantId()).isEqualTo(existingMenuItem.getRestaurantId());
    }

    @Test
    void shouldMapFromUpdateDtoWithExistingId() {
        // Given
        MenuItem existingMenuItem = MenuItem.of(
                2L, // ID válido
                "Old Pizza",
                "Old description",
                20.00,
                "https://example.com/old.jpg",
                RestaurantId.of(1L)
        );

        UpdateMenuItemDTO dto = new UpdateMenuItemDTO(
                "New Pizza",
                "New description",
                30.00,
                "https://example.com/new.jpg",
                1L // restaurantId é obrigatório
        );

        // When
        MenuItem result = MenuItemDtoMapper.fromUpdateDto(dto, existingMenuItem);

        // Then
        assertThat(result.getId().value()).isEqualTo(2L);
        assertThat(result.getName().value()).isEqualTo("New Pizza");
        assertThat(result.getDescription()).isEqualTo("New description");
        assertThat(result.getPrice()).isEqualTo(30.00);
        assertThat(result.getPhotoUrl()).isEqualTo("https://example.com/new.jpg");
        assertThat(result.getRestaurantId()).isEqualTo(existingMenuItem.getRestaurantId());
    }

    @Test
    void shouldReturnNullWhenMenuItemIsNull() {
        // Given
        MenuItem menuItem = null;

        // When
        MenuItemResponseDTO result = MenuItemDtoMapper.toResponseDto(menuItem);

        // Then
        assertThat(result).isNull();
    }

    @Test
    void shouldMapToResponseDto() {
        // Given
        MenuItem menuItem = MenuItem.of(
                1L,
                "Hamburger",
                "Tasty hamburger with beef",
                15.50,
                "https://example.com/burger.jpg",
                RestaurantId.of(2L)
        );

        // When
        MenuItemResponseDTO result = MenuItemDtoMapper.toResponseDto(menuItem);

        // Then
        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.name()).isEqualTo("Hamburger");
        assertThat(result.description()).isEqualTo("Tasty hamburger with beef");
        assertThat(result.price()).isEqualTo(15.50);
        assertThat(result.photoUrl()).isEqualTo("https://example.com/burger.jpg");
        assertThat(result.restaurantId()).isEqualTo(2L);
    }

    @Test
    void shouldMapToResponseDtoWithValidId() {
        // Given
        MenuItem menuItem = MenuItem.of(
                2L, // ID válido
                "Hamburger",
                "Tasty hamburger with beef",
                15.50,
                "https://example.com/burger.jpg",
                RestaurantId.of(2L)
        );

        // When
        MenuItemResponseDTO result = MenuItemDtoMapper.toResponseDto(menuItem);

        // Then
        assertThat(result.id()).isEqualTo(2L);
        assertThat(result.name()).isEqualTo("Hamburger");
        assertThat(result.description()).isEqualTo("Tasty hamburger with beef");
        assertThat(result.price()).isEqualTo(15.50);
        assertThat(result.photoUrl()).isEqualTo("https://example.com/burger.jpg");
        assertThat(result.restaurantId()).isEqualTo(2L);
    }

    @Test
    void shouldMapToResponseDtoWithValidRestaurantId() {
        // Given
        MenuItem menuItem = MenuItem.of(
                1L,
                "Hamburger",
                "Tasty hamburger with beef",
                15.50,
                "https://example.com/burger.jpg",
                RestaurantId.of(3L) // RestaurantId válido
        );

        // When
        MenuItemResponseDTO result = MenuItemDtoMapper.toResponseDto(menuItem);

        // Then
        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.name()).isEqualTo("Hamburger");
        assertThat(result.description()).isEqualTo("Tasty hamburger with beef");
        assertThat(result.price()).isEqualTo(15.50);
        assertThat(result.photoUrl()).isEqualTo("https://example.com/burger.jpg");
        assertThat(result.restaurantId()).isEqualTo(3L);
    }
}
