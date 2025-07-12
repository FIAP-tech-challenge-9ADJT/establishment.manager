package tech.challenge.establishment.manager.services.impl;

import tech.challenge.establishment.manager.dtos.user.CreateUserDTO;
import tech.challenge.establishment.manager.dtos.user.UpdateUserDTO;
import tech.challenge.establishment.manager.entities.Address;
import tech.challenge.establishment.manager.entities.User;
import tech.challenge.establishment.manager.repositories.UserRepository;
import tech.challenge.establishment.manager.services.UserService;

import java.time.LocalDateTime;
import java.util.UUID;

public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User save(CreateUserDTO createUserDTO) {
        var entity = new User(null, createUserDTO.name(), createUserDTO.email(), createUserDTO.login(), createUserDTO.password(), LocalDateTime.now(), null, null);
        return userRepository.save(entity);
    }

    @Override
    public User update(Long id, UpdateUserDTO updateUserDTO) {
        var userEntity = userRepository.findById(id);

        if(userEntity.isPresent()){
            var user = userEntity.get();

            if(updateUserDTO.name() != null && !updateUserDTO.name().isEmpty()){
                user.setName(updateUserDTO.name());
            }

            if(updateUserDTO.email() != null && !updateUserDTO.email().isEmpty()){
                user.setEmail(updateUserDTO.email());
            }

            if(updateUserDTO.login() != null && !updateUserDTO.login().isEmpty()){
                user.setLogin(updateUserDTO.login());
            }

            if(updateUserDTO.password() != null && !updateUserDTO.password().isEmpty()){
                user.setPassword(updateUserDTO.password());
            }

            user.setUpdatedAt(LocalDateTime.now());

            return userRepository.save(user);
        }else{
            return null;
        }
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public void delete(Long id) {
        var userExists = userRepository.existsById(id);
        if (userExists) {
            userRepository.deleteById(id);
        }
    }
}
