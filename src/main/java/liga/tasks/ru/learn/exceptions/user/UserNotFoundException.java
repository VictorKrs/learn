package liga.tasks.ru.learn.exceptions.user;

import org.springframework.http.HttpStatus;

import liga.tasks.ru.learn.exceptions.common.BaseRuntimeException;

public class UserNotFoundException extends BaseRuntimeException {

    public UserNotFoundException(String username) {
        super("Не найден пользователь с именем: " + username, HttpStatus.BAD_REQUEST);
    }
}
