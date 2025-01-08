package liga.tasks.ru.learn.exceptions;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;

public class AuthorNotFoundException extends BaseRuntimeException{

    public AuthorNotFoundException(Long id) {
        super("Не найден автор с id: " + id, HttpStatus.NOT_FOUND);
    }

    public AuthorNotFoundException(List<Long> id) {
        super("Не найдены авторы с id: " + id.stream().map(Object::toString).collect(Collectors.joining(", ")), HttpStatus.NOT_FOUND);
    }
}
