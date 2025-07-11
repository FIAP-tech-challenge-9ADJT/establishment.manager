package tech.challenge.establishment.manager.services;

import tech.challenge.establishment.manager.dtos.user.CreateUserDTO;
import tech.challenge.establishment.manager.dtos.user.UpdateUserDTO;
import tech.challenge.establishment.manager.entities.User;

public interface UserService {
    User findById(Long id);

    User save(CreateUserDTO user);

    User update(Long id, UpdateUserDTO user);

    void delete(Long id);
}
