package liga.tasks.ru.learn.exceptions.author;

import org.springframework.http.HttpStatus;

import liga.tasks.ru.learn.exceptions.common.BaseRuntimeException;

public class FilterAuthorsException extends BaseRuntimeException {

    public FilterAuthorsException(Throwable e) {
        super("Возникла ошибка при поиске авторов", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
