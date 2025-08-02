package tech.challenge.establishment.manager.services.impl;

import tech.challenge.establishment.manager.dtos.user.ChangePasswordDTO;
import tech.challenge.establishment.manager.entities.User;
import tech.challenge.establishment.manager.repositories.UserRepository;
import tech.challenge.establishment.manager.services.AuthService;
import tech.challenge.establishment.manager.services.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tech.challenge.establishment.manager.exceptions.UnauthorizedException;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public AuthServiceImpl(UserService userService, PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Override
    public void changePassword(Long userId, ChangePasswordDTO dto) {
        User user = userService.findById(userId);

        if (!passwordEncoder.matches(dto.currentPassword(), user.getPassword())) {
            throw new UnauthorizedException("Senha atual incorreta");
        }

        user.setPassword(passwordEncoder.encode(dto.newPassword()));
        userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByLogin(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com login: " + username));
    }
}