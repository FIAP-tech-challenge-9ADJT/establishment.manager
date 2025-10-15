package tech.challenge.establishment.manager.infrastructure.persistence.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tech.challenge.establishment.manager.domain.entities.Role;
import tech.challenge.establishment.manager.domain.entities.User;
import tech.challenge.establishment.manager.domain.valueobjects.Email;
import tech.challenge.establishment.manager.domain.valueobjects.Login;
import tech.challenge.establishment.manager.domain.valueobjects.UserId;
import tech.challenge.establishment.manager.infrastructure.persistence.entities.RoleJpaEntity;
import tech.challenge.establishment.manager.infrastructure.persistence.entities.UserJpaEntity;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserRepositoryImplTest {

    private UserRepositoryImpl repository;

    @Mock
    private UserJpaRepository userJpaRepository;

    @BeforeEach
    void setUp() {
        repository = new UserRepositoryImpl(userJpaRepository);
    }

    @Test
    void shouldFindByIdSuccessfully() {
        // Given
        Long id = 1L;
        UserId userId = UserId.of(id);
        
        RoleJpaEntity roleEntity = new RoleJpaEntity();
        roleEntity.setId(1L);
        roleEntity.setName(RoleJpaEntity.RoleName.USER);

        UserJpaEntity jpaEntity = new UserJpaEntity();
        jpaEntity.setId(id);
        jpaEntity.setName("Test User");
        jpaEntity.setEmail("test@example.com");
        jpaEntity.setLogin("testuser");
        jpaEntity.setPassword("password123");
        jpaEntity.setRoles(Set.of(roleEntity));
        jpaEntity.setCreatedAt(LocalDateTime.now());
        jpaEntity.setUpdatedAt(LocalDateTime.now());

        when(userJpaRepository.findById(id)).thenReturn(Optional.of(jpaEntity));

        // When
        Optional<User> result = repository.findById(userId);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getName().value()).isEqualTo("Test User");
        assertThat(result.get().getEmail().value()).isEqualTo("test@example.com");
        assertThat(result.get().getLogin().value()).isEqualTo("testuser");
        verify(userJpaRepository).findById(id);
    }

    @Test
    void shouldReturnEmptyWhenNotFoundById() {
        // Given
        Long id = 999L;
        UserId userId = UserId.of(id);
        
        when(userJpaRepository.findById(id)).thenReturn(Optional.empty());

        // When
        Optional<User> result = repository.findById(userId);

        // Then
        assertThat(result).isEmpty();
        verify(userJpaRepository).findById(id);
    }

    @Test
    void shouldFindByLoginSuccessfully() {
        // Given
        String loginValue = "testuser";
        Login login = Login.of(loginValue);
        
        RoleJpaEntity roleEntity = new RoleJpaEntity();
        roleEntity.setId(2L);
        roleEntity.setName(RoleJpaEntity.RoleName.ADMIN);

        UserJpaEntity jpaEntity = new UserJpaEntity();
        jpaEntity.setId(1L);
        jpaEntity.setName("Admin User");
        jpaEntity.setEmail("admin@example.com");
        jpaEntity.setLogin(loginValue);
        jpaEntity.setPassword("adminpass");
        jpaEntity.setRoles(Set.of(roleEntity));
        jpaEntity.setCreatedAt(LocalDateTime.now());
        jpaEntity.setUpdatedAt(LocalDateTime.now());

        when(userJpaRepository.findByLogin(loginValue)).thenReturn(Optional.of(jpaEntity));

        // When
        Optional<User> result = repository.findByLogin(login);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getName().value()).isEqualTo("Admin User");
        assertThat(result.get().getLogin().value()).isEqualTo(loginValue);
        verify(userJpaRepository).findByLogin(loginValue);
    }

    @Test
    void shouldReturnEmptyWhenNotFoundByLogin() {
        // Given
        String loginValue = "nonexistent";
        Login login = Login.of(loginValue);
        
        when(userJpaRepository.findByLogin(loginValue)).thenReturn(Optional.empty());

        // When
        Optional<User> result = repository.findByLogin(login);

        // Then
        assertThat(result).isEmpty();
        verify(userJpaRepository).findByLogin(loginValue);
    }

    @Test
    void shouldSaveUserSuccessfully() {
        // Given
        User user = User.of(
                1L,
                "Save User",
                "save@example.com",
                "saveuser",
                "savepass",
                null,
                Set.of(Role.of(1L, Role.RoleName.USER)),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        RoleJpaEntity roleEntity = new RoleJpaEntity();
        roleEntity.setId(1L);
        roleEntity.setName(RoleJpaEntity.RoleName.USER);

        UserJpaEntity savedEntity = new UserJpaEntity();
        savedEntity.setId(1L);
        savedEntity.setName("Save User");
        savedEntity.setEmail("save@example.com");
        savedEntity.setLogin("saveuser");
        savedEntity.setPassword("savepass");
        savedEntity.setRoles(Set.of(roleEntity));
        savedEntity.setCreatedAt(LocalDateTime.now());
        savedEntity.setUpdatedAt(LocalDateTime.now());

        when(userJpaRepository.save(any(UserJpaEntity.class))).thenReturn(savedEntity);

        // When
        User result = repository.save(user);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getName().value()).isEqualTo("Save User");
        assertThat(result.getEmail().value()).isEqualTo("save@example.com");
        verify(userJpaRepository).save(any(UserJpaEntity.class));
    }

    @Test
    void shouldDeleteByIdSuccessfully() {
        // Given
        Long id = 1L;
        UserId userId = UserId.of(id);

        // When
        repository.delete(userId);

        // Then
        verify(userJpaRepository).deleteById(id);
    }

    @Test
    void shouldReturnTrueWhenEmailExists() {
        // Given
        String emailValue = "existing@example.com";
        Email email = Email.of(emailValue);
        
        when(userJpaRepository.existsByEmail(emailValue)).thenReturn(true);

        // When
        boolean result = repository.existsByEmail(email);

        // Then
        assertThat(result).isTrue();
        verify(userJpaRepository).existsByEmail(emailValue);
    }

    @Test
    void shouldReturnFalseWhenEmailDoesNotExist() {
        // Given
        String emailValue = "nonexistent@example.com";
        Email email = Email.of(emailValue);
        
        when(userJpaRepository.existsByEmail(emailValue)).thenReturn(false);

        // When
        boolean result = repository.existsByEmail(email);

        // Then
        assertThat(result).isFalse();
        verify(userJpaRepository).existsByEmail(emailValue);
    }

    @Test
    void shouldReturnTrueWhenLoginExists() {
        // Given
        String loginValue = "existinguser";
        Login login = Login.of(loginValue);
        
        when(userJpaRepository.existsByLogin(loginValue)).thenReturn(true);

        // When
        boolean result = repository.existsByLogin(login);

        // Then
        assertThat(result).isTrue();
        verify(userJpaRepository).existsByLogin(loginValue);
    }

    @Test
    void shouldReturnFalseWhenLoginDoesNotExist() {
        // Given
        String loginValue = "nonexistentuser";
        Login login = Login.of(loginValue);
        
        when(userJpaRepository.existsByLogin(loginValue)).thenReturn(false);

        // When
        boolean result = repository.existsByLogin(login);

        // Then
        assertThat(result).isFalse();
        verify(userJpaRepository).existsByLogin(loginValue);
    }

    @Test
    void shouldReturnTrueWhenIdExists() {
        // Given
        Long id = 1L;
        UserId userId = UserId.of(id);
        
        when(userJpaRepository.existsById(id)).thenReturn(true);

        // When
        boolean result = repository.existsById(userId);

        // Then
        assertThat(result).isTrue();
        verify(userJpaRepository).existsById(id);
    }

    @Test
    void shouldReturnFalseWhenIdDoesNotExist() {
        // Given
        Long id = 999L;
        UserId userId = UserId.of(id);
        
        when(userJpaRepository.existsById(id)).thenReturn(false);

        // When
        boolean result = repository.existsById(userId);

        // Then
        assertThat(result).isFalse();
        verify(userJpaRepository).existsById(id);
    }
}

