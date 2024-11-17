package liga.tasks.ru.learn.exceptions;

import org.springframework.http.HttpStatus;

public class AuthorNotFoundException extends BaseRuntimeException{

    public AuthorNotFoundException(Long id) {
        super("Не найден автор с id: " + id, HttpStatus.NOT_FOUND);
    }
}
