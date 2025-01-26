package liga.tasks.ru.learn.exceptions.auth;

import org.springframework.http.HttpStatus;

import liga.tasks.ru.learn.exceptions.common.BaseRuntimeException;

public class WrongUserCredentialsException extends BaseRuntimeException {

    public WrongUserCredentialsException() {
        super("Неверные логин или пароль", HttpStatus.BAD_REQUEST);
    }
}
