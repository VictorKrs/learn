package liga.tasks.ru.learn.exceptions.executor;

import org.springframework.http.HttpStatus;

import liga.tasks.ru.learn.exceptions.common.BaseRuntimeException;

public class FutureGetException extends BaseRuntimeException {
    public FutureGetException(Throwable e) {
        super("Возникла ошибка при получении значения асинхронной обработки", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
