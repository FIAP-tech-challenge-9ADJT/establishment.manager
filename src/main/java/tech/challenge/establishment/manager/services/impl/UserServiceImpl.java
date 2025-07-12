package tech.challenge.establishment.manager.services.impl;

import org.springframework.stereotype.Service;
import tech.challenge.establishment.manager.dtos.user.CreateUserDTO;
import tech.challenge.establishment.manager.dtos.user.UpdateUserDTO;
import tech.challenge.establishment.manager.entities.Address;
import tech.challenge.establishment.manager.entities.User;
import tech.challenge.establishment.manager.repositories.AddressRepository;
import tech.challenge.establishment.manager.repositories.UserRepository;
import tech.challenge.establishment.manager.services.UserService;

import java.time.LocalDateTime;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository,  AddressRepository addressRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User save(CreateUserDTO createUserDTO) {
        try{
            var entity = new User(createUserDTO);
            return userRepository.save(entity);
        }catch(Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public User update(Long id, UpdateUserDTO updateUserDTO) {
        try{
            var userEntity = userRepository.findById(id);
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

            if(updateUserDTO.address() != null){
                user.setAddress(updateUserDTO.address());
            }

            user.setUpdatedAt(LocalDateTime.now());

            return userRepository.save(user);

        }catch(Exception e){
            throw new RuntimeException(e.getMessage());
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
