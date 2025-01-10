package liga.tasks.ru.learn.exceptions;

import org.springframework.http.HttpStatus;

public class FutureGetException extends BaseRuntimeException {
    public FutureGetException(Throwable e) {
        super("Возникла ошибка при получении значения асинхронной обработки", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
