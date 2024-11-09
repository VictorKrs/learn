package liga.tasks.ru.learn.services.exceptions;

import liga.tasks.ru.learn.entities.Author;

public class AuthorAlreadyExistException extends RuntimeException {

    public AuthorAlreadyExistException(Author author) {
        super("Автор с именем \"" + author.getName() + "\" уже существует");
    }
}
