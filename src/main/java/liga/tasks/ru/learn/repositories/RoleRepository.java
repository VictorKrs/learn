package liga.tasks.ru.learn.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import liga.tasks.ru.learn.entities.Role;

public interface RoleRepository extends CrudRepository<Role, Long> {

    Optional<Role> findByName(String name);
}
