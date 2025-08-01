package tech.challenge.establishment.manager.services.impl;

import tech.challenge.establishment.manager.dtos.user.CreateUserDTO;
import tech.challenge.establishment.manager.dtos.user.UpdateUserDTO;
import tech.challenge.establishment.manager.entities.Address;
import tech.challenge.establishment.manager.entities.Role;
import tech.challenge.establishment.manager.entities.RoleName;
import tech.challenge.establishment.manager.entities.User;
import tech.challenge.establishment.manager.repositories.RoleRepository;
import tech.challenge.establishment.manager.repositories.UserRepository;
import tech.challenge.establishment.manager.services.AddressService;
import tech.challenge.establishment.manager.services.AdminService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AddressService addressService;

    public AdminServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, AddressService addressService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.addressService = addressService;
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

        Role userRole = roleRepository.findByName(RoleName.ADMIN)
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
        return userRepository.save(user);
    }

    @Override
    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Recurso não encontrado");
        }
        try {
            userRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Falha de integridade referencial");
        }
    }
}