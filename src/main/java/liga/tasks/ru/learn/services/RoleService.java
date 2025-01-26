package liga.tasks.ru.learn.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import liga.tasks.ru.learn.entities.Role;
import liga.tasks.ru.learn.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public Optional<Role> getRoleByName(String roleName) {
        return roleRepository.findByName(roleName);
    }
}
