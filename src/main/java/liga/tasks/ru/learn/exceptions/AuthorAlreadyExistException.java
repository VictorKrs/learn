package liga.tasks.ru.learn.exceptions;

import org.springframework.http.HttpStatus;

import liga.tasks.ru.learn.entities.Author;

public class AuthorAlreadyExistException extends BaseRuntimeException {

    public AuthorAlreadyExistException(Author author) {
        super("Автор \"" + author.getFullName() + "\" уже существует", HttpStatus.BAD_REQUEST);
    }
}
