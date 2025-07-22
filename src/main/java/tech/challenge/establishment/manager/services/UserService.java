package tech.challenge.establishment.manager.services;

import tech.challenge.establishment.manager.dtos.user.ChangePasswordDTO;
import tech.challenge.establishment.manager.dtos.user.CreateUserDTO;
import tech.challenge.establishment.manager.dtos.user.UpdateUserDTO;
import tech.challenge.establishment.manager.entities.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    User findById(Long id);
    User save(CreateUserDTO dto);
    User update(Long id, UpdateUserDTO dto);
    void delete(Long id);
    void changePassword(Long userId, ChangePasswordDTO dto);
}
