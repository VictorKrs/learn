package liga.tasks.ru.learn.exceptions;

import org.springframework.http.HttpStatus;

import liga.tasks.ru.learn.entities.Author;

public class AuthorAlreadyExistException extends BaseRuntimeException {

    public AuthorAlreadyExistException(Author author) {
        super("Автор с именем \"" + author.getName() + "\" уже существует", HttpStatus.BAD_REQUEST);
    }
}
