package liga.tasks.ru.learn.services;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import liga.tasks.ru.learn.consts.Roles;
import liga.tasks.ru.learn.dto.users.UserRegistration;
import liga.tasks.ru.learn.entities.Role;
import liga.tasks.ru.learn.entities.User;
import liga.tasks.ru.learn.exceptions.user.UserNotFoundException;
import liga.tasks.ru.learn.repositories.UserRepository;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));

        return convertUserToDetails(user);
    }

    public User createNewUser(UserRegistration userInfo) {
        log.info("12345 encr: {}", passwordEncoder.encode("12345"));
        User user = User.builder()
                        .username(userInfo.getUsername())
                        .password(passwordEncoder.encode(userInfo.getPassword()))
                        .roles(List.of(roleService.getRoleByName(Roles.USER_ROLE_DB).orElseThrow(() -> new RuntimeException("Не найдена роль по-умолчанию"))))
                        .build();
        
        return userRepository.save(user);
    }

    private UserDetails convertUserToDetails(User user) {
        return new org.springframework.security.core.userdetails.User(
            user.getUsername(),
            user.getPassword(),
            user.getRoles().stream()
                .map(Role::getName)
                .map(SimpleGrantedAuthority::new)
                .toList()
        );
    }
}
