package liga.tasks.ru.learn.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import liga.tasks.ru.learn.entities.Book;

public interface BookRepository extends JpaRepository<Book, Long> {

}
