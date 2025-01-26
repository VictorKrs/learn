package liga.tasks.ru.learn.exceptions.auth;

import org.springframework.http.HttpStatus;

import liga.tasks.ru.learn.exceptions.common.BaseRuntimeException;

public class ErrorTokenException extends BaseRuntimeException  {

    public ErrorTokenException() {
        super("Неверный токен", HttpStatus.UNAUTHORIZED);
    }

}
