package liga.tasks.ru.learn.exceptions;

import org.springframework.http.HttpStatus;

public class BookNotFoundException extends BaseRuntimeException{

    public BookNotFoundException(Long id) {
        super("Не найдена книга с id: " + id, HttpStatus.NOT_FOUND);
    }
}
