package liga.tasks.ru.learn.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import liga.tasks.ru.learn.entities.Author;

public interface AuthorRepository extends JpaRepository<Author, Long> {

}
