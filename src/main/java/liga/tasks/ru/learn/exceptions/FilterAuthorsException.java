package liga.tasks.ru.learn.exceptions;

import org.springframework.http.HttpStatus;

public class FilterAuthorsException extends BaseRuntimeException {

    public FilterAuthorsException(Throwable e) {
        super("Возникла ошибка при поиске авторов", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
