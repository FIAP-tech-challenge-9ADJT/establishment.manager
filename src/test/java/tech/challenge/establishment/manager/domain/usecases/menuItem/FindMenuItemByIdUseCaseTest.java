package tech.challenge.establishment.manager.domain.usecases.menuItem;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tech.challenge.establishment.manager.domain.entities.MenuItem;
import tech.challenge.establishment.manager.domain.exceptions.MenuItemNotFoundException;
import tech.challenge.establishment.manager.domain.repositories.MenuItemRepository;
import tech.challenge.establishment.manager.domain.valueobjects.MenuItemId;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FindMenuItemByIdUseCaseTest {

    @Mock
    private MenuItemRepository menuItemRepository;

    private FindMenuItemByIdUseCase findMenuItemByIdUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        findMenuItemByIdUseCase = new FindMenuItemByIdUseCase(menuItemRepository);
    }

    @Test
    void shouldFindMenuItemById() {
        MenuItemId menuItemId = MenuItemId.of(1L);
        MenuItem menuItem = mock(MenuItem.class);

        when(menuItemRepository.findById(menuItemId)).thenReturn(Optional.of(menuItem));

        MenuItem result = findMenuItemByIdUseCase.execute(menuItemId);

        assertNotNull(result);
        assertEquals(menuItem, result);
        verify(menuItemRepository).findById(menuItemId);
    }

    @Test
    void shouldThrowExceptionWhenMenuItemNotFound() {
        MenuItemId menuItemId = MenuItemId.of(1L);

        when(menuItemRepository.findById(menuItemId)).thenReturn(Optional.empty());

        assertThrows(MenuItemNotFoundException.class, () -> {
            findMenuItemByIdUseCase.execute(menuItemId);
        });

        verify(menuItemRepository).findById(menuItemId);
    }

    @Test
    void shouldFindDifferentMenuItems() {
        MenuItemId id1 = MenuItemId.of(1L);
        MenuItemId id2 = MenuItemId.of(2L);
        MenuItem menuItem1 = mock(MenuItem.class);
        MenuItem menuItem2 = mock(MenuItem.class);

        when(menuItemRepository.findById(id1)).thenReturn(Optional.of(menuItem1));
        when(menuItemRepository.findById(id2)).thenReturn(Optional.of(menuItem2));

        MenuItem result1 = findMenuItemByIdUseCase.execute(id1);
        MenuItem result2 = findMenuItemByIdUseCase.execute(id2);

        assertNotNull(result1);
        assertNotNull(result2);
        assertNotEquals(result1, result2);
    }
}
