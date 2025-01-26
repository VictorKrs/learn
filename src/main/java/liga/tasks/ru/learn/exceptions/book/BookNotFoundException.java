package liga.tasks.ru.learn.exceptions.book;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;

import liga.tasks.ru.learn.exceptions.common.BaseRuntimeException;

public class BookNotFoundException extends BaseRuntimeException{

    public BookNotFoundException(Long id) {
        super("Не найдено произведение с id: " + id, HttpStatus.NOT_FOUND);
    }

    public BookNotFoundException(List<Long> id) {
        super("Не найдены произведения с id: " + id.stream().map(Object::toString).collect(Collectors.joining(", ")), HttpStatus.NOT_FOUND);
    }
}
