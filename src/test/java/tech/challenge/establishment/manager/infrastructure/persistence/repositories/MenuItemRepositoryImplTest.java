package tech.challenge.establishment.manager.infrastructure.persistence.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tech.challenge.establishment.manager.domain.entities.MenuItem;
import tech.challenge.establishment.manager.domain.valueobjects.MenuItemId;
import tech.challenge.establishment.manager.domain.valueobjects.RestaurantId;
import tech.challenge.establishment.manager.infrastructure.persistence.entities.MenuItemJpaEntity;
import tech.challenge.establishment.manager.infrastructure.persistence.entities.RestaurantJpaEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MenuItemRepositoryImplTest {

    private MenuItemRepositoryImpl repository;

    @Mock
    private MenuItemJpaRepository menuItemJpaRepository;

    @BeforeEach
    void setUp() {
        repository = new MenuItemRepositoryImpl(menuItemJpaRepository);
    }

    @Test
    void shouldSaveNewMenuItemSuccessfully() {
        // Given
        MenuItem menuItem = MenuItem.create( // usar create para novo item
                "New Pizza",
                "Delicious new pizza",
                25.99,
                "https://example.com/pizza.jpg",
                RestaurantId.of(1L)
        );

        RestaurantJpaEntity restaurantEntity = new RestaurantJpaEntity();
        restaurantEntity.setId(1L);

        MenuItemJpaEntity savedEntity = new MenuItemJpaEntity();
        savedEntity.setId(1L);
        savedEntity.setName("New Pizza");
        savedEntity.setDescription("Delicious new pizza");
        savedEntity.setPrice(25.99);
        savedEntity.setPhotoUrl("https://example.com/pizza.jpg");
        savedEntity.setRestaurant(restaurantEntity);

        when(menuItemJpaRepository.save(any(MenuItemJpaEntity.class))).thenReturn(savedEntity);

        // When
        MenuItem result = repository.save(menuItem);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId().value()).isEqualTo(1L);
        assertThat(result.getName().value()).isEqualTo("New Pizza");
        assertThat(result.getDescription()).isEqualTo("Delicious new pizza");
        assertThat(result.getPrice()).isEqualTo(25.99);
        verify(menuItemJpaRepository).save(any(MenuItemJpaEntity.class));
    }

    @Test
    void shouldSaveExistingMenuItemSuccessfully() {
        // Given
        MenuItem menuItem = MenuItem.of(
                1L, // item existente
                "Updated Pizza",
                "Updated description",
                30.99,
                "https://example.com/updated.jpg",
                RestaurantId.of(1L)
        );

        MenuItemJpaEntity existingEntity = new MenuItemJpaEntity();
        existingEntity.setId(1L);
        existingEntity.setName("Old Pizza");

        RestaurantJpaEntity restaurantEntity = new RestaurantJpaEntity();
        restaurantEntity.setId(1L);

        MenuItemJpaEntity savedEntity = new MenuItemJpaEntity();
        savedEntity.setId(1L);
        savedEntity.setName("Updated Pizza");
        savedEntity.setDescription("Updated description");
        savedEntity.setPrice(30.99);
        savedEntity.setPhotoUrl("https://example.com/updated.jpg");
        savedEntity.setRestaurant(restaurantEntity);

        when(menuItemJpaRepository.findById(1L)).thenReturn(Optional.of(existingEntity));
        when(menuItemJpaRepository.save(any(MenuItemJpaEntity.class))).thenReturn(savedEntity);

        // When
        MenuItem result = repository.save(menuItem);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId().value()).isEqualTo(1L);
        assertThat(result.getName().value()).isEqualTo("Updated Pizza");
        assertThat(result.getDescription()).isEqualTo("Updated description");
        verify(menuItemJpaRepository).findById(1L);
        verify(menuItemJpaRepository).save(any(MenuItemJpaEntity.class));
    }

    @Test
    void shouldFindByIdSuccessfully() {
        // Given
        Long id = 1L;
        MenuItemId menuItemId = MenuItemId.of(id);

        RestaurantJpaEntity restaurantEntity = new RestaurantJpaEntity();
        restaurantEntity.setId(1L);

        MenuItemJpaEntity jpaEntity = new MenuItemJpaEntity();
        jpaEntity.setId(id);
        jpaEntity.setName("Test Pizza");
        jpaEntity.setDescription("Test description");
        jpaEntity.setPrice(20.50);
        jpaEntity.setPhotoUrl("https://example.com/test.jpg");
        jpaEntity.setRestaurant(restaurantEntity);

        when(menuItemJpaRepository.findById(id)).thenReturn(Optional.of(jpaEntity));

        // When
        Optional<MenuItem> result = repository.findById(menuItemId);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getId().value()).isEqualTo(id);
        assertThat(result.get().getName().value()).isEqualTo("Test Pizza");
        assertThat(result.get().getPrice()).isEqualTo(20.50);
        verify(menuItemJpaRepository).findById(id);
    }

    @Test
    void shouldReturnEmptyWhenNotFoundById() {
        // Given
        Long id = 999L;
        MenuItemId menuItemId = MenuItemId.of(id);

        when(menuItemJpaRepository.findById(id)).thenReturn(Optional.empty());

        // When
        Optional<MenuItem> result = repository.findById(menuItemId);

        // Then
        assertThat(result).isEmpty();
        verify(menuItemJpaRepository).findById(id);
    }

    @Test
    void shouldFindAllByRestaurantSuccessfully() {
        // Given
        RestaurantId restaurantId = RestaurantId.of(1L);

        RestaurantJpaEntity restaurantEntity = new RestaurantJpaEntity();
        restaurantEntity.setId(1L);

        MenuItemJpaEntity item1 = new MenuItemJpaEntity();
        item1.setId(1L);
        item1.setName("Pizza Margherita");
        item1.setDescription("Classic pizza");
        item1.setPrice(18.99);
        item1.setPhotoUrl("https://example.com/margherita.jpg");
        item1.setRestaurant(restaurantEntity);

        MenuItemJpaEntity item2 = new MenuItemJpaEntity();
        item2.setId(2L);
        item2.setName("Pizza Pepperoni");
        item2.setDescription("Pepperoni pizza");
        item2.setPrice(22.99);
        item2.setPhotoUrl("https://example.com/pepperoni.jpg");
        item2.setRestaurant(restaurantEntity);

        when(menuItemJpaRepository.findAllByRestaurant_Id(1L)).thenReturn(Arrays.asList(item1, item2));

        // When
        List<MenuItem> result = repository.findAllByRestaurant(restaurantId);

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName().value()).isEqualTo("Pizza Margherita");
        assertThat(result.get(1).getName().value()).isEqualTo("Pizza Pepperoni");
        verify(menuItemJpaRepository).findAllByRestaurant_Id(1L);
    }

    @Test
    void shouldReturnEmptyListWhenNoItemsFoundForRestaurant() {
        // Given
        RestaurantId restaurantId = RestaurantId.of(999L);

        when(menuItemJpaRepository.findAllByRestaurant_Id(999L)).thenReturn(Arrays.asList());

        // When
        List<MenuItem> result = repository.findAllByRestaurant(restaurantId);

        // Then
        assertThat(result).isEmpty();
        verify(menuItemJpaRepository).findAllByRestaurant_Id(999L);
    }

    @Test
    void shouldReturnTrueWhenItemExistsByNameAndRestaurant() {
        // Given
        String name = "Pizza Margherita";
        RestaurantId restaurantId = RestaurantId.of(1L);

        when(menuItemJpaRepository.existsByNameAndRestaurant_Id(name, 1L)).thenReturn(true);

        // When
        boolean result = repository.existsByNameAndRestaurant(name, restaurantId);

        // Then
        assertThat(result).isTrue();
        verify(menuItemJpaRepository).existsByNameAndRestaurant_Id(name, 1L);
    }

    @Test
    void shouldReturnFalseWhenItemDoesNotExistByNameAndRestaurant() {
        // Given
        String name = "Nonexistent Pizza";
        RestaurantId restaurantId = RestaurantId.of(1L);

        when(menuItemJpaRepository.existsByNameAndRestaurant_Id(name, 1L)).thenReturn(false);

        // When
        boolean result = repository.existsByNameAndRestaurant(name, restaurantId);

        // Then
        assertThat(result).isFalse();
        verify(menuItemJpaRepository).existsByNameAndRestaurant_Id(name, 1L);
    }

    @Test
    void shouldDeleteByIdSuccessfully() {
        // Given
        MenuItemId menuItemId = MenuItemId.of(1L);

        // When
        repository.deleteById(menuItemId);

        // Then
        verify(menuItemJpaRepository).deleteById(1L);
    }
}
