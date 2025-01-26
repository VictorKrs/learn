package liga.tasks.ru.learn.exceptions.book;

import org.springframework.http.HttpStatus;

import liga.tasks.ru.learn.entities.Book;
import liga.tasks.ru.learn.exceptions.common.BaseRuntimeException;

public class BookAlreadyExistException extends BaseRuntimeException {

    public BookAlreadyExistException(Book book) {
        super("Произведение с названием \"" + book.getTitle() + "\" уже существует", HttpStatus.BAD_REQUEST);
    }
}
