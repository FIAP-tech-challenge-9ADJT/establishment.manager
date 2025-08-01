package tech.challenge.establishment.manager.services;

import tech.challenge.establishment.manager.dtos.user.ChangePasswordDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AuthService extends UserDetailsService {

    void changePassword(Long userId, ChangePasswordDTO dto);
}