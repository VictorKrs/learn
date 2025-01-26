package liga.tasks.ru.learn.exceptions.author;

import org.springframework.http.HttpStatus;

import liga.tasks.ru.learn.entities.Author;
import liga.tasks.ru.learn.exceptions.common.BaseRuntimeException;

public class AuthorAlreadyExistException extends BaseRuntimeException {

    public AuthorAlreadyExistException(Author author) {
        super("Автор \"" + author.getFullName() + "\" уже существует", HttpStatus.BAD_REQUEST);
    }
}
