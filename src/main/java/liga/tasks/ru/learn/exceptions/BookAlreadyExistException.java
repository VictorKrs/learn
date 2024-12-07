package liga.tasks.ru.learn.exceptions;

import org.springframework.http.HttpStatus;

import liga.tasks.ru.learn.entities.Book;

public class BookAlreadyExistException extends BaseRuntimeException {

    public BookAlreadyExistException(Book book) {
        super("Книга с названием \"" + book.getTitle() + "\" уже существует", HttpStatus.BAD_REQUEST);
    }
}
