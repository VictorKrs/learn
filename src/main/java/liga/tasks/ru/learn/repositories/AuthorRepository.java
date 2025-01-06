package liga.tasks.ru.learn.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import liga.tasks.ru.learn.entities.Author;

public interface AuthorRepository extends JpaRepository<Author, Long> {

    public Optional<Author> findBySecondNameAndFirstNameAndMiddleName(String secondName, String firstName, String middleName);
}
