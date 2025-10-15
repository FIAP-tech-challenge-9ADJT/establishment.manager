package tech.challenge.establishment.manager.domain.entities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoleTest {

    @Test
    void shouldCreateRoleSuccessfully() {
        Role.RoleName roleName = Role.RoleName.USER;
        
        Role role = Role.create(roleName);
        
        assertNotNull(role);
        assertEquals(roleName, role.getName());
        assertNull(role.getId());
    }

    @Test
    void shouldCreateRoleWithIdSuccessfully() {
        Long id = 1L;
        Role.RoleName roleName = Role.RoleName.ADMIN;
        
        Role role = Role.of(id, roleName);
        
        assertNotNull(role);
        assertEquals(id, role.getId());
        assertEquals(roleName, role.getName());
    }

    @Test
    void shouldThrowExceptionForNullRoleName() {
        assertThrows(NullPointerException.class, () -> Role.create(null));
        assertThrows(NullPointerException.class, () -> Role.of(1L, null));
    }

    @Test
    void shouldReturnCorrectAuthority() {
        Role userRole = Role.create(Role.RoleName.USER);
        Role adminRole = Role.create(Role.RoleName.ADMIN);
        Role ownerRole = Role.create(Role.RoleName.RESTAURANT_OWNER);

        assertEquals("ROLE_USER", userRole.getAuthority());
        assertEquals("ROLE_ADMIN", adminRole.getAuthority());
        assertEquals("ROLE_RESTAURANT_OWNER", ownerRole.getAuthority());
    }

    @Test
    void shouldBeEqualWhenSameRoleName() {
        Role role1 = Role.create(Role.RoleName.USER);
        Role role2 = Role.create(Role.RoleName.USER);
        
        assertEquals(role1, role2);
        assertEquals(role1.hashCode(), role2.hashCode());
    }

    @Test
    void shouldNotBeEqualWhenDifferentRoleName() {
        Role role1 = Role.create(Role.RoleName.USER);
        Role role2 = Role.create(Role.RoleName.ADMIN);
        
        assertNotEquals(role1, role2);
    }

    @Test
    void shouldHaveAllRequiredRoleNames() {
        Role.RoleName[] expectedRoles = {
            Role.RoleName.USER,
            Role.RoleName.RESTAURANT_OWNER,
            Role.RoleName.ADMIN
        };

        assertEquals(3, expectedRoles.length);
        assertNotNull(Role.RoleName.valueOf("USER"));
        assertNotNull(Role.RoleName.valueOf("RESTAURANT_OWNER"));
        assertNotNull(Role.RoleName.valueOf("ADMIN"));
    }

    @Test
    void shouldBeEqualToItself() {
        Role role = Role.create(Role.RoleName.USER);
        assertEquals(role, role);
    }

    @Test
    void shouldNotBeEqualToNull() {
        Role role = Role.create(Role.RoleName.USER);
        assertNotEquals(role, null);
    }

    @Test
    void shouldNotBeEqualToDifferentClass() {
        Role role = Role.create(Role.RoleName.USER);
        assertNotEquals(role, "String");
    }

    @Test
    void shouldHaveConsistentHashCode() {
        Role role1 = Role.create(Role.RoleName.ADMIN);
        Role role2 = Role.create(Role.RoleName.ADMIN);
        
        assertEquals(role1.hashCode(), role2.hashCode());
    }
}
