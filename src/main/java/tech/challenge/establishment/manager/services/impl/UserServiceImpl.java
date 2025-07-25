package tech.challenge.establishment.manager.services.impl;

import tech.challenge.establishment.manager.dtos.user.ChangePasswordDTO;
import tech.challenge.establishment.manager.dtos.user.CreateUserDTO;
import tech.challenge.establishment.manager.dtos.user.UpdateUserDTO;
import tech.challenge.establishment.manager.entities.Address;
import tech.challenge.establishment.manager.entities.Role;
import tech.challenge.establishment.manager.entities.RoleName;
import tech.challenge.establishment.manager.entities.User;
import tech.challenge.establishment.manager.repositories.RoleRepository;
import tech.challenge.establishment.manager.repositories.UserRepository;
import tech.challenge.establishment.manager.services.AddressService;
import tech.challenge.establishment.manager.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AddressService addressService;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public UserServiceImpl(UserRepository userRepository, AddressService addressService,
                           PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.addressService = addressService;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
    }

    @Override
    public User save(CreateUserDTO dto) {
        User user = new User();
        user.setName(dto.name());
        user.setEmail(dto.email());
        user.setLogin(dto.login());
        user.setPassword(passwordEncoder.encode(dto.password()));

        Role userRole = roleRepository.findByName(RoleName.USER)
                .orElseThrow(() -> new RuntimeException("Role USER not found in the database"));

        user.getRoles().add(userRole);

        user = userRepository.save(user);

        Address address = addressService.create(dto.address(), user);
        user.setAddress(address);

        return userRepository.save(user);
    }

    @Override
    public User update(Long id, UpdateUserDTO dto) {
        User user = findById(id);
        user.setName(dto.name());
        user.setEmail(dto.email());
        user.setLogin(dto.login());
        user.setPassword(passwordEncoder.encode(dto.password()));
        return userRepository.save(user);
    }

    @Override
    public void delete(Long id) {
        User user = findById(id);
        userRepository.delete(user);
    }

    @Override
    public void changePassword(Long userId, ChangePasswordDTO dto) {
        if (!dto.newPassword().equals(dto.newPasswordConfirmation())) {
            throw new IllegalArgumentException("Nova senha e confirmação não coincidem.");
        }

        User user = findById(userId);

        if (!passwordEncoder.matches(dto.currentPassword(), user.getPassword())) {
            throw new IllegalStateException("Senha atual incorreta.");
        }

        user.setPassword(passwordEncoder.encode(dto.newPassword()));
        userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByLogin(username).orElseThrow(() -> new RuntimeException("O usuário não foi encontrado!"));
    }
}
