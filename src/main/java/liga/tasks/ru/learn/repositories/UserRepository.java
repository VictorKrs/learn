package liga.tasks.ru.learn.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import liga.tasks.ru.learn.entities.User;

public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByUsername(String username);
}
